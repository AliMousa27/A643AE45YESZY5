package com.toothtrek.bookings.request.office;

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
public class OfficeRequestAllocatorService {

    // Hashmap of all the booking request types and their handlers.
    private final Map<OfficeRequestType, RequestHandlerInterface> handlers = new HashMap<>();

    @Autowired
    public OfficeRequestAllocatorService(OfficeCreateRequestHandler createHandler,
            OfficeGetRequestHandler getHandler,
            OfficeUpdateHandler updateHandler,
            OfficeDeleteHandler deleteHandler) {
        handlers.put(OfficeRequestType.CREATE, createHandler);
        handlers.put(OfficeRequestType.GET, getHandler);
        handlers.put(OfficeRequestType.UPDATE, updateHandler);
        handlers.put(OfficeRequestType.DELETE, deleteHandler);
    }

    /**
     * This method is responsible for allocating the correct handler.
     * 
     * @param officeRequest OfficeRequestType - The type of office request
     * @param request       MqttMessage - The request message
     */
    public void handleRequest(OfficeRequestType officeRequest, MqttMessage request) {
        RequestHandlerInterface handler = getHandler(officeRequest);
        handler.handle(request);
    }

    // Get the correct handler from the hashmap
    private RequestHandlerInterface getHandler(OfficeRequestType officeRequest) {
        return handlers.get(officeRequest);
    }
}
