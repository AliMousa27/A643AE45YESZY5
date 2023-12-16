package com.toothtrek.bookings.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.toothtrek.bookings.entity.Booking;
import com.toothtrek.bookings.entity.Dentist;
import com.toothtrek.bookings.entity.Office;
import com.toothtrek.bookings.entity.Patient;
import com.toothtrek.bookings.entity.Timeslot;
import com.toothtrek.bookings.repository.BookingRepository;
import com.toothtrek.bookings.repository.DentistRepository;
import com.toothtrek.bookings.repository.OfficeRepository;
import com.toothtrek.bookings.repository.PatientRepository;
import com.toothtrek.bookings.repository.TimeslotRepository;

@Component
public class TestUtil {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private DentistRepository dentistRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TimeslotRepository timeslotRepository;

    public void createDummyData(
            boolean createPatient,
            boolean createDentist,
            boolean createOffice,
            boolean createTimeslot,
            boolean createBooking)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long time = sdf.parse("2023-12-06 18:40").getTime();
        Timestamp ts = new Timestamp(time);

        if (createPatient) {
            Patient patient = new Patient("id1", "Bob 1", "this@that.com");
            Patient patient2 = new Patient("id2", "Bob 2", "that@this.com");
            patientRepository.save(patient);
            patientRepository.save(patient2);
        }
        if (createDentist) {
            Dentist dentist = new Dentist("John Doe 1", ts);
            Dentist dentist2 = new Dentist("John Doe 2", ts);
            dentistRepository.save(dentist);
            dentistRepository.save(dentist2);
        }

        if (createOffice) {
            Office office = new Office("Design ofis", "Lindholmen", 57.7066159f, 11.934085f);
            Office office2 = new Office("Grapix ofis", "Johanneberg", 57.690085f, 11.9760141f);
            officeRepository.save(office);
            officeRepository.save(office2);
        }

        if (createTimeslot) {
            Office office = officeRepository.findAll().get(0);
            Dentist dentist = dentistRepository.findAll().get(0);
            Office office2 = officeRepository.findAll().get(1);
            Dentist dentist2 = dentistRepository.findAll().get(1);
            Timeslot timeslot = new Timeslot(office, dentist, ts);
            Timeslot timeslot2 = new Timeslot(office2, dentist2, ts);
            timeslotRepository.save(timeslot);
            timeslotRepository.save(timeslot2);
        }

        if (createBooking) {
            Patient patient = patientRepository.findAll().get(0);
            Timeslot timeslot = timeslotRepository.findAll().get(0);
            Patient patient2 = patientRepository.findAll().get(1);
            Timeslot timeslot2 = timeslotRepository.findAll().get(1);
            Booking booking = new Booking(patient, timeslot);
            Booking booking2 = new Booking(patient2, timeslot2);
            bookingRepository.save(booking);
            bookingRepository.save(booking2);
        }
    }

    public void deleteAll() {
        // Delete all
        // *NOTE*: Order matters
        bookingRepository.deleteAll();
        timeslotRepository.deleteAll();
        dentistRepository.deleteAll();
        officeRepository.deleteAll();
        patientRepository.deleteAll();
    }

}
