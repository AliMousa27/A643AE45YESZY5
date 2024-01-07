package com.toothtrek.dentalRecord.request.record;

import java.sql.Timestamp;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.dentalRecord.repository.PatientRepository;
import com.toothtrek.dentalRecord.repository.RecordRepository;
import com.toothtrek.dentalRecord.repository.TimeslotRepository;
import com.toothtrek.dentalRecord.request.RequestHandlerInterface;
import com.toothtrek.dentalRecord.response.ResponseHandler;
import com.toothtrek.dentalRecord.response.ResponseStatus;
import com.toothtrek.dentalRecord.serializer.json.TimestampSerializer;
import com.toothtrek.dentalRecord.entity.Record;

@Configuration
public class RecordGetRequestHandler implements RequestHandlerInterface {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TimeslotRepository timeslotRepository;

    @Autowired
    private ResponseHandler responseHandler;

    @Override
    public void handle(MqttMessage request) {
        JsonObject json;
        try {
            json = new Gson().fromJson(new String(request.getPayload()), JsonObject.class);
        } catch (JsonSyntaxException e) {
            responseHandler.reply(ResponseStatus.ERROR, "Invalid JSON", request);
            return;
        }

        if (json.has("patientId")) {
            getByPatientId(json, request);
            return;
        }

        if (json.has("timeslotId")) {
            getByTimeslotId(json, request);
            return;
        }

        responseHandler.reply(ResponseStatus.ERROR, "Missing JSON properties: patientId or recordId", request);

    }

    private void getByPatientId(JsonObject json, MqttMessage request) {
        String id = null;
        try {
            id = json.get("patientId").getAsString();
        } catch (Exception e) {
            responseHandler.reply(ResponseStatus.ERROR, "Invalid patientId", request);
            return;
        }

        if (!patientRepository.existsById(id)) {
            responseHandler.reply(ResponseStatus.ERROR, "Patient does not exist", request);
            return;
        }

        List<Record> record;
        try {
            record = recordRepository.findByPatientId(id);
        } catch (Exception e) {
            responseHandler.reply(ResponseStatus.ERROR, "Record does not exist", request);
            return;
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, new TimestampSerializer())
                .create();

        // Reply with the record
        responseHandler.reply(ResponseStatus.SUCCESS, gson.toJson(record), request);
    }

    private void getByTimeslotId(JsonObject json, MqttMessage request) {
        // Get the recordId
        Long id = null;
        try {
            id = json.get("timeslotId").getAsLong();
        } catch (Exception e) {
            responseHandler.reply(ResponseStatus.ERROR, "Invalid timeslotId", request);
            return;
        }

        if (!timeslotRepository.existsById(id)) {
            responseHandler.reply(ResponseStatus.ERROR, "Timeslot does not exist", request);
            return;
        }

        // Find the record & check if it exists
        Record record;
        try {
            record = recordRepository.findByTimeslotId(id);
        } catch (Exception e) {
            responseHandler.reply(ResponseStatus.ERROR, "Record does not exist", request);
            return;
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Timestamp.class, new TimestampSerializer())
                .create();

        // Reply with the record
        responseHandler.reply(ResponseStatus.SUCCESS, gson.toJson(record), request);
    }
}
