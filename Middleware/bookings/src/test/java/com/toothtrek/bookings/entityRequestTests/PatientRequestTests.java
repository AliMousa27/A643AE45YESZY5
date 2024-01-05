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
import com.toothtrek.bookings.entity.Patient;
import com.toothtrek.bookings.mqtt.MqttCallbackHandler;
import com.toothtrek.bookings.mqtt.MqttHandler;
import com.toothtrek.bookings.repository.PatientRepository;
import com.toothtrek.bookings.request.patient.PatientCreateRequestHandler;
import com.toothtrek.bookings.request.patient.PatientGetRequestHandler;
import com.toothtrek.bookings.request.patient.PatientUpdateRequestHandler;
import com.toothtrek.bookings.util.TestUtil;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PatientRequestTests {

    @Autowired
    private MqttHandler mqttHandler;

    // Repositories
    @Autowired
    private PatientRepository patientRepository;

    // Patient request handlers
    @Autowired
    private PatientCreateRequestHandler patientCreateRequestHandler;

    @Autowired
    private PatientGetRequestHandler patientGetRequestHandler;

    @Autowired
    private PatientUpdateRequestHandler patientUpdateRequestHandler;

    // Variables
    private static final String PATIENT_ID = "Test Patient ID";
    private static final String PATIENT_NAME = "Test Patient Name";
    private static final String PATIENT_EMAIL = "Test Email";
    private static final String UPDATED_PATIENT_NAME = "Updated Patient Name";

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
    }

    @Test
    @Order(1)
    public void invalidPatientCreateRequestTest() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("id", PATIENT_ID);
        // Invalid: missing name field
        // jsonMessage.addProperty("name", PATIENT_NAME);
        jsonMessage.addProperty("email", PATIENT_EMAIL);

        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());

        // Handle message
        mqttHandler.subscribe(responseTopic);
        patientCreateRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("error"));

        // Check that no patient was created
        assert (patientRepository.findAll().size() == 0);

    }

    @Test
    @Order(2)
    public void validPatientCreateRequestTest() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("id", PATIENT_ID);
        jsonMessage.addProperty("name", PATIENT_NAME);
        jsonMessage.addProperty("email", PATIENT_EMAIL);

        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());

        // Handle message
        mqttHandler.subscribe(responseTopic);
        patientCreateRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check that patient was created
        assert (patientRepository.findAll().size() == 1);

        // Check that patient was created with correct values
        Patient patient = patientRepository.findAll().get(0);
        assert (patient != null);
        assert (patient.getName().equals(PATIENT_NAME));
    }

    @Test
    @Order(3)
    public void patientGetRequestTest() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("id", PATIENT_ID);

        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());

        // Handle message
        mqttHandler.subscribe(responseTopic);
        patientGetRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check if name is correct
        assert (new String(response.getPayload()).contains(PATIENT_NAME));

        // Check if id is correct
        assert (new String(response.getPayload()).contains(PATIENT_ID));
    }

    @Test
    @Order(4)
    public void patientUpdateRequestTest() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("id", PATIENT_ID);
        jsonMessage.addProperty("name", UPDATED_PATIENT_NAME);
        jsonMessage.addProperty("email", PATIENT_EMAIL);

        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());

        // Handle message
        mqttHandler.subscribe(responseTopic);
        patientUpdateRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check that patient was updated with correct values
        Patient patient = patientRepository.findAll().get(0);
        assert (patient != null);
        assert (patient.getName().equals(UPDATED_PATIENT_NAME));
    }

    @AfterEach
    public void reset() {
        messageArrived = false;
        response = null;
    }

    @AfterAll
    public void cleanUp() {
        mqttHandler.disconnect();
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
