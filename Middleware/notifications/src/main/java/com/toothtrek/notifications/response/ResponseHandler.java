package com.toothtrek.notifications.response;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.notifications.mqtt.MqttHandler;

@Component
/**
 * This class is used as an interface to reply to requests.
 */
public class ResponseHandler {
    @Autowired
    private MqttHandler mqttHandler;

    /**
     * Reply with status.
     * 
     * @param status  ResponseStatus - The status of the response
     * @param request MqttMessage - The MqttMessage of the request
     * @see MqttMessage
     */
    public void reply(ResponseStatus status, MqttMessage request) {
        reply(status, "No content provided", request);
    }

    /**
     * Reply with status and message as json object.
     * 
     * @param status  ResponseStatus
     * @param content JsonObject
     * @param request MqttMessage
     * @see MqttMessage
     */
    public void reply(ResponseStatus status, String content, MqttMessage request) {
        // Get response topic from message properties
        JsonObject json = new Gson().fromJson(new String(request.getPayload()), JsonObject.class);
        String topic = json.get("responseTopic").getAsString();

        // Create JSON object
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("status", status.toString());

        // Check if content is a valid JSON string
        try {
            JsonElement contentJson = new Gson().fromJson(content, JsonElement.class);
            responseJson.add("content", contentJson);
        } catch (JsonSyntaxException e) {
            // If not, add it as a plain string
            responseJson.addProperty("content", content);
        }

        // Create message
        MqttMessage reply = new MqttMessage();
        reply.setPayload(responseJson.toString().getBytes());

        // Publish reply
        mqttHandler.publish(topic, reply);
    }
}
