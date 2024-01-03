package com.toothtrek.notifications.request.notification;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toothtrek.notifications.request.RequestHandlerInterface;

@Service
/**
 * This class is responsible for allocating the
 * correct handler for each notification request.
 */
public class NotificationRequestAllocatorService {

    // Hashmap of all the notification request types and their handlers.
    private final Map<NotificationRequestType, RequestHandlerInterface> handlers = new HashMap<>();

    @Autowired
    public NotificationRequestAllocatorService(NotificationSendRequestHandler sendHandler) {
        handlers.put(NotificationRequestType.SEND, sendHandler);
    }

    /**
     * This method is responsible for allocating the correct handler.
     * 
     * @param notificationRequest NotificationRequestType - The type of notification request
     * @param request        MqttMessage - The request message
     */
    public void handleRequest(NotificationRequestType notificationRequest, MqttMessage request) {
        RequestHandlerInterface handler = getHandler(notificationRequest);
        handler.handle(request);
    }

    // Get the correct handler from the hashmap
    private RequestHandlerInterface getHandler(NotificationRequestType templateRequest) {
        return handlers.get(templateRequest);
    }
}
