package com.toothtrek.bookings.request.timeslot;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.bookings.entity.Timeslot;
import com.toothtrek.bookings.repository.DentistRepository;
import com.toothtrek.bookings.repository.OfficeRepository;
import com.toothtrek.bookings.repository.TimeslotRepository;
import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;

@Configuration
public class TimeslotCreateRequestHandler implements RequestHandlerInterface {

    @Autowired
    private TimeslotRepository timeSlotRepo;

    @Autowired
    private OfficeRepository officeRepo;

    @Autowired
    private DentistRepository dentistRepo;

    @Autowired
    private ResponseHandler responseHandler;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final String[] MESSAGE_PROPERTIES = { "officeId", "dentistId", "dateAndTime" };

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

        // check if officeId and dentistId exist

        Long officeId = json.get("officeId").getAsLong();

        if (!officeRepo.findById(officeId).isPresent()) {
            responseHandler.reply(ResponseStatus.ERROR, "Office with id " + officeId + " does not exist", request);
            return;
        }

        Long dentistId = json.get("dentistId").getAsLong();

        if (!dentistRepo.findById(dentistId).isPresent()) {
            responseHandler.reply(ResponseStatus.ERROR, "Dentist with id " + dentistId + " does not exist", request);
            return;
        }

        // Check if dateAndTime exists and is formatted correctly

        long time;
        String dateAndTimeString = json.get("dateAndTime").getAsString();
        try {
            time = sdf.parse(dateAndTimeString).getTime();
        } catch (ParseException e) {
            responseHandler.reply(ResponseStatus.ERROR, "Wrongly formatted date and time", request);
            return;
        }
        Timestamp dateAndTimeTimestamp = new Timestamp(time);

        // Check if dateAndTime is in the future
        if (dateAndTimeTimestamp.before(new Timestamp(System.currentTimeMillis()))) {
            responseHandler.reply(ResponseStatus.ERROR, "Date and time is in the past", request);
            return;
        }

        // Create timeslot -> synchronized to prevent multiple entries of the same
        // timeslot
        createTimeslot(dentistId, officeId, dateAndTimeTimestamp, request);
    }

    private synchronized void createTimeslot(
            long dentistId, long officeId, Timestamp dateAndTime, MqttMessage request) {

        // Check if timeslot already exists
        Timeslot timeslot = timeSlotRepo.findByDentistIdAndOfficeIdAndDateAndTime(dentistId, officeId, dateAndTime);
        if (timeslot != null) {
            responseHandler.reply(ResponseStatus.ERROR, "Timeslot already exists", request);
            return;
        }

        // Create timeslot
        timeslot = new Timeslot(officeRepo.findById(officeId).get(), dentistRepo.findById(dentistId).get(), dateAndTime);
        timeSlotRepo.save(timeslot);

        // Reply with success
        responseHandler.reply(ResponseStatus.SUCCESS, "Timeslot created", request);
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
