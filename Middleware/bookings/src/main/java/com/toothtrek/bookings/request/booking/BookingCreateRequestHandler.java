package com.toothtrek.bookings.request.booking;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.toothtrek.bookings.entity.Booking;
import com.toothtrek.bookings.entity.Patient;
import com.toothtrek.bookings.repository.BookingRepository;
import com.toothtrek.bookings.repository.PatientRepository;
import com.toothtrek.bookings.repository.TimeslotRepository;
import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;

@Configuration
public class BookingCreateRequestHandler implements RequestHandlerInterface {

    @Autowired
    private TimeslotRepository timeSlotRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private ResponseHandler responseHandler;

    @Override
    // !NOTE! 'synchronized'
    public synchronized void handle(MqttMessage request) {        
        // Example of a message payload:
        // {
        // "patient": {
        // "id": "1234567890",
        // "name": "John Doe",
        // "dateOfBirth": "2021-05-05T00:00:00.000+00:00"
        // },
        // "timeslotId": "1234567890"
        // }

        // Create json object from payload
        JsonObject json = new Gson().fromJson(new String(request.getPayload()), JsonObject.class);
        JsonObject jsonPatient = json.get("patient").getAsJsonObject();
        // Check if timeslotId exists
        try {
            Integer timeslotId = json.get("timeslotId").getAsInt();
            timeSlotRepo.findById(timeslotId).orElseThrow();
        } catch (NoSuchElementException e) {
            responseHandler.reply(ResponseStatus.ERROR, request);
            return;
        }

        // Find if any booking with the timeslotId exists (Already booked)
        if (!bookingRepo.findByTimeslotId(json.get("timeslotId").getAsInt()).isEmpty()) {
            responseHandler.reply(ResponseStatus.ERROR, request);
            return;
        }

        // Create booking
        Booking booking = new Booking();
        booking.setTimeslotId(json.get("timeslotId").getAsInt());

        // Find patient or create new patient
        if (patientRepo.findById(jsonPatient.get("id").getAsString()).isEmpty()) {
            // Create patient
            Patient patient = new Patient();
            patient.setId(jsonPatient.get("id").getAsString());
            patient.setName(jsonPatient.get("name").getAsString());

            // Convert date to timestamp
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                long time = sdf.parse(jsonPatient.get("dateOfBirth").getAsString()).getTime();
                Timestamp ts = new Timestamp(time);
                patient.setDateOfBirth(ts);
            } catch (ParseException pe) {
                responseHandler.reply(ResponseStatus.ERROR, "Wrongly formatted date", request);
                return;
            }

            patientRepo.save(patient);
        }
        
        // Set patientId and save booking
        booking.setPatientId(jsonPatient.get("id").getAsString());
        bookingRepo.save(booking);

        // Reply with success
        System.out.println("SUCCESS TEST");
        responseHandler.reply(ResponseStatus.SUCCESS, request);
    }
}
