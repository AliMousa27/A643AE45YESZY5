package com.toothtrek.notifications;

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
import com.toothtrek.notifications.entity.Notification;
import com.toothtrek.notifications.mqtt.MqttCallbackHandler;
import com.toothtrek.notifications.mqtt.MqttHandler;
import com.toothtrek.notifications.repository.NotificationRepository;
import com.toothtrek.notifications.request.notification.NotificationCreateRequestHandler;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RequestTests {

    @Autowired
    private MqttHandler mqttHandler;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationCreateRequestHandler notificationCreateRequestHandler;

    private static boolean arrived = false;
    private static MqttMessage response;

    // Customized MQTT callback handler for testing.
    private static MqttCallbackHandler mqttCallbackHandler = new MqttCallbackHandler() {

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            response = message;
            arrived = true;
        }
    };

    @BeforeAll
    public void setup() {
        mqttHandler.initialize(mqttCallbackHandler);
        mqttHandler.connect(false, false);
    }


    @Test
    public void createRequest() {
        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("title", "Your appointment is coming up!");
        jsonMessage.addProperty("message", "Your appointment is coming up tomorrow.");
        jsonMessage.addProperty("email", "this@that.com");
        jsonMessage.addProperty("time", "2021-05-05T00:00:00.000+00:00");
        jsonMessage.addProperty("booking_id", "1234567890");
        jsonMessage.addProperty("scheduled", "true");

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());
        MqttProperties properties = new MqttProperties();
        String responseTopic = "test/response/" + System.currentTimeMillis();
        properties.setResponseTopic(responseTopic);
        message.setProperties(properties);

        mqttHandler.subscribe(responseTopic);
        notificationCreateRequestHandler.handle(message);

        for (int i = 0; i < 10; i++) {
            if (!arrived) {
                try {
                    Thread.sleep(333);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }


        // Check if reply is success
        assert (response != null);
        System.out.println(new String(response.getPayload()));
        assert (new String(response.getPayload()).contains("success"));

        // Check if notification is created
        Notification notification = notificationRepository.findByBookingId("1234567890").get(0);
        assert (notification != null);
        assert (notification.getEmail().equals("this@that.com"));
    }

    @AfterEach
    public void reset() {
        arrived = false;
        response = null;
    }

    @AfterAll
    public void cleanUp() {
        mqttHandler.disconnect();

        notificationRepository.deleteAll();
    }

}
