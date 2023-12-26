package com.toothtrek.bookings.notification;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.toothtrek.bookings.mqtt.MqttHandler;

@Component
public class NotificationHandler {
    @Autowired
    private MqttHandler mqttHandler;

    public void sendNotification(Long bookingId, String type) {
        

        // Create JSON object
        JsonObject json = new JsonObject();
        json.addProperty("booking_id", bookingId.toString());
        json.addProperty("type", type);

        // Create message
        MqttMessage request = new MqttMessage();
        request.setPayload(json.toString().getBytes());

        // Publish reply
        mqttHandler.publish("toothtrek/notification/send", request);
    }
}
