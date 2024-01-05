package com.toothtrek.bookings.request.office;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.bookings.entity.Office;
import com.toothtrek.bookings.repository.OfficeRepository;
import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;

@Configuration
public class OfficeUpdateRequestHandler implements RequestHandlerInterface {
    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private ResponseHandler responseHandler;

    private final String[] MESSAGE_PROPERTIES = { "id" };

    @Override
    public void handle(MqttMessage request) {

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

        try {
            // Get office by id
            Long officeId = json.get("id").getAsLong();
            Office office = officeRepository.findById(officeId).get();

            // set office properties if they exist
            if (json.has("name")) {
                office.setName(json.get("name").getAsString());
            }
            if (json.has("address")) {
                office.setAddress(json.get("address").getAsString());
            }
            if (json.has("latitude")) {
                office.setLatitude(json.get("latitude").getAsFloat());
            }
            if (json.has("longitude")) {
                office.setLongitude(json.get("longitude").getAsFloat());
            }

            // Save office
            officeRepository.save(office);

            // Reply with success
            responseHandler.reply(ResponseStatus.SUCCESS, request);
        } catch (Exception e) {
            responseHandler.reply(ResponseStatus.ERROR, "Office not found", request);
        }
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
