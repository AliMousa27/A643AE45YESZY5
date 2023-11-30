package com.toothtrek.template;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.toothtrek.template.mqtt.MqttCallbackHandler;
import com.toothtrek.template.mqtt.MqttHandler;

@SpringBootApplication
@EntityScan("com.toothtrek.template.entity")
public class TemplateApplication implements CommandLineRunner {

	/*
	 * How to import the repository:
	 * 
	 * @Autowired
	 * private TemplateEntityRepository templateEntityRepository;
	 */

	// Necessary to decouple the MQTT handler from the MQTT callback handler.
	// To avoid circular dependencies.
	@Autowired
	private MqttHandler mqttHandler;

	@Autowired
	private MqttCallbackHandler mqttCallbackHandler;

	public static void main(String[] args) {
		SpringApplication.run(TemplateApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Initialize MQTT
		mqttHandler.initialize(mqttCallbackHandler);
		mqttHandler.connect(true, true);
		mqttHandler.subscribe("toothtrek/booking_service/+/+/", 1);

		/*
		 * Example MQTT publish:
		 * 
		 * MqttProperties properties = new MqttProperties();
		 * properties.setResponseTopic("<response topic>");
		 * 
		 * MqttMessage message = new MqttMessage();
		 * message.setProperties(properties);
		 * 
		 * message.setPayload("<payload>".getBytes());
		 * 
		 * mqttHandler.getClientId());
		 * mqttHandler.publish("<topic>", message);
		 * 
		 * -----------------------------------------------------------------------
		 * 
		 * Example of how to use CRUD with the repository:
		 * 
		 * TemplateEntity template = new TemplateEntity("name", 1);
		 * templateEntityRepository.save(template);
		 * 
		 */

	}

}
