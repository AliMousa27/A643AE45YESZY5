package com.toothtrek.bookings.request.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;
import com.toothtrek.bookings.serializer.json.TimestampSerializer;
import com.toothtrek.bookings.repository.PatientRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.bookings.entity.Patient;

@Configuration
public class PatientGetRequestHandler implements RequestHandlerInterface {

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private ResponseHandler responseHandler;

    @Override
    public void handle(MqttMessage request) {
        // Check if payload is JSON
        JsonObject json = null;
        try {
            json = new Gson().fromJson(new String(request.getPayload()), JsonObject.class);
        } catch (JsonSyntaxException e) {
            responseHandler.reply(ResponseStatus.ERROR, "Wrongly formatted JSON", request);
        }

        // Get patient
        Patient patient = patientRepo.findById(json.get("id").getAsString()).orElse(null);

        // Check if patient exists
        if (patient == null) {
            responseHandler.reply(ResponseStatus.ERROR, "Patient does not exist", request);
        } else {
            // Send patient
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, new TimestampSerializer())
                .create();
        String ResponseJson = gson.toJson(patient);
            responseHandler.reply(ResponseStatus.SUCCESS, ResponseJson, request);
        }
    }
}
