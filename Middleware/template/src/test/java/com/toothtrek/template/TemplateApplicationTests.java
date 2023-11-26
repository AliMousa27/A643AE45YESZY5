package com.toothtrek.template;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// Set args to connect to MQTT
@SpringBootTest(args = { "tcp://test.mosquitto.org:1883", "random", "1" })
class TemplateApplicationTests {

	static boolean messageArrived = false;
	static boolean deliveryComplete = false;

	@Autowired
	private TemplateApplication app;

	@Test
	void contextLoads() {
	}

	@Test
	void testMqttConnection() {
		assert (app.getMqttHandler().isConnected());
	}

	@Test
	void testMqttPubAndSub() {
		// Can publish and subscribe
		String topic = "toothtrek/test/" + UUID.randomUUID().toString().replace("-", "");;
		String content = "lorem ipsum";
		app.getMqttHandler().getClient().setCallback(new MqttCallback() {
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
		app.getMqttHandler().subscribe(topic);
		app.getMqttHandler().publish(topic, content);

		assertEquals(true, messageArrived);
		assertEquals(true, deliveryComplete);
	}

}
