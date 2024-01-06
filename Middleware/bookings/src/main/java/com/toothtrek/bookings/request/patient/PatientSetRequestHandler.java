package com.toothtrek.bookings.request.patient;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;
import com.toothtrek.bookings.repository.PatientRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.bookings.entity.Patient;

@Configuration
public class PatientSetRequestHandler implements RequestHandlerInterface {

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
            // Set patient
            setPatient(json, patient, request);
        }
    }

    private synchronized void setPatient(JsonObject json, Patient patient, MqttMessage request) {
        // Set patient properties if they exist
        if (json.has("name")) {
            patient.setName(json.get("name").getAsString());
        }
        if (json.has("email")) {
            patient.setEmail(json.get("email").getAsString());
        }
        if (json.has("notified")) {
            patient.setNotified(json.get("notified").getAsBoolean());
        }

        // Save patient
        patientRepo.save(patient);

        // Reply
        responseHandler.reply(ResponseStatus.SUCCESS, "Patient updated", request);
    }
}
