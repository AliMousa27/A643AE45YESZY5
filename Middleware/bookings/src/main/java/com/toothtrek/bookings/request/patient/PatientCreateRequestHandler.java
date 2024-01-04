package com.toothtrek.bookings.request.patient;


import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.bookings.entity.Patient;
import com.toothtrek.bookings.repository.PatientRepository;
import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;

@Configuration
public class PatientCreateRequestHandler implements RequestHandlerInterface {
    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private ResponseHandler responseHandler;

    private final String[] MESSAGE_PROPERTIES = { "id", "name", "email" };

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

        // Create patient -> synchronized to prevent double patients
        createPatient(json, request);
    }

    private synchronized void createPatient(JsonObject json, MqttMessage request) {

        // Check if patient already exists
        if (patientRepo.existsById(json.get("id").getAsString())) {
            responseHandler.reply(ResponseStatus.SUCCESS, "Patient already exists", request);
            return;
        }

        // Create patient
        Patient patient = new Patient();
        patient.setId(json.get("id").getAsString());
        patient.setName(json.get("name").getAsString());
        patient.setEmail(json.get("email").getAsString());
        patient.setNotified(true);

        patientRepo.save(patient);

        // Reply with success
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
