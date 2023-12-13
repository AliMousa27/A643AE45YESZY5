package com.toothtrek.dentalRecord.request.record;

import java.util.NoSuchElementException;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.dentalRecord.request.RequestHandlerInterface;
import com.toothtrek.dentalRecord.response.ResponseHandler;
import com.toothtrek.dentalRecord.response.ResponseStatus;
import com.toothtrek.dentalRecord.entity.Booking;
import com.toothtrek.dentalRecord.entity.Patient;
import com.toothtrek.dentalRecord.entity.Record;
import com.toothtrek.dentalRecord.entity.Timeslot;
import com.toothtrek.dentalRecord.repository.RecordRepository;
import com.toothtrek.dentalRecord.repository.TimeslotRepository;
import com.toothtrek.dentalRecord.repository.BookingRepository;

@Configuration
public class RecordCreateRequestHandler implements RequestHandlerInterface {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private TimeslotRepository timeslotRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ResponseHandler responseHandler;

    private final String[] MESSAGE_PROPERTIES = { "timeslotId", "notes" };

    @Override
    public void handle(MqttMessage request) {

        // example of a message payload:
        // {
        // "timeslotId": "1234567890",
        // "notes": "This is a note"
        // }

        // Create json object from payload
        JsonObject json;
        try {
            json = new Gson().fromJson(new String(request.getPayload()), JsonObject.class);
        } catch (JsonSyntaxException e) {
            responseHandler.reply(ResponseStatus.ERROR, "Invalid JSON", request);
            return;
        }

        // Check if all required properties are present
        if (checkMissingJSONProperties(json, MESSAGE_PROPERTIES, request)) {
            return;
        }

        // Check if timeslotId exists
        Timeslot timeslot = null;
        try {
            Long timeslotId = json.get("timeslotId").getAsLong();
            timeslot = timeslotRepository.findById(timeslotId).orElseThrow();
        } catch (NoSuchElementException e) {
            responseHandler.reply(ResponseStatus.ERROR, request);
            return;
        }

        String notes = json.get("notes").getAsString();
        if (notes == null) {
            notes = "";
        }

        Booking booking = bookingRepository.findCompletedBookingByTimeslotId(timeslot.getId());
        if (booking == null) {
            responseHandler.reply(ResponseStatus.ERROR, "No completed booking found for timeslot", request);
            return;
        }

        // Find patient
        Patient patient = booking.getPatient();

        // Create a new Record
        Record record = new Record(patient, timeslot, notes);

        // Create Record -> synchronized
        createRecord(record, request);
    }

    private synchronized void createRecord(Record record, MqttMessage request) {

        // Check if the timeslot is still available
        if (recordRepository.findByTimeslot(record.getTimeslot()) != null) {
            responseHandler.reply(ResponseStatus.ERROR, "Timeslot already has a record", request);
            return;
        }

        // Save the Record to the database
        recordRepository.save(record);

        // Reply
        responseHandler.reply(ResponseStatus.SUCCESS, "Record successfully created.", request);
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
