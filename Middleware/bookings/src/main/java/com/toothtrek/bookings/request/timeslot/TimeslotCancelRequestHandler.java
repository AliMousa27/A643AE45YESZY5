package com.toothtrek.bookings.request.timeslot;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.bookings.repository.TimeslotRepository;
import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;

@Configuration
public class TimeslotCancelRequestHandler implements RequestHandlerInterface {

    @Autowired
    private TimeslotRepository timeslotRepository;

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

        long timeslotId;
        try {
            timeslotId = json.get("timeslotId").getAsLong();
        } catch (Exception e) {
            responseHandler.reply(ResponseStatus.ERROR, "Invalid timeslotId", request);
            return;
        }

        if (timeslotRepository.existsById(timeslotId)) {
            timeslotRepository.deleteById(timeslotId);
            responseHandler.reply(ResponseStatus.SUCCESS, "Timeslot deleted", request);
        } else {
            responseHandler.reply(ResponseStatus.ERROR, "Timeslot not found", request);
        }
    }

}
