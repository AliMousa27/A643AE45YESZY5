package com.toothtrek;

import org.junit.jupiter.api.Test;

import com.middleware.Logs.mqtt.MqttHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

/*
        All used handler function calls are asynchronous and awaited for.
        Therefore, no timeout is needed between calls.

        ! IMPORTANT ! 
        If test broker from mosquitto is down then tests will fail
*/
public class TestMqtt {
    static MqttHandler handler;
    static boolean messageArrived = false;
    static boolean deliveryComplete = false;

    @BeforeAll
    public static void setup() {
        String broker = "tcp://test.mosquitto.org:1883";
        String clientId = "testclient-" + UUID.randomUUID().toString().replace("-", "");
        int qos = 1;
        handler = new MqttHandler(broker, clientId, qos);
    }

    @AfterEach
    public void reset() {
        handler.disconnect();
    }

    @Test
    public void testConnect() {
        handler.connect(true, false);
        assertEquals(true, handler.isConnected());
    }

    @Test
    public void testPublishAndSubscribe() {
        String topic = "a/b/c";
        String content = "lorem ipsum";
        handler.connect(true, false);
        handler.getClient().setCallback(new MqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                messageArrived = true;
            }

            @Override
            public void deliveryComplete(IMqttToken token) {
                deliveryComplete = true;
            }

            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
            }

            @Override
            public void authPacketArrived(int reasonCode, MqttProperties properties) {
            }

            @Override
            public void disconnected(MqttDisconnectResponse disconnectResponse) {
            }

            @Override
            public void mqttErrorOccurred(MqttException exception) {
            }
        });
        handler.subscribe(topic);
        handler.publish(topic, content);

        assertEquals(true, messageArrived);
        assertEquals(true, deliveryComplete);
    }
}
