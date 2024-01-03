package com.toothtrek.dentalRecord.request.record;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toothtrek.dentalRecord.request.RequestHandlerInterface;

@Service
/**
 * This class is responsible for allocating the
 * correct handler for each request.
 */
public class RecordRequestAllocatorService {

    // Hashmap of all the request types and their handlers.
    private final Map<RecordRequestType, RequestHandlerInterface> handlers = new HashMap<>();

    @Autowired
    public RecordRequestAllocatorService(
            RecordCreateRequestHandler createHandler,
            RecordGetRequestHandler getHandler,
            RecordUpdateRequestHandler updateHandler) {
        handlers.put(RecordRequestType.CREATE, createHandler);
        handlers.put(RecordRequestType.GET, getHandler);
        handlers.put(RecordRequestType.UPDATE, updateHandler);
    }

    /**
     * This method is responsible for allocating the correct handler.
     * 
     * @param recordRequest RecordRequestType - The type of request
     * @param request       MqttMessage - The request message
     */
    public void handleRequest(RecordRequestType recordRequest, MqttMessage request) {
        RequestHandlerInterface handler = getHandler(recordRequest);
        handler.handle(request);
    }

    // Get the correct handler from the hashmap
    private RequestHandlerInterface getHandler(RecordRequestType recordRequest) {
        return handlers.get(recordRequest);
    }
}
