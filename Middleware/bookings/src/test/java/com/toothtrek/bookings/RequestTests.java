package com.toothtrek.bookings;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.toothtrek.bookings.entity.Booking;
import com.toothtrek.bookings.mqtt.MqttCallbackHandler;
import com.toothtrek.bookings.mqtt.MqttHandler;
import com.toothtrek.bookings.repository.BookingRepository;
import com.toothtrek.bookings.repository.DentistRepository;
import com.toothtrek.bookings.repository.OfficeRepository;
import com.toothtrek.bookings.repository.PatientRepository;
import com.toothtrek.bookings.repository.TimeslotRepository;
import com.toothtrek.bookings.request.booking.BookingCreateRequestHandler;
import com.toothtrek.bookings.request.booking.BookingStateRequestHandler;
import com.toothtrek.bookings.request.timeslot.TimeslotGetRequestHandler;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RequestTests {

    @Autowired
    private MqttHandler mqttHandler;

    // Repositories

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

    // Booking request handlers

    @Autowired
    private BookingCreateRequestHandler bookingCreateRequestHandler;

    @Autowired
    private BookingStateRequestHandler bookingStateRequestHandler;

    // Timeslot request handlers

    @Autowired
    private TimeslotGetRequestHandler timeslotGetRequestHandler;

    // MQTT callback variables

    private static boolean messageArrived = false;
    private static MqttMessage response;

    // Customized MQTT callback handler for testing.
    private static MqttCallbackHandler mqttCallbackHandler = new MqttCallbackHandler() {

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            response = message;
            messageArrived = true;
        }
    };

    @BeforeAll
    public void setup() {
        mqttHandler.initialize(mqttCallbackHandler);
        mqttHandler.connect(false, false);

        // Create test data
        // #TODO: Currently created in the main application til all functionality is
        // implemented.
    }

    @Test
    public void bookingCreateRequest() {
        // Timeslot ID
        Long timeslotId = timeslotRepository.findAll().get(0).getId();

        // Patient
        JsonObject jsonPatient = new JsonObject();
        jsonPatient.addProperty("id", "1234567890");
        jsonPatient.addProperty("name", "Patient");
        jsonPatient.addProperty("email", "this@that.com");

        // Message
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.add("patient", jsonPatient);
        jsonMessage.addProperty("timeslotId", timeslotId);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());
        MqttProperties properties = new MqttProperties();
        String responseTopic = "test/response/" + System.currentTimeMillis();
        properties.setResponseTopic(responseTopic);
        message.setProperties(properties);

        mqttHandler.subscribe(responseTopic);
        bookingCreateRequestHandler.handle(message);

        for (int i = 0; i < 10; i++) {
            if (!messageArrived) {
                try {
                    Thread.sleep(333);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check if booking is created
        Booking booking = bookingRepository.findByTimeslotId(timeslotId).get(0);
        assert (booking != null);
        assert (booking.getPatient().getId().equals("1234567890"));
    }

    @Test
    public void bookingChangeState() {
        // JSON message
        String responseTopic = "test/response/" + System.currentTimeMillis();
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("id", bookingRepository.findAll().get(0).getId());
        jsonMessage.addProperty("state", "confirmed");
        jsonMessage.addProperty("responseTopic", responseTopic);

        // MQTT message
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());
        MqttProperties properties = new MqttProperties();
        properties.setResponseTopic(responseTopic);
        message.setProperties(properties);

        mqttHandler.subscribe(responseTopic);
        bookingStateRequestHandler.handle(message);

        for (int i = 0; i < 10; i++) {
            if (!messageArrived) {
                try {
                    Thread.sleep(333);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("success"));

        // Check if booking state is changed
        Booking booking = bookingRepository.findAll().get(0);
        assert (booking.getState().toString().equals("confirmed"));
    }

    @Test
    public void invalidBookingStateChange() {
        // JSON message
        String responseTopic = "test/response/" + System.currentTimeMillis();
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("id", bookingRepository.findAll().get(0).getId());
        jsonMessage.addProperty("state", "confirmed"); // Invalid state: Cannot change from confirmed to confirmed
        jsonMessage.addProperty("responseTopic", responseTopic);

        // MQTT message
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());
        MqttProperties properties = new MqttProperties();
        properties.setResponseTopic(responseTopic);
        message.setProperties(properties);

        mqttHandler.subscribe(responseTopic);
        bookingStateRequestHandler.handle(message);

        for (int i = 0; i < 10; i++) {
            if (!messageArrived) {
                try {
                    Thread.sleep(333);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }

        // Check if reply is success
        assert (response != null);
        assert (new String(response.getPayload()).contains("error"));
    }

    @Test
    public void timeslotGetRequest() {
        long timeslotCount = timeslotRepository.count();

        // Message
        JsonObject jsonMessage = new JsonObject();
        String responseTopic = "test/response/" + System.currentTimeMillis();
        jsonMessage.addProperty("responseTopic", responseTopic);

        // Set payload
        MqttMessage message = new MqttMessage();
        message.setPayload(jsonMessage.toString().getBytes());
        MqttProperties properties = new MqttProperties();
        properties.setResponseTopic(responseTopic);
        message.setProperties(properties);

        mqttHandler.subscribe(responseTopic);
        timeslotGetRequestHandler.handle(message);

        for (int i = 0; i < 10; i++) {
            if (!messageArrived) {
                try {
                    Thread.sleep(333);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }

        // Check if reply is success
        assert (response != null);

        // response to JSON
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(new String(response.getPayload()), JsonObject.class);

        // Check number of timeslots returned
        /*
         * Example object
         * {"status":"success","content":
         * 
         * [{"office":{"id":3,"name":"Design ofis","address":"Lindholmen",
         * "latitude":57.706615,"longitude":11.934085},
         * 
         * "timeslots":[]},
         * 
         * {"office":{"id":4,"name":"Grapix ofis","address":"Johanneberg",
         * "latitude":57.690086,"longitude":11.976014},
         * 
         * "timeslots":[{
         * 
         * "timeslot":{"id":4,"officeId":4,"dentistId":4,
         * "date_and_time":"2023-12-06 18:40:00"},
         * "dentist":{"id":4,"name":"John Doe 2","dateOfBirth":"2023-12-06 00:00:00"}}]}
         * 
         * ]}
         */
        // For each content entry -> check timeslots length
        int timeslotsLength = 0;
        for (int i = 0; i < jsonResponse.get("content").getAsJsonArray().size(); i++) {
            timeslotsLength += jsonResponse.get("content").getAsJsonArray().get(i).getAsJsonObject().get("timeslots")
                    .getAsJsonArray().size();
        }

        // Should not return all timeslots, some are booked
        assert (timeslotsLength != timeslotCount);
    }

    @AfterEach
    public void reset() {
        messageArrived = false;
        response = null;
    }

    @AfterAll
    public void cleanUp() {
        mqttHandler.disconnect();

        // Order matters
        bookingRepository.deleteAll();
        timeslotRepository.deleteAll();
        dentistRepository.deleteAll();
        officeRepository.deleteAll();
        patientRepository.deleteAll();
    }
}
