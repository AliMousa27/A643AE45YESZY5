package com.toothtrek.Logs;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.toothtrek.Logs.entity.Log;
import com.toothtrek.Logs.mqtt.MqttCallbackHandler;
import com.toothtrek.Logs.mqtt.MqttHandler;
import com.toothtrek.Logs.repository.LogRepository;

@SpringBootTest
public class LogTests {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private MqttHandler mqttHandler;

    private static boolean messageArrived = false;

    private final static String TOPIC = "toothtrek/test/" + UUID.randomUUID().toString();
    private final static String PAYLOAD = "testing";

    private static MqttCallbackHandler mqttCallbackHandler = new MqttCallbackHandler() {
        @Autowired
        private MqttCallbackHandler defaultMqttCallbackHandler;

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            defaultMqttCallbackHandler.messageArrived(topic, message);

            // Wait for 1 second to make sure the message is saved to the database.
            Thread.sleep(1000);

            messageArrived = true;
        }
    };

    @Test
    void testLogging() {
        // MQTT connection and publish.
        mqttHandler.initialize(mqttCallbackHandler);
        mqttHandler.connect(false, false);
        mqttHandler.publish(TOPIC, PAYLOAD);

        // Wait until message arrived.
        waitUntilMessageArrived();

        Log log = logRepository.findAll().get(0);
        assert (log.getTopic().equals(TOPIC));
        assert (log.getPayload().equals(PAYLOAD));

        logRepository.delete(log);
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
