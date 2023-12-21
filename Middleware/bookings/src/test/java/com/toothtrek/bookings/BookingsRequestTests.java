package com.toothtrek.bookings;

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
import com.toothtrek.bookings.entity.Booking;
import com.toothtrek.bookings.entity.Patient;
import com.toothtrek.bookings.mqtt.MqttCallbackHandler;
import com.toothtrek.bookings.mqtt.MqttHandler;
import com.toothtrek.bookings.repository.BookingRepository;
import com.toothtrek.bookings.repository.PatientRepository;
import com.toothtrek.bookings.repository.TimeslotRepository;
import com.toothtrek.bookings.request.booking.BookingCreateRequestHandler;
import com.toothtrek.bookings.request.booking.BookingGetRequestHandler;
import com.toothtrek.bookings.request.booking.BookingStateRequestHandler;
import com.toothtrek.bookings.util.TestUtil;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingsRequestTests {

    @Autowired
    private MqttHandler mqttHandler;

    // Repositories

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TimeslotRepository timeslotRepository;

    // Booking request handlers

    @Autowired
    private BookingCreateRequestHandler bookingCreateRequestHandler;

    @Autowired
    private BookingStateRequestHandler bookingStateRequestHandler;

    @Autowired
    private BookingGetRequestHandler bookingGetRequestHandler;

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
                true, // Create timeslot
                false // Create booking
        );
    }

    @Test
    public void bookingCreateRequest() {
        // Timeslot ID
        Long timeslotId = timeslotRepository.findAll().get(0).getId();

        // Patient
        JsonObject jsonPatient = new JsonObject();
        Patient patient = patientRepository.findAll().get(0);
        jsonPatient.addProperty("id", patient.getId());
        jsonPatient.addProperty("name", patient.getName());
        jsonPatient.addProperty("email", patient.getEmail());

        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.add("patient", jsonPatient);
        jsonMessage.addProperty("timeslotId", timeslotId);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());
        MqttProperties properties = new MqttProperties();
        String responseTopic = "test/response/" + System.currentTimeMillis();
        properties.setResponseTopic(responseTopic);
        message.setProperties(properties);

        mqttHandler.subscribe(responseTopic);
        bookingCreateRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check if booking is created
        Booking booking = bookingRepository.findByTimeslotId(timeslotId).get(0);
        assert (booking != null);
        assert (booking.getPatient().getId().equals(patient.getId()));
    }

    @Test
    public void bookingGetRequest() {
        // JSON
        JsonObject jsonMessage = new JsonObject();
        String patientId = patientRepository.findAll().get(0).getId();
        jsonMessage.addProperty("patientId", patientId);

        // Create MQTT message
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());
        MqttProperties properties = new MqttProperties();
        String responseTopic = "test/response/" + System.currentTimeMillis();
        properties.setResponseTopic(responseTopic);
        message.setProperties(properties);

        mqttHandler.subscribe(responseTopic);
        bookingGetRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check if id is correct
        assert (new String(response.getPayload()).contains(patientId));
    }

    @Test
    public void bookingChangeState() {
        // JSON message
        String responseTopic = "test/response/" + System.currentTimeMillis();
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("bookingId", bookingRepository.findAll().get(0).getId());
        jsonMessage.addProperty("state", "confirmed");
        jsonMessage.addProperty("responseTopic", responseTopic);

        // MQTT message
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());
        MqttProperties properties = new MqttProperties();
        properties.setResponseTopic(responseTopic);
        message.setProperties(properties);

        mqttHandler.subscribe(responseTopic);
        bookingStateRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check if booking state is changed
        Booking booking = bookingRepository.findAll().get(0);
        assert (booking.getState().toString().equals("confirmed"));
    }

    @Test
    public void invalidBookingStateChange() {
        // JSON message
        String responseTopic = "test/response/" + System.currentTimeMillis();
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("id", bookingRepository.findAll().get(0).getId());
        jsonMessage.addProperty("state", "confirmed"); // Invalid state: Cannot change from confirmed to confirmed
        jsonMessage.addProperty("responseTopic", responseTopic);

        // MQTT message
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());
        MqttProperties properties = new MqttProperties();
        properties.setResponseTopic(responseTopic);
        message.setProperties(properties);

        mqttHandler.subscribe(responseTopic);
        bookingStateRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("error"));
    }

    @Test
    private void cancelBooking() {
        // JSON message
        String responseTopic = "test/response/" + System.currentTimeMillis();
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("id", bookingRepository.findAll().get(0).getId());
        jsonMessage.addProperty("patientId", patientRepository.findAll().get(0).getId());
        jsonMessage.addProperty("state", "cancelled");
        jsonMessage.addProperty("responseTopic", responseTopic);

        // MQTT message
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());
        MqttProperties properties = new MqttProperties();
        properties.setResponseTopic(responseTopic);
        message.setProperties(properties);

        mqttHandler.subscribe(responseTopic);
        bookingStateRequestHandler.handle(message);

        waitUntilMessageArrived();

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check if booking state is changed
        Booking booking = bookingRepository.findAll().get(0);
        assert (booking.getState().toString().equals("cancelled"));
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
