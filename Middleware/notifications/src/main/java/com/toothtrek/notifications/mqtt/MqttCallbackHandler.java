package com.toothtrek.notifications.mqtt;

import java.util.concurrent.ExecutorService;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.toothtrek.notifications.request.notification.NotificationRequestAllocatorService;
import com.toothtrek.notifications.request.notification.NotificationRequestType;

/**
 * MqttCallbackHandler class.
 * 
 * This class implements the MqttCallback interface and overrides all of its
 * methods. This class is used to handle callbacks from the Paho Java Client.
 * 
 * @see org.eclipse.paho.mqttv5.client.MqttCallback
 */
@Component
public class MqttCallbackHandler implements MqttCallback {

    @Autowired
    NotificationRequestAllocatorService notificationRequestAllocatorService;

    @Autowired
    ExecutorService executorService;

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // [0] = "toothtrek"
        // [1] = "notification"
        // [2] = "send"
        String[] topicParts = topic.split("/");
        executorService.submit(() -> notificationRequestAllocatorService
                .handleRequest(NotificationRequestType.fromString(topicParts[2]), message));
    }

    @Override
    public void connectionLost(Throwable cause) {
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

}
