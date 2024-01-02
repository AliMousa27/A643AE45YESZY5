package com.toothtrek.dentalRecord;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.gson.JsonObject;

import com.toothtrek.dentalRecord.entity.Booking;
import com.toothtrek.dentalRecord.entity.Record;
import com.toothtrek.dentalRecord.mqtt.MqttCallbackHandler;
import com.toothtrek.dentalRecord.mqtt.MqttHandler;
import com.toothtrek.dentalRecord.repository.BookingRepository;
import com.toothtrek.dentalRecord.repository.RecordRepository;
import com.toothtrek.dentalRecord.repository.TimeslotRepository;
import com.toothtrek.dentalRecord.request.record.RecordCreateRequestHandler;
import com.toothtrek.dentalRecord.request.record.RecordGetRequestHandler;
import com.toothtrek.dentalRecord.request.record.RecordUpdateRequestHandler;
import com.toothtrek.dentalRecord.util.TestDatabaseUtil;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecordTests {

    // Repositories

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private TimeslotRepository timeslotRepository;

    // Request Handlers

    @Autowired
    private RecordCreateRequestHandler recordCreateRequestHandler;

    @Autowired
    private RecordGetRequestHandler recordGetRequestHandler;

    @Autowired
    private RecordUpdateRequestHandler recordUpdateRequestHandler;

    // Test Util

    @Autowired
    TestDatabaseUtil testDatabaseUtil;

    // MQTT

    @Autowired
    private MqttHandler mqttHandler;

    private static boolean messageArrived = false;
    private static MqttMessage response;

    // Customized MQTT callback handler for testing.
    private static MqttCallbackHandler mqttCallbackHandler = new MqttCallbackHandler() {

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            response = message;
            messageArrived = true;
        }
    };

    // Variables

    private static final String NOTE = "this is a note";
    private static final String UPDATED_NOTE = "this is an updated note";

    @BeforeAll
    void setup() throws Exception {
        mqttHandler.initialize(mqttCallbackHandler);
        mqttHandler.connect(false, false);

        testDatabaseUtil.createDummyData(true, true, true, true, true);
    }

    @Test
    @Order(1)
    void invalidCreateRecord() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("timeslotId", timeslotRepository.findAll().get(0).getId());
        jsonMessage.addProperty("notes", "this is a note");

        // Create message
        MqttMessage message = createMqttMessage(jsonMessage);
        mqttHandler.subscribe(message.getProperties().getResponseTopic());

        // Handle message
        recordCreateRequestHandler.handle(message);
        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);

        // Timeslot is not "completed", cannot create record
        assert (new String(response.getPayload()).contains("error"));
    }

    @Test
    @Order(2)
    void createRecord() {
        // Set booking to "completed"
        Booking booking = bookingRepository.findAll().get(0);
        booking.setState(Booking.State.completed);
        bookingRepository.save(booking);
        bookingRepository.flush();

        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("timeslotId", timeslotRepository.findAll().get(0).getId());
        jsonMessage.addProperty("notes", NOTE);

        // Create message
        MqttMessage message = createMqttMessage(jsonMessage);
        mqttHandler.subscribe(message.getProperties().getResponseTopic());

        // Handle message
        recordCreateRequestHandler.handle(message);
        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));
    }

    @Test
    @Order(3)
    void getRecord() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("timeslotId", timeslotRepository.findAll().get(0).getId());

        // Create message
        MqttMessage message = createMqttMessage(jsonMessage);
        mqttHandler.subscribe(message.getProperties().getResponseTopic());

        // Handle message
        recordGetRequestHandler.handle(message);
        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));
    }

    @Test
    @Order(4)
    void updateRecord() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("id", recordRepository.findAll().get(0).getId());
        jsonMessage.addProperty("notes", UPDATED_NOTE);

        // Create message
        MqttMessage message = createMqttMessage(jsonMessage);
        mqttHandler.subscribe(message.getProperties().getResponseTopic());

        // Handle message
        recordUpdateRequestHandler.handle(message);
        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        Record record = recordRepository.findAll().get(0);
        assert (record.getNotes().equals(UPDATED_NOTE));
    }

    @AfterEach
    public void reset() {
        messageArrived = false;
        response = null;
    }

    @AfterAll
    public void cleanUp() {
        mqttHandler.disconnect();
        testDatabaseUtil.deleteAll();
    }

    private MqttMessage createMqttMessage(JsonObject jsonMessage) {
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());
        MqttProperties properties = new MqttProperties();
        String responseTopic = "test/response/" + System.currentTimeMillis();
        properties.setResponseTopic(responseTopic);
        message.setProperties(properties);
        return message;
    }

    private void waitUntilMessageArrived() {
        for (int i = 0; i < 10; i++) {
            if (!messageArrived) {
                try {
                    Thread.sleep(333);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
    }

}
