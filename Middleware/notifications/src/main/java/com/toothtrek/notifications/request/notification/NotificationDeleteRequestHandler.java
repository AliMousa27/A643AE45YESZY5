package com.toothtrek.notifications.request.notification;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.toothtrek.notifications.repository.NotificationRepository;
import com.toothtrek.notifications.request.RequestHandlerInterface;
import com.toothtrek.notifications.response.ResponseHandler;
import com.toothtrek.notifications.response.ResponseStatus;

@Configuration
public class NotificationDeleteRequestHandler implements RequestHandlerInterface {

    @Autowired
    private NotificationRepository notificationRepo;

    @Autowired
    private ResponseHandler responseHandler;

    @Override
    // !NOTE! 'synchronized'
    public synchronized void handle(MqttMessage request) {
        // Example of a message payload:
        // {
        // "booking_id": "1234567890"
        // }

        // Create json object from payload
        JsonObject json = new Gson().fromJson(new String(request.getPayload()), JsonObject.class);
        notificationRepo.deleteByBookingId(json.get("booking_id").getAsString());

        // Reply with success
        System.out.println("SUCCESS TEST");
        responseHandler.reply(ResponseStatus.SUCCESS, request);
    }
}
