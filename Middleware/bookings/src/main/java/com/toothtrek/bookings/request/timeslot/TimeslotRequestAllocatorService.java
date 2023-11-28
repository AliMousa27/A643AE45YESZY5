package com.toothtrek.bookings.request.timeslot;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toothtrek.bookings.request.RequestHandlerInterface;

@Service
/**
 * This class is responsible for allocating the
 * correct handler for each timeslot request.
 */
public class TimeslotRequestAllocatorService {

    // Hashmap of all the booking request types and their handlers.
    private final Map<TimeslotRequestType, RequestHandlerInterface> handlers = new HashMap<>();

    @Autowired
    public TimeslotRequestAllocatorService(TimeslotCreateRequestHandler createHandler,
            TimeslotCancelRequestHandler cancelHandler,
            TimeslotGetRequestHandler getHandler) {
        handlers.put(TimeslotRequestType.CREATE, createHandler);
        handlers.put(TimeslotRequestType.CANCEL, cancelHandler);
        handlers.put(TimeslotRequestType.GET, getHandler);
    }

    /**
     * This method is responsible for allocating the correct handler.
     * 
     * @param timeslotRequest TimeslotRequestType - The type of booking request
     * @param request         MqttMessage - The request message
     */
    public void handleRequest(TimeslotRequestType timeslotRequest, MqttMessage request) {
        RequestHandlerInterface handler = getHandler(timeslotRequest);
        handler.handle(request);
    }

    // Get the correct handler from the hashmap
    private RequestHandlerInterface getHandler(TimeslotRequestType timeslotRequest) {
        return handlers.get(timeslotRequest);
    }
}