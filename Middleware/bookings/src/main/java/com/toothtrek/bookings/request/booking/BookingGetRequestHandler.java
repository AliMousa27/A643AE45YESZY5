package com.toothtrek.bookings.request.booking;

import java.sql.Timestamp;
import java.util.List;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.bookings.entity.Booking;
import com.toothtrek.bookings.entity.Patient;
import com.toothtrek.bookings.repository.BookingRepository;
import com.toothtrek.bookings.repository.PatientRepository;
import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;
import com.toothtrek.bookings.serializer.json.TimestampSerializer;

@Configuration
public class BookingGetRequestHandler implements RequestHandlerInterface {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ResponseHandler responseHandler;

    private final String[] MESSAGE_PROPERTIES = { "patientId" };

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

        String patientId = json.get("patientId").getAsString();
        Patient patient = patientRepository.findById(patientId).get();
        if (patient == null) {
            responseHandler.reply(ResponseStatus.ERROR, "Patient not found", request);
            return;
        }

        List<Booking> bookings = bookingRepository.findByPatient(patient);
        if (bookings == null) {
            responseHandler.reply(ResponseStatus.EMPTY, "No bookings found", request);
            return;
        }

        // Create JSON response
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, new TimestampSerializer())
                .create();
        String jsonBookings = gson.toJson(bookings);

        responseHandler.reply(ResponseStatus.SUCCESS, jsonBookings, request);
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
