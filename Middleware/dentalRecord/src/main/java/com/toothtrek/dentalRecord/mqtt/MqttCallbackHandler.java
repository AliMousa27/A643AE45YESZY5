package com.toothtrek.dentalRecord.mqtt;

import java.util.concurrent.ExecutorService;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.toothtrek.dentalRecord.request.record.RecordRequestAllocatorService;
import com.toothtrek.dentalRecord.request.record.RecordRequestType;

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
    RecordRequestAllocatorService recordRequestAllocatorService;

    @Autowired
    ExecutorService executorService;

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // [0] = "toothtrek"
        // [1] = "record"
        // [2] = "create"
        String[] topicParts = topic.split("/");

        switch (topicParts[1]) {
            case "record":
                executorService.submit(() -> recordRequestAllocatorService
                        .handleRequest(RecordRequestType.fromString(topicParts[2]), message));
                break;

            default:
                System.out.println("Unknown topic: " + topic);
                break;
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

}
