package com.toothtrek.notifications.request.notification;

import java.sql.Timestamp;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
            // TODO: send email here
        } else {
            // Create notification
            Notification notification = new Notification();
            notification.setTitle(json.get("title").getAsString());
            notification.setMessage(json.get("message").getAsString());
            notification.setEmail(json.get("email").getAsString());
            notification.setTime(Timestamp.valueOf(json.get("time").getAsString()));
            notification.setBookingId(json.get("booking_id").getAsString());
            notificationRepo.save(notification);
        }

        // Reply with success
        System.out.println("SUCCESS TEST");
        responseHandler.reply(ResponseStatus.SUCCESS, request);
    }

}
