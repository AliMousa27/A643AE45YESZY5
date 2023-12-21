package com.toothtrek.dentalRecord.request.record;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.sql.Timestamp;
import java.util.Optional;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.toothtrek.dentalRecord.entity.Record;
import com.toothtrek.dentalRecord.repository.RecordRepository;
import com.toothtrek.dentalRecord.request.RequestHandlerInterface;
import com.toothtrek.dentalRecord.response.ResponseHandler;
import com.toothtrek.dentalRecord.response.ResponseStatus;
import com.toothtrek.dentalRecord.serializer.json.TimestampSerializer;

@Configuration
public class RecordUpdateRequestHandler implements RequestHandlerInterface {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private ResponseHandler responseHandler;

    private final String[] MESSAGE_PROPERTIES = { "id", "notes" };

    @Override
    public void handle(MqttMessage request) {
        try {
            // Create json object from payload
            JsonObject json;
            try {
                json = new Gson().fromJson(new String(request.getPayload()), JsonObject.class);
            } catch (JsonSyntaxException e) {
                responseHandler.reply(ResponseStatus.ERROR, "Invalid JSON", request);
                return;
            }

            System.out.println(json);

            // Check if all required properties are present
            if (checkMissingJSONProperties(json, MESSAGE_PROPERTIES, request)) {
                return;
            }

            // Get the record ID and new notes from the JSON
            Long id = 0L;
            String newNotes = "";
            try {
                id = json.get("id").getAsLong();
                newNotes = json.get("notes").getAsString();
            } catch (Exception e) {
                responseHandler.reply(ResponseStatus.ERROR, "Invalid JSON", request);
            }

            // Find the record
            Optional<Record> recordOptional = recordRepository.findById(id);

            // Check if the record exists
            if (!recordOptional.isPresent()) {
                responseHandler.reply(ResponseStatus.ERROR, "Record with id " + id + " does not exist", request);
                return;
            }

            // Get the record from the Optional
            Record record = recordOptional.get();

            // Update the notes
            record.setNotes(newNotes);

            // Save the updated record to the database
            recordRepository.save(record);

            // Create a Gson object with a custom Timestamp serializer
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Timestamp.class, new TimestampSerializer())
                    .create();

            // Reply with the updated record
            responseHandler.reply(ResponseStatus.SUCCESS, gson.toJson(record), request);

        } catch (JsonSyntaxException e) {
            // Handle the exception
            e.printStackTrace();
        }
    }

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