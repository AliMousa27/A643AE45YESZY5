package com.toothtrek.dentalRecord.request.record;

import java.sql.Timestamp;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.toothtrek.dentalRecord.repository.RecordRepository;
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
    private ResponseHandler responseHandler;

    @Override
    public void handle(MqttMessage request) {
        try {
            JsonObject json;
            try {
                json = new Gson().fromJson(new String(request.getPayload()), JsonObject.class);
            } catch (JsonSyntaxException e) {
                responseHandler.reply(ResponseStatus.ERROR, "Invalid JSON", request);
                return;
            }

            // Get the record ID from the JSON
            Long id = json.get("id").getAsLong();

            // Find the record & check if it exists
            Record record;
            try {
                record = recordRepository.findById(id).get();
            } catch (Exception e) {
                responseHandler.reply(ResponseStatus.ERROR, "Record does not exist", request);
                return;
            }

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Timestamp.class, new TimestampSerializer())
                    .create();
            // Reply with the record
            responseHandler.reply(ResponseStatus.SUCCESS, gson.toJson(record), request);

        } catch (JsonSyntaxException e) {
            // Handle the exception
            e.printStackTrace();
        }
    }
}
