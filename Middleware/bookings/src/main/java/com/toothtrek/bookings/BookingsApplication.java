package com.toothtrek.bookings;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;

import com.toothtrek.bookings.mqtt.MqttCallbackHandler;
import com.toothtrek.bookings.mqtt.MqttHandler;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

@SpringBootApplication
@EntityScan("com.toothtrek.bookings.entity")
public class BookingsApplication implements CommandLineRunner {

	// Necessary to decouple the MQTT handler from the MQTT callback handler.
	// To avoid circular dependencies.
	@Autowired
	private MqttHandler mqttHandler;

	@Autowired
	private MqttCallbackHandler mqttCallbackHandler;

	public static void main(String[] args) {
		try {
			// Set environment variables from .env file
			Dotenv.configure().load().entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
		} catch (DotenvException exception) {
			System.out.println(exception.getMessage());
			System.exit(1);
		}

		// Check if required environment variables are set
		List<String> requiredEnvVars = Arrays.asList(
				"DB_HOST",
				"DB_PORT",
				"DB_NAME",
				"DB_USERNAME",
				"DB_PASSWORD",
				"MQTT_BROKER",
				"MQTT_QOS",
				"MQTT_CLIENT_ID");

		requiredEnvVars.forEach(env -> {
			if (System.getProperty(env) == null) {
				System.out.println("Required environment variable not set: " + env);
				System.exit(1);
			} else {
				System.out.println(env + ": " + System.getProperty(env));
			}
		});

		SpringApplication app = new SpringApplication(BookingsApplication.class);
		ConfigurableApplicationContext context = app.run(args);

		ExecutorService executorService = context.getBean(ExecutorService.class);
		MqttHandler mqttHandler = context.getBean(MqttHandler.class);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("\nShutting down executor service...");
			executorService.shutdown();
			try {
				if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
					executorService.shutdownNow();
					if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
						System.err.println("\n!! Executor service did not terminate !!");
					}
				}
			} catch (InterruptedException ie) {
				System.out.println("\n!! Executor service interrupted !!");
				executorService.shutdownNow();
				Thread.currentThread().interrupt();
			}

			mqttHandler.disconnect();

			System.out.println("\nShutdown complete");
		}));
	}

	@Override
	public void run(String... args) throws Exception {

		// Initialize MQTT
		mqttHandler.initialize(mqttCallbackHandler);
		mqttHandler.connect(true, true);
		mqttHandler.subscribe("toothtrek/booking_service/+/+/");

	}

}
