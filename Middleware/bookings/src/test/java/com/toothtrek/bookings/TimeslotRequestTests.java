package com.toothtrek.bookings;

import java.text.SimpleDateFormat;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.gson.JsonObject;
import com.toothtrek.bookings.entity.Timeslot;
import com.toothtrek.bookings.mqtt.MqttCallbackHandler;
import com.toothtrek.bookings.mqtt.MqttHandler;
import com.toothtrek.bookings.repository.DentistRepository;
import com.toothtrek.bookings.repository.OfficeRepository;
import com.toothtrek.bookings.repository.TimeslotRepository;
import com.toothtrek.bookings.request.timeslot.TimeslotCancelRequestHandler;
import com.toothtrek.bookings.request.timeslot.TimeslotCreateRequestHandler;
import com.toothtrek.bookings.request.timeslot.TimeslotGetRequestHandler;
import com.toothtrek.bookings.util.TestUtil;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TimeslotRequestTests {

    @Autowired
    private MqttHandler mqttHandler;

    // Repositories

    @Autowired
    private TimeslotRepository timeslotRepository;

    @Autowired
    private OfficeRepository officeRepo;

    @Autowired
    private DentistRepository dentistRepo;

    // Timeslot request handlers

    @Autowired
    private TimeslotCreateRequestHandler timeslotCreateRequestHandler;

    @Autowired
    private TimeslotGetRequestHandler timeslotGetRequestHandler;

    @Autowired
    private TimeslotCancelRequestHandler timeslotCancelRequestHandler;

    // Test Util

    @Autowired
    private TestUtil testUtil;

    // MQTT callback variables

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

    @BeforeAll
    public void setup() throws Exception {
        mqttHandler.initialize(mqttCallbackHandler);
        mqttHandler.connect(false, false);

        testUtil.createDummyData(
                true, // Create patient
                true, // Create dentist
                true, // Create office
                false, // Create timeslot
                false // Create booking
        );
    }

    @Test
    public void timeslotCreateRequest() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("dentistId", dentistRepo.findAll().get(0).getId());
        jsonMessage.addProperty("officeId", officeRepo.findAll().get(0).getId());
        jsonMessage.addProperty("dateAndTime", "2025-12-06 18:40");

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());
        MqttProperties properties = new MqttProperties();
        String responseTopic = "test/response/" + System.currentTimeMillis();
        properties.setResponseTopic(responseTopic);
        message.setProperties(properties);

        mqttHandler.subscribe(responseTopic);
        timeslotCreateRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);

        // response to JSON
        Timeslot timeslot = timeslotRepository.findAll().get(0);
        assert (timeslot.getDentist().getId() == jsonMessage.get("dentistId").getAsLong());
        assert (timeslot.getOffice().getId() == jsonMessage.get("officeId").getAsLong());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        assert (sdf.format(timeslot.getDateAndTime()).equals(jsonMessage.get("dateAndTime").getAsString()));
    }

    @Test
    public void timeslotGetRequest() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());
        MqttProperties properties = new MqttProperties();
        properties.setResponseTopic(responseTopic);
        message.setProperties(properties);

        mqttHandler.subscribe(responseTopic);
        timeslotGetRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));
    }

    @Test
    private void cancelTimeslot() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("timeslotId", timeslotRepository.findAll().get(0).getId());
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());
        MqttProperties properties = new MqttProperties();
        properties.setResponseTopic(responseTopic);
        message.setProperties(properties);

        mqttHandler.subscribe(responseTopic);
        timeslotCancelRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check if timeslot is cancelled
        Timeslot timeslot = timeslotRepository.findAll().get(0);
        assert (timeslot.getState() == Timeslot.State.cancelled);
    }

    @AfterEach
    public void reset() {
        messageArrived = false;
        response = null;
    }

    @AfterAll
    public void cleanUp() {
        mqttHandler.disconnect();
        testUtil.deleteAll();
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
