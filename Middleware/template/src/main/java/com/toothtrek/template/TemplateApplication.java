package com.toothtrek.template;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.toothtrek.template.mqtt.MqttHandler;

@SpringBootApplication
@EntityScan("com.toothtrek.template.entity")
public class TemplateApplication implements CommandLineRunner {

	// How to import the repository:
	//
	// @Autowired
	// private TemplateEntityRepository templateEntityRepository;

	private MqttHandler mqttHandler;

	public static void main(String[] args) {
		SpringApplication.run(TemplateApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Check required arguments
		if (args.length != 3) {
			System.out.println(
					"\n   Usage: java -jar <jar-file> <broker-address> <specified client-id or random> <qos> \n");
			System.exit(1);
		}

		String broker = args[0];
		if (args[1].equals("random")) {
			args[1] = UUID.randomUUID().toString().replace("-", "");
		}

		String clientId = args[1];
		int qos = Integer.parseInt(args[2]);
		mqttHandler = new MqttHandler(broker, clientId, qos);
		this.mqttHandler.connect(true, true);

		// Example of how to use CRUD with the repository:
		//
		// TemplateEntity template = new TemplateEntity("name", 1);
		// templateEntityRepository.save(template);

	}

	public MqttHandler getMqttHandler() {
		return mqttHandler;
	}

}
