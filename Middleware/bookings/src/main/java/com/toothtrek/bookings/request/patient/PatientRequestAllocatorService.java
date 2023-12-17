package com.toothtrek.bookings.request.patient;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toothtrek.bookings.request.RequestHandlerInterface;

@Service
/**
 * This class is responsible for allocating the
 * correct handler for each patient request.
 */
public class PatientRequestAllocatorService {

    // Hashmap of all the patient request types and their handlers.
    private final Map<PatientRequestType, RequestHandlerInterface> handlers = new HashMap<>();

    @Autowired
    public PatientRequestAllocatorService(PatientCreateRequestHandler createHandler,
            PatientGetRequestHandler getHandler,
            PatientSetRequestHandler setHandler) {
        handlers.put(PatientRequestType.CREATE, createHandler);
        handlers.put(PatientRequestType.GET, getHandler);
        handlers.put(PatientRequestType.SET, setHandler);
    }

    /**
     * This method is responsible for allocating the correct handler.
     * 
     * @param PatientRequest PatientRequestType - The type of patient request
     * @param request        MqttMessage - The request message
     */
    public void handleRequest(PatientRequestType patientRequest, MqttMessage request) {
        RequestHandlerInterface handler = getHandler(patientRequest);
        handler.handle(request);
    }

    // Get the correct handler from the hashmap
    private RequestHandlerInterface getHandler(PatientRequestType patientRequest) {
        return handlers.get(patientRequest);
    }
}