package com.toothtrek.bookings.request.booking;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.bookings.entity.Booking;
import com.toothtrek.bookings.repository.BookingRepository;
import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;

@Configuration
public class BookingStateRequestHandler implements RequestHandlerInterface {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    private ResponseHandler responseHandler;

    private final String[] MESSAGE_PROPERTIES = { "bookingId", "state" };
    private final List<String> ALLOWED_STATES = List.of("booked", "confirmed", "rejected", "cancelled", "completed");

    private final HashMap<String, List<String>> ALLOWED_STATE_CHANGES_MAP = new HashMap<String, List<String>>() {
        {
            put("booked", List.of("confirmed", "rejected", "cancelled"));
            put("confirmed", List.of("completed", "cancelled"));
            put("rejected", List.of());
            put("cancelled", List.of());
            put("completed", List.of());
        }
    };

    /*
     * Possible states and who can change them:
     * automatic: booked (on creation)
     * patient: cancelled
     * dentist: confirmed, rejected, completed
     * 
     * Possible state changes:
     * booked -> confirmed/rejected/cancelled
     * confirmed -> completed/cancelled
     */

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

        // Check if state is allowed
        String state = json.get("state").getAsString().toLowerCase();
        if (!ALLOWED_STATES.contains(state)) {
            responseHandler.reply(ResponseStatus.ERROR, "State " + state + " not allowed", request);
            return;
        }

        long bookingId = json.get("bookingId").getAsLong();
        Booking booking;
        try {
            booking = bookingRepository.findById(bookingId).get();
        } catch (NoSuchElementException e) {
            responseHandler.reply(ResponseStatus.ERROR, "No booking with id " + bookingId + " found", request);
            return;
        }

        // If booking is cancelled, check if it belongs to patient
        if (state.equals("cancelled")) {
            // Check if patientId is provided
            try {
                if (!booking.getPatient().getId().equals(json.get("patientId").getAsString())) {
                    responseHandler.reply(ResponseStatus.ERROR, "Booking does not belong to patient", request);
                    return;
                }
            } catch (NullPointerException e) {
                responseHandler.reply(ResponseStatus.ERROR, "No patientId provided", request);
                return;
            }
        }

        // Change state -> synchronized
        changeState(booking, state, request);
    }

    private synchronized void changeState(Booking booking, String state, MqttMessage request) {
        // Current state
        String currentState = booking.getState().toString();

        // Check if state is allowed
        if (!ALLOWED_STATE_CHANGES_MAP.get(currentState).contains(state)) {
            responseHandler.reply(ResponseStatus.ERROR,
                    "State change from " + currentState + " to " + state + " not allowed", request);
            return;
        }

        // Change state
        booking.setState(Booking.State.valueOf(state));

        // Save booking
        bookingRepository.save(booking);

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
