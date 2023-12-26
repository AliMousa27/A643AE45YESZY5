package com.toothtrek.notifications.request.notification;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.notifications.email.EmailService;
import com.toothtrek.notifications.request.RequestHandlerInterface;
import com.toothtrek.notifications.response.ResponseHandler;
import com.toothtrek.notifications.response.ResponseStatus;
import com.toothtrek.notifications.entity.Booking;
import com.toothtrek.notifications.repository.BookingRepository;

@Configuration
public class NotificationSendRequestHandler implements RequestHandlerInterface {

    @Autowired
    private ResponseHandler responseHandler;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private BookingRepository bookingRepo;

    private final String[] MESSAGE_PROPERTIES = { "booking_id", "type"};

    @Override
    // !NOTE! 'synchronized'
    public synchronized void handle(MqttMessage request) {
        // Example of a message payload:
        // {
        // "booking_id": "1234567890",
        // "type": "booked", //booked, cancelled, confirmed, rejected
        // }

        // Check if payload is JSON
        JsonObject json = null;
        try {
            json = new Gson().fromJson(new String(request.getPayload()), JsonObject.class);
        } catch (JsonSyntaxException e) {
            responseHandler.reply(ResponseStatus.ERROR, "Wrongly formatted JSON", request);
        }

        // Check if JSON contains all required properties
        if (checkMissingJSONProperties(json, MESSAGE_PROPERTIES, request)) {
            return;
        }

        // Check if booking exists
        Booking booking = bookingRepo.findById(json.get("booking_id").getAsLong()).orElse(null);
        if (booking == null) {
            responseHandler.reply(ResponseStatus.ERROR, "Booking does not exist", request);
            return;
        }
        
        Context context = new Context();
        context.setVariable("patientName", booking.getPatient().getName());
        context.setVariable("dentistName", booking.getTimeslot().getDentist().getName());
        context.setVariable("officeName", booking.getTimeslot().getOffice().getName());
        context.setVariable("officeAddress", booking.getTimeslot().getOffice().getAddress());
        context.setVariable("dateAndTime", booking.getTimeslot().getDateAndTime().toString());

        String processedTemplate = templateEngine.process(json.get("type") + "Template", context);

        emailService.sendNotificationEmail(booking.getPatient().getEmail() , 
            "Dental Appointment: " + json.get("type").getAsString(), processedTemplate);
        
        // Reply with success
        System.out.println("SUCCESS TEST");
        responseHandler.reply(ResponseStatus.SUCCESS, request);
    }

    /**
     * Checks if the JSON contains all required properties.
     * If not, it replies with an error.
     * 
     * @param json       JsonObject to check
     * @param properties Required String[] properties
     * @param request    MqttMessage request to reply to
     */
    private boolean checkMissingJSONProperties(JsonObject json, String[] properties, MqttMessage request) {
        for (String property : properties) {
            if (!json.has(property)) {
                responseHandler.reply(ResponseStatus.ERROR, "No " + property + " provided", request);
                return true;
            }
        }
        return false;
    }

}
