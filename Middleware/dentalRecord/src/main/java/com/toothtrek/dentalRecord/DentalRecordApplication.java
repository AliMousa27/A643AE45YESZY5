package com.toothtrek.dentalRecord;

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

import com.toothtrek.dentalRecord.mqtt.MqttCallbackHandler;
import com.toothtrek.dentalRecord.mqtt.MqttHandler;
import com.toothtrek.dentalRecord.repository.BookingRepository;
import com.toothtrek.dentalRecord.repository.DentistRepository;
import com.toothtrek.dentalRecord.repository.OfficeRepository;
import com.toothtrek.dentalRecord.repository.PatientRepository;
import com.toothtrek.dentalRecord.repository.TimeslotRepository;
import com.toothtrek.dentalRecord.entity.Booking;
import com.toothtrek.dentalRecord.entity.Dentist;
import com.toothtrek.dentalRecord.entity.Office;
import com.toothtrek.dentalRecord.entity.Patient;
import com.toothtrek.dentalRecord.entity.Timeslot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

@SpringBootApplication
@EntityScan("com.toothtrek.dentalRecord.entity")
public class DentalRecordApplication implements CommandLineRunner {

	@Autowired
	private TimeslotRepository timeslotRepository;

	@Autowired
	private OfficeRepository officeRepository;

	@Autowired
	private DentistRepository dentistRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private BookingRepository bookingRepository;

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
			}
		});

		SpringApplication app = new SpringApplication(DentalRecordApplication.class);
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
		mqttHandler.subscribe("toothtrek/record/+/");

		// Dummy data for testing purposes
		// TODO: Remove this when no longer needed
		// Update RequestTests.java accordingly when removing
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long time = sdf.parse("2023-12-06 18:40").getTime();
		Timestamp ts = new Timestamp(time);

		if (dentistRepository.findAll().isEmpty()) {
			Dentist dentist = new Dentist("John Doe 1", ts);
			Dentist dentist2 = new Dentist("John Doe 2", ts);
			dentistRepository.save(dentist);
			dentistRepository.save(dentist2);
		}

		if (patientRepository.findAll().isEmpty()) {
			Patient patient = new Patient("12345678910", "Johnny", ts);
			Patient patient2 = new Patient("12345678911", "Johnny2", ts);
			patientRepository.save(patient);
			patientRepository.save(patient2);
		}

		if (officeRepository.findAll().isEmpty()) {
			Office office = new Office("Desing ofis", "Lindholmen", 57.7066159f, 11.934085f);
			Office office2 = new Office("Grapix ofis", "Johanneberg", 57.690085f, 11.9760141f);
			officeRepository.save(office);
			officeRepository.save(office2);
		}

		if (timeslotRepository.findAll().isEmpty()) {
			Office office = officeRepository.findAll().get(0);
			Dentist dentist = dentistRepository.findAll().get(0);
			Office office2 = officeRepository.findAll().get(1);
			Dentist dentist2 = dentistRepository.findAll().get(1);
			Timeslot timeslot = new Timeslot(office, dentist, ts);
			Timeslot timeslot2 = new Timeslot(office2, dentist2, ts);
			timeslotRepository.save(timeslot);
			timeslotRepository.save(timeslot2);
		}

		if (bookingRepository.findAll().isEmpty()) {
			Timeslot timeslot = timeslotRepository.findAll().get(0);
			Patient patient = patientRepository.findAll().get(0);
			Booking booking = new Booking(patient, timeslot);
			booking.setState(Booking.State.completed);
			bookingRepository.save(booking);
		}

	}

}
