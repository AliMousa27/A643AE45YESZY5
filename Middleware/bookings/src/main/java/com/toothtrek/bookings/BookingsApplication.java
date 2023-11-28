package com.toothtrek.bookings;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.toothtrek.bookings.entity.Dentist;
import com.toothtrek.bookings.entity.Office;
import com.toothtrek.bookings.entity.Timeslot;
import com.toothtrek.bookings.mqtt.MqttCallbackHandler;
import com.toothtrek.bookings.mqtt.MqttHandler;
import com.toothtrek.bookings.repository.DentistRepository;
import com.toothtrek.bookings.repository.OfficeRepository;
import com.toothtrek.bookings.repository.TimeslotRepository;

@SpringBootApplication
@EntityScan("com.toothtrek.bookings.entity")
public class BookingsApplication implements CommandLineRunner {

	@Autowired
	private TimeslotRepository timeslotRepository;

	@Autowired
	private OfficeRepository officeRepository;

	@Autowired
	private DentistRepository dentistRepository;

	// Necessary to decouple the MQTT handler from the MQTT callback handler.
	// To avoid circular dependencies.
	@Autowired
	private MqttHandler mqttHandler;

	@Autowired
	private MqttCallbackHandler mqttCallbackHandler;

	public static void main(String[] args) {
		SpringApplication.run(BookingsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Initialize MQTT
		mqttHandler.initialize(mqttCallbackHandler);
		mqttHandler.connect(true, true);
		mqttHandler.subscribe("toothtrek/booking_service/#", 1);

		/*
		 * Example MQTT publish
		 * MqttMessage message = new MqttMessage();
		 * MqttProperties properties = new MqttProperties();
		 * message.setProperties(properties);
		 * message.
		 * setPayload("{\"patientId\": \"1234567890\", \"timeslotId\": \"1234567890\"}"
		 * .getBytes());
		 * 
		 * properties.setResponseTopic("toothtrek/booking_service/booking/create/" +
		 * mqttHandler.getClientId());
		 * mqttHandler.publish("toothtrek/booking_service/booking/create", message);
		 * mqttHandler.unsubscribe("toothtrek/booking_service/booking/#");
		 */

		// Dummy data for testing purposes
		// TODO: Remove this
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long time = sdf.parse("2027-11-22 12:40").getTime();
		Timestamp ts = new Timestamp(time);

		if (dentistRepository.findAll().isEmpty()) {
			Dentist dentist = new Dentist("John Doe 1", ts);
			Dentist dentist2 = new Dentist("John Doe 2", ts);
			dentistRepository.save(dentist);
			dentistRepository.save(dentist2);
		}

		if (officeRepository.findAll().isEmpty()) {
			Office office = new Office("Design ofis", "Lindholmen", 57.7066159f, 11.934085f);
			Office office2 = new Office("Grapix ofis", "Johanneberg", 57.690085f, 11.9760141f);
			officeRepository.save(office);
			officeRepository.save(office2);
		}

		if (timeslotRepository.findAll().isEmpty()) {
			Office office = officeRepository.findById(1).get();
			Dentist dentist = dentistRepository.findById(1).get();
			Office office2 = officeRepository.findById(2).get();
			Dentist dentist2 = dentistRepository.findById(2).get();
			Timeslot timeslot = new Timeslot(office.getId(), dentist.getId(), ts);
			Timeslot timeslot2 = new Timeslot(office2.getId(), dentist2.getId(), ts);
			timeslotRepository.save(timeslot);
			timeslotRepository.save(timeslot2);
		}

	}

}
