package com.toothtrek.bookings.request.dentist;

import java.sql.Timestamp;
import java.util.List;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.bookings.entity.Dentist;
import com.toothtrek.bookings.repository.DentistRepository;
import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;
import com.toothtrek.bookings.serializer.json.TimestampSerializer;

@Configuration
public class DentistGetRequestHandler implements RequestHandlerInterface {

    @Autowired
    private DentistRepository dentistRepository;

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

        // Create JSON response
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, new TimestampSerializer())
                .create();

        if (json.has("id")) {
            try {
                // Get dentist by id
                Long dentistId = json.get("id").getAsLong();
                Dentist dentist = dentistRepository.findById(dentistId).get();
                responseHandler.reply(ResponseStatus.SUCCESS, gson.toJson(dentist), request);
            } catch (Exception e) {
                responseHandler.reply(ResponseStatus.ERROR, "Dentist not found", request);
            }
        } else {
            // Get all dentists
            List<Dentist> dentists = dentistRepository.findAll();
            responseHandler.reply(ResponseStatus.SUCCESS, gson.toJson(dentists), request);
        }
    }
}
