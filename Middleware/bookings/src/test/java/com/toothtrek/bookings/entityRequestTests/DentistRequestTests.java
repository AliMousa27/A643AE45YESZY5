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
import com.toothtrek.bookings.entity.Dentist;
import com.toothtrek.bookings.mqtt.MqttCallbackHandler;
import com.toothtrek.bookings.mqtt.MqttHandler;
import com.toothtrek.bookings.repository.DentistRepository;
import com.toothtrek.bookings.request.dentist.DentistCreateRequestHandler;
import com.toothtrek.bookings.request.dentist.DentistGetRequestHandler;
import com.toothtrek.bookings.request.dentist.DentistUpdateRequestHandler;
import com.toothtrek.bookings.request.dentist.DentistDeleteRequestHandler;
import com.toothtrek.bookings.util.TestUtil;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DentistRequestTests {

    @Autowired
    private MqttHandler mqttHandler;

    // Repositories
    @Autowired
    private DentistRepository dentistRepository;

    // Dentist request handlers
    @Autowired
    private DentistCreateRequestHandler dentistCreateRequestHandler;

    @Autowired
    private DentistGetRequestHandler dentistGetRequestHandler;

    @Autowired
    private DentistUpdateRequestHandler dentistUpdateRequestHandler;

    @Autowired
    private DentistDeleteRequestHandler dentistDeleteRequestHandler;

    // Test Util
    @Autowired
    private TestUtil testUtil;

    // Variables
    private static final String DENTIST_NAME = "Test Dentist";
    private static final String DENTIST_DOB = "1990-01-01 00:00";
    private static final String UPDATED_DENTIST_NAME = "Updated Dentist Name";

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
    public void invalidDentistCreateRequest() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("name", DENTIST_NAME);
        // Invalid: missing date of birth field
        // jsonMessage.addProperty("dateOfBirth", DENTIST_DOB);

        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());

        // Handle message
        mqttHandler.subscribe(responseTopic);
        dentistCreateRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("error"));

        // Check that no dentist was created
        assert (dentistRepository.findAll().size() == 0);

    }

    @Test
    @Order(2)
    public void validDentistCreateRequest() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("name", DENTIST_NAME);
        jsonMessage.addProperty("dateOfBirth", DENTIST_DOB);

        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());

        // Handle message
        mqttHandler.subscribe(responseTopic);
        dentistCreateRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check that dentist was created
        assert (dentistRepository.findAll().size() == 1);

        Dentist dentist = dentistRepository.findAll().get(0);
        assert (dentist != null);
        assert (dentist.getName().equals(DENTIST_NAME));
    }

    @Test
    @Order(3)
    public void dentistGetRequest() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        Long dentistId = dentistRepository.findAll().get(0).getId();
        jsonMessage.addProperty("id", dentistId);

        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());

        // Handle message
        mqttHandler.subscribe(responseTopic);
        dentistGetRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check if name is correct
        assert (new String(response.getPayload()).contains(DENTIST_NAME));

        // Check if id is correct
        assert (new String(response.getPayload()).contains(dentistId.toString()));

    }

    @Test
    @Order(4)
    public void dentistUpdateRequest() {
        // Variables
        Long dentistId = dentistRepository.findAll().get(0).getId();

        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("id", dentistId);
        jsonMessage.addProperty("name", UPDATED_DENTIST_NAME);
        jsonMessage.addProperty("dateOfBirth", DENTIST_DOB);

        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());

        // Handle message
        mqttHandler.subscribe(responseTopic);
        dentistUpdateRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check if dentist is updated
        Dentist dentist = dentistRepository.findAll().get(0);
        assert (dentist != null);
        assert (dentist.getName().equals(UPDATED_DENTIST_NAME));
        assert (dentist.getDateOfBirth().toString().equals(DENTIST_DOB));
    }

    @Test
    @Order(5)
    public void dentistDeleteRequest() {
        // Variables
        Long dentistId = dentistRepository.findAll().get(0).getId();

        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("id", dentistId);

        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());

        // Handle message
        mqttHandler.subscribe(responseTopic);
        dentistDeleteRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check that dentist was deleted and no longer exists
        assert (dentistRepository.findAll().size() == 0);
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
