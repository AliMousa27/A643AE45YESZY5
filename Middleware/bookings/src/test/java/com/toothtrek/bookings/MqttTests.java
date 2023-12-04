package com.toothtrek.bookings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

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

import com.toothtrek.bookings.mqtt.MqttCallbackHandler;
import com.toothtrek.bookings.mqtt.MqttHandler;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MqttTests {

    @Autowired
    private MqttHandler mqttHandler;

    @Autowired
    private MqttCallbackHandler mqttCallbackHandler;

    private static MqttCallback callback = null;

    // Flags to check if message arrived and delivered
    private static boolean messageArrived = false;
    private static boolean messageDelivered = false;

    // MQTT topic and payload content for testing
    private static String topic = "toothtrek/test/" + UUID.randomUUID().toString().replace("-", "");
    private static String content = "lorem ipsum";

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
                messageArrived = true;
            }

            @Override
            public void deliveryComplete(IMqttToken token) {
                messageDelivered = true;
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
    void mqttConnection() {
        assert (mqttHandler.isConnected());
    }

    @Test
    void mqttPubAndSub() {
        // Will automatically wait for completion.
        // See IMqttToken.waitForCompletion()
        mqttHandler.subscribe(topic);
        mqttHandler.publish(topic, content);

        // Guarantee that the callback has sufficient time to execute.
        // To avoid race conditions.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(true, messageArrived);
        assertEquals(true, messageDelivered);
    }

    @AfterAll
    public void cleanup() {
        mqttHandler.unsubscribe(topic);
        mqttHandler.getClient().setCallback(mqttCallbackHandler);
    }
}
