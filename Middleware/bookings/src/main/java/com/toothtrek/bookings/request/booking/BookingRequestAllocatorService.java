package com.toothtrek.bookings.request.booking;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toothtrek.bookings.request.RequestHandlerInterface;

@Service
/**
 * This class is responsible for allocating the
 * correct handler for each booking request.
 */
public class BookingRequestAllocatorService {

    // Hashmap of all the booking request types and their handlers.
    private final Map<BookingRequestType, RequestHandlerInterface> handlers = new HashMap<>();

    @Autowired
    public BookingRequestAllocatorService(BookingCreateRequestHandler createHandler,
            BookingCancelRequestHandler cancelHandler,
            BookingGetRequestHandler getHandler,
            BookingConfirmRequestHandler confirmHandler,
            BookingRejectRequestHandler rejectHandler) {
        handlers.put(BookingRequestType.CREATE, createHandler);
        handlers.put(BookingRequestType.CANCEL, cancelHandler);
        handlers.put(BookingRequestType.GET, getHandler);
        handlers.put(BookingRequestType.CONFIRM, confirmHandler);
        handlers.put(BookingRequestType.REJECT, rejectHandler);
    }

    /**
     * This method is responsible for allocating the correct handler.
     * 
     * @param bookingRequest BookingRequestType - The type of booking request
     * @param request        MqttMessage - The request message
     */
    public void handleRequest(BookingRequestType bookingRequest, MqttMessage request) {
        RequestHandlerInterface handler = getHandler(bookingRequest);
        handler.handle(request);
    }

    // Get the correct handler from the hashmap
    private RequestHandlerInterface getHandler(BookingRequestType bookingRequest) {
        return handlers.get(bookingRequest);
    }
}