package com.toothtrek.template.mqtt;

import java.util.concurrent.ExecutorService;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.toothtrek.template.request.templateEntity.TemplateRequestAllocatorService;
import com.toothtrek.template.request.templateEntity.TemplateRequestType;

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
    TemplateRequestAllocatorService templateRequestAllocatorService;

    @Autowired
    ExecutorService executorService;

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Message arrived!");
        System.out.println("   Topic: " + topic);
        System.out.println("   Message: " + message.toString());
        System.out.println();

        // [0] = "toothtrek"
        // [1] = "template_service"
        // [2] = "template" -> the entity
        // [3] = "create"
        String[] topicParts = topic.split("/");

        switch (topicParts[2]) {
            case "template":
                executorService.submit(() -> templateRequestAllocatorService
                        .handleRequest(TemplateRequestType.fromString(topicParts[3]), message));
                break;

            default:
                System.out.println("Unknown topic: " + topic);
                break;
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'connectionLost'");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deliveryComplete'");
    }

}
