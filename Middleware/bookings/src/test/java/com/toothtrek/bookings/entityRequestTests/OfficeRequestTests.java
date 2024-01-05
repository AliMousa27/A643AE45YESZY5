package com.toothtrek.bookings.entityRequestTests;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.gson.JsonObject;
import com.toothtrek.bookings.entity.Office;
import com.toothtrek.bookings.mqtt.MqttCallbackHandler;
import com.toothtrek.bookings.mqtt.MqttHandler;
import com.toothtrek.bookings.repository.OfficeRepository;
import com.toothtrek.bookings.request.office.OfficeCreateRequestHandler;
import com.toothtrek.bookings.request.office.OfficeGetRequestHandler;
import com.toothtrek.bookings.request.office.OfficeUpdateRequestHandler;
import com.toothtrek.bookings.request.office.OfficeDeleteRequestHandler;
import com.toothtrek.bookings.util.TestUtil;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OfficeRequestTests {

    @Autowired
    private MqttHandler mqttHandler;

    // Repositories
    @Autowired
    private OfficeRepository officeRepository;

    // Office request handlers
    @Autowired
    private OfficeCreateRequestHandler officeCreateRequestHandler;
    @Autowired
    private OfficeGetRequestHandler officeGetRequestHandler;
    @Autowired
    private OfficeUpdateRequestHandler officeUpdateRequestHandler;
    @Autowired
    private OfficeDeleteRequestHandler officeDeleteRequestHandler;

    // Test Util
    @Autowired
    private TestUtil testUtil;

    // Variables
    private static final String OFFICE_NAME = "Test Office";
    private static final String UPDATED_OFFICE_NAME = "Updated Office Name";
    private static final String UPDATED_OFFICE_ADDRESS = "Updated Office Address";

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
                false, // Create patient
                false, // Create dentist
                false, // Create office
                false, // Create timeslot
                false // Create booking
        );
    }

    @Test
    @Order(1)
    public void invalidOfficeCreateRequest() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("name", "Test Office");
        // Invalid: missing address field
        // jsonMessage.addProperty("address", "Test Address");
        jsonMessage.addProperty("latitude", 57.7066159f);
        jsonMessage.addProperty("longitude", 11.934085f);

        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());

        // Handle message
        mqttHandler.subscribe(responseTopic);
        officeCreateRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("error"));

        // Check that no office was created
        assert (officeRepository.findAll().size() == 0);
    }

    @Test
    @Order(2)
    public void officeCreateRequest() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("name", OFFICE_NAME);
        jsonMessage.addProperty("address", "Test Address");
        jsonMessage.addProperty("latitude", 57.7066159f);
        jsonMessage.addProperty("longitude", 11.934085f);

        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());

        // Handle message
        mqttHandler.subscribe(responseTopic);
        officeCreateRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check if office is created
        assert (officeRepository.findAll().size() == 1);

        Office office = officeRepository.findAll().get(0);
        assert (office != null);
        assert (office.getName().equals(OFFICE_NAME));
    }

    @Test
    @Order(3)
    public void officeGetRequest() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        Long officeId = officeRepository.findAll().get(0).getId();
        jsonMessage.addProperty("id", officeId);

        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());

        // Handle message
        mqttHandler.subscribe(responseTopic);
        officeGetRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check if name is correct
        assert (new String(response.getPayload()).contains(OFFICE_NAME));

        // Check if id is correct
        assert (new String(response.getPayload()).contains(officeId.toString()));
    }

    @Test
    @Order(4)
    public void officeUpdateRequest() {
        // Variables
        Long officeId = officeRepository.findAll().get(0).getId();

        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("id", officeId);
        jsonMessage.addProperty("name", UPDATED_OFFICE_NAME);
        jsonMessage.addProperty("address", UPDATED_OFFICE_ADDRESS);
        jsonMessage.addProperty("latitude", 57.7066159f);
        jsonMessage.addProperty("longitude", 11.934085f);

        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());

        // Handle message
        mqttHandler.subscribe(responseTopic);
        officeUpdateRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check if office is updated
        Office office = officeRepository.findAll().get(0);
        assert (office != null);
        assert (office.getName().equals(UPDATED_OFFICE_NAME));
        assert (office.getAddress().equals(UPDATED_OFFICE_ADDRESS));
    }

    @Test
    @Order(5)
    public void officeDeleteRequest() {
        // Variables
        Long officeId = officeRepository.findAll().get(0).getId();

        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("id", officeId);

        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());

        // Handle message
        mqttHandler.subscribe(responseTopic);
        officeDeleteRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check that office was deleted and no longer exists
        assert (officeRepository.findAll().size() == 0);
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