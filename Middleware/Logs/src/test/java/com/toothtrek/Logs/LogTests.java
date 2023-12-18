package com.toothtrek.Logs;

import java.util.UUID;

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

    @Autowired
    private MqttCallbackHandler mqttCallbackHandler;

    private final static String TOPIC = "toothtrek/test/" + UUID.randomUUID().toString();
    private final static String PAYLOAD = "testing";

    @Test
    void testLogging() {
        // MQTT connection and publish.
        mqttHandler.initialize(mqttCallbackHandler);
        mqttHandler.connect(false, false);
        mqttHandler.publish(TOPIC, PAYLOAD);

        Log log = logRepository.findAll().get(0);
        assert (log.getTopic().equals(TOPIC));
        assert (log.getPayload().equals(PAYLOAD));

        logRepository.delete(log);
    }
}
