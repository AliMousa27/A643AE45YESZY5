package com.toothtrek.bookings.mqtt;

import java.util.concurrent.ExecutorService;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.toothtrek.bookings.request.booking.BookingRequestAllocatorService;
import com.toothtrek.bookings.request.booking.BookingRequestType;
import com.toothtrek.bookings.request.timeslot.TimeslotRequestAllocatorService;
import com.toothtrek.bookings.request.timeslot.TimeslotRequestType;
import com.toothtrek.bookings.request.patient.PatientRequestAllocatorService;
import com.toothtrek.bookings.request.patient.PatientRequestType;
import com.toothtrek.bookings.request.office.OfficeRequestAllocatorService;
import com.toothtrek.bookings.request.office.OfficeRequestType;
import com.toothtrek.bookings.request.dentist.DentistRequestAllocatorService;
import com.toothtrek.bookings.request.dentist.DentistRequestType;

/**
 * MqttCallbackHandler class.
 * 
 * This class implements the MqttCallback interface and overrides all of its
 * methods. This class is used to handle callbacks from the Paho Java Client.
 * 
 * @see org.eclipse.paho.client.mqttv3.MqttCallback
 */
@Component
public class MqttCallbackHandler implements MqttCallback {

    @Autowired
    BookingRequestAllocatorService bookingRequestAllocatorService;

    @Autowired
    TimeslotRequestAllocatorService timeslotRequestAllocatorService;

    @Autowired
    PatientRequestAllocatorService patientRequestAllocatorService;

    @Autowired
    OfficeRequestAllocatorService officeRequestAllocatorService;

    @Autowired
    DentistRequestAllocatorService dentistRequestAllocatorService;

    @Autowired
    private ExecutorService executorService;

    @Override
    public void messageArrived(String topic, MqttMessage message) {

        // [0] = "toothtrek"
        // [1] = "booking", "timeslot", "patient", "office", "dentist"
        // [2] = "create", "get, "update", "delete"

        String[] topicParts = topic.split("/");

        switch (topicParts[1]) {
            case "booking":
                executorService.submit(() -> bookingRequestAllocatorService
                        .handleRequest(BookingRequestType.fromString(topicParts[2]), message));
                break;

            case "timeslot":
                executorService.submit(() -> timeslotRequestAllocatorService
                        .handleRequest(TimeslotRequestType.fromString(topicParts[2]), message));
                break;

            case "patient":
                executorService.submit(() -> patientRequestAllocatorService
                        .handleRequest(PatientRequestType.fromString(topicParts[2]), message));
                break;

            case "office":
                executorService.submit(() -> officeRequestAllocatorService
                        .handleRequest(OfficeRequestType.fromString(topicParts[2]), message));
                break;

            case "dentist":
                executorService.submit(() -> dentistRequestAllocatorService
                        .handleRequest(DentistRequestType.fromString(topicParts[2]), message));
                break;

            default:
                throw new UnsupportedOperationException("Unknown request type.");
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

}
