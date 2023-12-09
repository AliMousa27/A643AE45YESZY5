package com.toothtrek.notifications.request.notification;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.toothtrek.notifications.email.EmailService;
import com.toothtrek.notifications.entity.Notification;
import com.toothtrek.notifications.repository.NotificationRepository;
import com.toothtrek.notifications.request.RequestHandlerInterface;
import com.toothtrek.notifications.response.ResponseHandler;
import com.toothtrek.notifications.response.ResponseStatus;

@Configuration
public class NotificationCreateRequestHandler implements RequestHandlerInterface {

    @Autowired
    private NotificationRepository notificationRepo;

    @Autowired
    private ResponseHandler responseHandler;

    @Autowired
    private EmailService emailService;

    @Override
    // !NOTE! 'synchronized'
    public synchronized void handle(MqttMessage request) {
        // Example of a message payload:
        // {
        // "title": "Your appointment is coming up!",
        // "message": "Your appointment is coming up tomorrow.",
        // "email": "this@that.com",
        // "time": "2021-05-05T00:00:00.000+00:00"
        // "booking_id": "1234567890",
        // "scheduled": "true"
        // }

        // Create json object from payload
        JsonObject json = new Gson().fromJson(new String(request.getPayload()), JsonObject.class);

        if (!json.get("scheduled").getAsBoolean()) {

            String email = json.get("email").getAsString();
            String title = json.get("title").getAsString();
            String message = json.get("message").getAsString();
            emailService.sendNotificationEmail(email, title, message);

        } else {

            // Create notification
            Notification notification = new Notification();
            notification.setTitle(json.get("title").getAsString());
            notification.setMessage(json.get("message").getAsString());
            notification.setEmail(json.get("email").getAsString());
            notification.setBookingId(json.get("booking_id").getAsString());

            // Convert date to timestamp
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                long time = sdf.parse(json.get("time").getAsString()).getTime();
                Timestamp ts = new Timestamp(time);
                notification.setTime(ts);
                notificationRepo.save(notification);
            } catch (ParseException pe) {
                responseHandler.reply(ResponseStatus.ERROR, "Wrongly formatted date", request);
                return;
            }
        }

        // Reply with success
        System.out.println("SUCCESS TEST");
        responseHandler.reply(ResponseStatus.SUCCESS, request);
    }

}
