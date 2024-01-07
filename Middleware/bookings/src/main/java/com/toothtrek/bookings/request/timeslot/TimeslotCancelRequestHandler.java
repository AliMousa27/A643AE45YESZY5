package com.toothtrek.bookings.request.timeslot;

import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.bookings.entity.Booking;
import com.toothtrek.bookings.entity.Timeslot;
import com.toothtrek.bookings.repository.BookingRepository;
import com.toothtrek.bookings.repository.TimeslotRepository;
import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;
import com.toothtrek.bookings.notification.NotificationHandler;

@Configuration
public class TimeslotCancelRequestHandler implements RequestHandlerInterface {

    @Autowired
    private TimeslotRepository timeslotRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ResponseHandler responseHandler;

    @Autowired
    private NotificationHandler notificationHandler;

    @Override
    public void handle(MqttMessage request) {

        // Check if payload is JSON
        JsonObject json = null;
        try {
            json = new Gson().fromJson(new String(request.getPayload()), JsonObject.class);
        } catch (JsonSyntaxException e) {
            responseHandler.reply(ResponseStatus.ERROR, "Wrongly formatted JSON", request);
        }

        // Check if timeslotId is present
        long timeslotId;
        try {
            timeslotId = json.get("timeslotId").getAsLong();
        } catch (Exception e) {
            responseHandler.reply(ResponseStatus.ERROR, "Invalid timeslotId", request);
            return;
        }

        // Get timeslot
        Timeslot timeslot;
        try {
            timeslot = timeslotRepository.findById(timeslotId).get();
        } catch (Exception e) {
            responseHandler.reply(ResponseStatus.ERROR, "Timeslot does not exist", request);
            return;
        }

        // Check if timeslot is already cancelled
        if (timeslot.getState() == Timeslot.State.cancelled) {
            responseHandler.reply(ResponseStatus.ERROR, "Timeslot already cancelled", request);
            return;
        }

        cancelBooking(timeslot, request);
    }

    private synchronized void cancelBooking(Timeslot timeslot, MqttMessage request) {
        // Get bookings for timeslot
        Booking updatedBooking = null;
        List<Booking> bookings = bookingRepository.findByTimeslotId(timeslot.getId());
        for (Booking booking : bookings) {
            if (booking.getState() == Booking.State.cancelled || booking.getState() == Booking.State.rejected) {
                continue;
            }

            if (booking.getState() == Booking.State.completed) {
                responseHandler.reply(ResponseStatus.ERROR, "Timeslot already completed", request);
                return;
            }

            // Cancel booking
            booking.setState(Booking.State.cancelled);
            updatedBooking = booking;
        }

        // Save bookings, separated from loop to avoid saving if:
        // timeslot already completed
        bookingRepository.saveAll(bookings);

        // Send email to notify user
        notificationHandler.sendNotification(updatedBooking.getId(), "cancelled");

        // Cancel timeslot & save
        timeslot.setState(Timeslot.State.cancelled);
        timeslotRepository.save(timeslot);

        // Reply
        responseHandler.reply(ResponseStatus.SUCCESS, "Timeslot cancelled", request);
    }

}
