package com.toothtrek.bookings.request.booking;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.bookings.entity.Booking;
import com.toothtrek.bookings.entity.Patient;
import com.toothtrek.bookings.entity.Timeslot;
import com.toothtrek.bookings.repository.BookingRepository;
import com.toothtrek.bookings.repository.PatientRepository;
import com.toothtrek.bookings.repository.TimeslotRepository;
import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;

@Configuration
public class BookingCreateRequestHandler implements RequestHandlerInterface {

    @Autowired
    private TimeslotRepository timeSlotRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private ResponseHandler responseHandler;

    private final String[] MESSAGE_PROPERTIES = { "patient", "timeslotId" };
    private final String[] MESSAGE_PROPERTIES_PATIENT = { "id", "name", "dateOfBirth" };

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
        checkJSONProperties(json, MESSAGE_PROPERTIES, request);

        JsonObject patientJSON = json.get("patient").getAsJsonObject();

        // Check if patient JSON contains all required properties
        checkJSONProperties(patientJSON, MESSAGE_PROPERTIES_PATIENT, request);

        // Check if timeslot exists
        try {
            Long timeslotId = json.get("timeslotId").getAsLong();
            timeSlotRepo.findById(timeslotId).orElseThrow();
        } catch (NoSuchElementException e) {
            responseHandler.reply(ResponseStatus.ERROR, request);
            return;
        }

        // Create booking -> synchronized to prevent double bookings
        createBooking(json, patientJSON, request);
    }

    private synchronized void createBooking(JsonObject json, JsonObject patientJSON, MqttMessage request) {
        // Find if any booking with the timeslotId exists (Already booked)
        if (timeSlotRepo.isBooked(json.get("timeslotId").getAsLong())) {
            responseHandler.reply(ResponseStatus.ERROR, "Timeslot already booked", request);
            return;
        }

        long timeslotId = json.get("timeslotId").getAsLong();
        Timeslot timeslot = timeSlotRepo.findById(timeslotId).get();

        // Create booking
        Booking booking = new Booking();
        booking.setTimeslot(timeslot);

        // Find patient or create new patient
        String patientId = patientJSON.get("id").getAsString();
        Patient patient = new Patient();
        try {
            patient = patientRepo.findById(patientId).get();
        } catch (NoSuchElementException e) {
            patient.setId(patientJSON.get("id").getAsString());
            patient.setName(patientJSON.get("name").getAsString());

            // Convert date to timestamp
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                long time = sdf.parse(patientJSON.get("dateOfBirth").getAsString()).getTime();
                Timestamp ts = new Timestamp(time);
                patient.setDateOfBirth(ts);
            } catch (ParseException pe) {
                responseHandler.reply(ResponseStatus.ERROR, "Wrongly formatted date", request);
                return;
            }

            patientRepo.save(patient);
        }
        booking.setPatient(patient);

        // Set patientId and save booking
        bookingRepo.save(booking);

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
    private void checkJSONProperties(JsonObject json, String[] properties, MqttMessage request) {
        for (String property : properties) {
            if (!json.has(property)) {
                responseHandler.reply(ResponseStatus.ERROR, "No " + property + " provided", request);
                return;
            }
        }
    }
}
