package com.toothtrek.bookings.request.dentist;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.bookings.entity.Dentist;
import com.toothtrek.bookings.repository.DentistRepository;
import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;

@Configuration
public class DentistUpdateRequestHandler implements RequestHandlerInterface {
    @Autowired
    private DentistRepository dentistRepository;

    @Autowired
    private ResponseHandler responseHandler;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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
            // Get dentist by id
            Long dentistId = json.get("id").getAsLong();
            Dentist dentist = dentistRepository.findById(dentistId).get();

            // set dentist properties if they exist
            if (json.has("name")) {
                dentist.setName(json.get("name").getAsString());
            }
            if (json.has("dateOfBirth")) {
                long time;
                String dateOfBirthString = json.get("dateOfBirth").getAsString();
                try {
                    time = sdf.parse(dateOfBirthString).getTime();
                } catch (ParseException e) {
                    responseHandler.reply(ResponseStatus.ERROR, "Wrongly formatted date of birth", request);
                    return;
                }
                Timestamp dateOfBirthTimestamp = new Timestamp(time);
                dentist.setDateOfBirth(dateOfBirthTimestamp);
            }

            // Save dentist
            dentistRepository.save(dentist);

            // Reply with success
            responseHandler.reply(ResponseStatus.SUCCESS, request);
        } catch (Exception e) {
            responseHandler.reply(ResponseStatus.ERROR, "Dentist not found", request);
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
