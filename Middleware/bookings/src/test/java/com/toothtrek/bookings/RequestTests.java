package com.toothtrek.bookings;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.gson.JsonObject;
import com.toothtrek.bookings.entity.Booking;
import com.toothtrek.bookings.mqtt.MqttCallbackHandler;
import com.toothtrek.bookings.mqtt.MqttHandler;
import com.toothtrek.bookings.repository.BookingRepository;
import com.toothtrek.bookings.request.booking.BookingCreateRequestHandler;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RequestTests {

    @Autowired
    private MqttHandler mqttHandler;

    @Autowired
    private MqttCallbackHandler mqttCallbackHandler;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingCreateRequestHandler bookingCreateRequestHandler;

    private static MqttCallback callback = null;

    private static boolean replied = false;
    private static boolean arrived = false;
    private static MqttMessage response;

    @BeforeAll
    public void setup() {
        callback = new MqttCallback() {

            @Override
            public void disconnected(MqttDisconnectResponse disconnectResponse) {
            }

            @Override
            public void mqttErrorOccurred(MqttException exception) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                response = message;
                arrived = true;
            }

            @Override
            public void deliveryComplete(IMqttToken token) {
                replied = true;
            }

            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
            }

            @Override
            public void authPacketArrived(int reasonCode, MqttProperties properties) {
            }

        };

        mqttHandler.getClient().setCallback(callback);
    }

    @Test
    public void createRequest() {
        // Patient
        JsonObject jsonPatient = new JsonObject();
        jsonPatient.addProperty("id", "1234567890");
        jsonPatient.addProperty("name", "Patient");
        jsonPatient.addProperty("dateOfBirth", "2000-05-05");

        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.add("patient", jsonPatient);
        jsonMessage.addProperty("timeslotId", 1);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());
        MqttProperties properties = new MqttProperties();
        String responseTopic = "test/response/" + System.currentTimeMillis();
        properties.setResponseTopic(responseTopic);
        message.setProperties(properties);

        mqttHandler.subscribe(responseTopic);
        bookingCreateRequestHandler.handle(message);

        while (!replied || !arrived) {
            try {
                Thread.sleep(333);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Check if reply is success
        assert (response != null);
        System.out.println(new String(response.getPayload()));
        assert (new String(response.getPayload()).contains("success"));

        // Check if booking is created
        Booking booking = bookingRepository.findByTimeslotId(1).get(0);
        assert (booking != null);
        assert (booking.getPatientId().equals("1234567890"));
    }

    @AfterAll
    public void cleanup() {
        bookingRepository.deleteAll();
        mqttHandler.getClient().setCallback(mqttCallbackHandler);
    }
}
