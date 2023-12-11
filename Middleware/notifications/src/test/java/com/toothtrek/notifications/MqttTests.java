package com.toothtrek.notifications;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.toothtrek.notifications.mqtt.MqttCallbackHandler;
import com.toothtrek.notifications.mqtt.MqttHandler;

@SpringBootTest
public class MqttTests {

    @Autowired
    private MqttHandler mqttHandler;

    // Flags to check if message arrived and delivered.
    private static boolean messageArrived = false;
    private static boolean messageDelivered = false;

    // MQTT topic and payload content for testing.
    private static String topic = "toothtrek/test/" + UUID.randomUUID().toString();
    private static String content = "lorem ipsum";

    // Customized MQTT callback handler for testing.
    private static MqttCallbackHandler mqttCallbackHandler = new MqttCallbackHandler() {

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            messageArrived = true;
        }

        @Override
        public void deliveryComplete(IMqttToken token) {
            messageDelivered = true;
        }
    };

    @Test
    void testMqttConnectionAndPubSub() {
        mqttHandler.initialize(mqttCallbackHandler);
        mqttHandler.connect(false, false);

        assert (mqttHandler.isConnected());

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

        // Cleanup
        mqttHandler.unsubscribe(topic);
        mqttHandler.disconnect();
    }
}
