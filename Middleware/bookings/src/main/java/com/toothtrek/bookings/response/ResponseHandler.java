package com.toothtrek.bookings.response;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.toothtrek.bookings.mqtt.MqttHandler;

@Component
/**
 * This class is used as an interface to reply to requests.
 */
public class ResponseHandler {
    @Autowired
    private MqttHandler mqttHandler;

    /**
     * Reply with status. Use received MqttMessage to get response topic.
     * 
     * @param status  ResponseStatus enum
     * @param request MqttMessage
     * @see MqttMessage
     */
    public void reply(ResponseStatus status, MqttMessage request) {
        reply(status, "No message provided", request);
    }

    /**
     * Reply with status to a specific topic.
     * 
     * @param status ResponseStatus enum
     * @param topic  String
     * @see MqttMessage
     */
    public void reply(ResponseStatus status, String topic) {
        reply(status, "No message provided", topic);
    }

    /**
     * Reply with status and message.
     * Use received MqttMessage to get response topic.
     * 
     * @param status  ResponseStatus enum
     * @param message String
     * @param request MqttMessage
     * @see MqttMessage
     */
    public void reply(ResponseStatus status, String message, MqttMessage request) {
        // Get response topic from message properties
        String topic = request.getProperties().getResponseTopic();

        reply(status, message, topic);
    }

    /**
     * Reply with status and message to a specific topic.
     * 
     * @param status  ResponseStatus enum
     * @param message String
     * @param topic   String
     */
    public void reply(ResponseStatus status, String message, String topic) {
        // Create and set payload
        MqttMessage reply = new MqttMessage();
        JsonObject json = new JsonObject();
        json.addProperty("status", status.toString());
        json.addProperty("message", message);
        reply.setPayload(json.toString().getBytes());

        // Publish reply
        mqttHandler.publish(topic, reply);
    }
}
