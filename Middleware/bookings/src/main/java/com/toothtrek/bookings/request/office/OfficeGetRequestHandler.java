package com.toothtrek.bookings.request.office;

import java.sql.Timestamp;
import java.util.List;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.bookings.entity.Office;
import com.toothtrek.bookings.repository.OfficeRepository;
import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;
import com.toothtrek.bookings.serializer.json.TimestampSerializer;

@Configuration
public class OfficeGetRequestHandler implements RequestHandlerInterface {

    @Autowired
    private OfficeRepository officeRepository;

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
        String jsonOffice = null;

        if (json.has("id")) {
            // Get office by id
            Long officeId = json.get("id").getAsLong();
            Office office = officeRepository.findById(officeId).get();
            if (office == null) {
                responseHandler.reply(ResponseStatus.ERROR, "Office not found", request);
                return;
            }
            jsonOffice = gson.toJson(office);
        } else {
            // Get all offices
            List<Office> offices = officeRepository.findAll();
            jsonOffice = gson.toJson(offices);
        }

        responseHandler.reply(ResponseStatus.SUCCESS, jsonOffice, request);
    }
}
