package com.toothtrek.bookings;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.toothtrek.bookings.entity.Booking;
import com.toothtrek.bookings.entity.Dentist;
import com.toothtrek.bookings.entity.Office;
import com.toothtrek.bookings.entity.Patient;
import com.toothtrek.bookings.entity.TimeSlot;
import com.toothtrek.bookings.mqtt.MqttHandler;
import com.toothtrek.bookings.repository.BookingRepository;
import com.toothtrek.bookings.repository.DentistRepository;
import com.toothtrek.bookings.repository.OfficeRepository;
import com.toothtrek.bookings.repository.PatientRepository;
import com.toothtrek.bookings.repository.TimeSlotRepository;

@SpringBootApplication
@EntityScan("com.toothtrek.bookings.entity")
public class BookingsApplication implements CommandLineRunner {

	@Autowired
	private BookingRepository bookingRepo;

	@Autowired
	private DentistRepository dentistRepo;

	@Autowired
	private OfficeRepository officeRepo;

	@Autowired
	private PatientRepository patientRepo;

	@Autowired
	private TimeSlotRepository timeSlotRepo;

	private MqttHandler mqttHandler;

	public static void main(String[] args) {
		SpringApplication.run(BookingsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Check required arguments
		if (args.length != 3) {
			System.out.println("\n   Usage: java -jar <jar-file> <broker-address> <specified client-id or random> <qos> \n");
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

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long time = sdf.parse("2027-11-22 12:40").getTime();
		Timestamp ts = new Timestamp(time);

		Dentist dentist = new Dentist("John Doe", ts);
		dentistRepo.save(dentist);

		Office office = new Office("Design ofis", "Monsungatan 64", 55.5f, 43.2f);
		officeRepo.save(office);

		Patient patient = new Patient("Cool Guy", "John Doe", ts);
		patientRepo.save(patient);

		TimeSlot timeSlot = new TimeSlot(office.getId(), dentist.getId(), ts);
		timeSlotRepo.save(timeSlot);

		Booking booking = new Booking(patient.getId(), timeSlot.getId());
		bookingRepo.save(booking);
	}

}
