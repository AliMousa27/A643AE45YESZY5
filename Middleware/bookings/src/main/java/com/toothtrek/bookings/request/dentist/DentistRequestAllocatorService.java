package com.toothtrek.bookings.request.dentist;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toothtrek.bookings.request.RequestHandlerInterface;

@Service
/**
 * This class is responsible for allocating the
 * correct handler for each booking request.
 */
public class DentistRequestAllocatorService {

    // Hashmap of all the booking request types and their handlers.
    private final Map<DentistRequestType, RequestHandlerInterface> handlers = new HashMap<>();

    @Autowired
    public DentistRequestAllocatorService(DentistCreateRequestHandler createHandler,
            DentistGetRequestHandler getHandler,
            DentistUpdateHandler updateHandler,
            DentistDeleteHandler deleteHandler) {
        handlers.put(DentistRequestType.CREATE, createHandler);
        handlers.put(DentistRequestType.GET, getHandler);
        handlers.put(DentistRequestType.UPDATE, updateHandler);
        handlers.put(DentistRequestType.DELETE, deleteHandler);
    }

    /**
     * This method is responsible for allocating the correct handler.
     * 
     * @param dentistRequest DentistRequestType - The type of dentist request
     * @param request       MqttMessage - The request message
     */
    public void handleRequest(DentistRequestType dentistRequest, MqttMessage request) {
        RequestHandlerInterface handler = getHandler(dentistRequest);
        handler.handle(request);
    }

    // Get the correct handler from the hashmap
    private RequestHandlerInterface getHandler(DentistRequestType dentistRequest) {
        return handlers.get(dentistRequest);
    }
}
