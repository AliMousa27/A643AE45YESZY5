package com.toothtrek.template.request.templateEntity;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toothtrek.template.request.RequestHandlerInterface;

@Service
/**
 * This class is responsible for allocating the
 * correct handler for each booking request.
 */
public class TemplateRequestAllocatorService {

    // Hashmap of all the booking request types and their handlers.
    private final Map<TemplateRequestType, RequestHandlerInterface> handlers = new HashMap<>();

    @Autowired
    public TemplateRequestAllocatorService(TemplateCreateRequestHandler createHandler,
            TemplateGetRequestHandler getHandler) {
        handlers.put(TemplateRequestType.CREATE, createHandler);
        handlers.put(TemplateRequestType.GET, getHandler);
    }

    /**
     * This method is responsible for allocating the correct handler.
     * 
     * @param bookingRequest BookingRequestType - The type of booking request
     * @param request        MqttMessage - The request message
     */
    public void handleRequest(TemplateRequestType bookingRequest, MqttMessage request) {
        RequestHandlerInterface handler = getHandler(bookingRequest);
        handler.handle(request);
    }

    // Get the correct handler from the hashmap
    private RequestHandlerInterface getHandler(TemplateRequestType templateRequest) {
        return handlers.get(templateRequest);
    }
}
