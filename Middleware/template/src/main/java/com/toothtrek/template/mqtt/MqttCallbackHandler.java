package com.toothtrek.template.mqtt;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
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

    @Override
    public void disconnected(MqttDisconnectResponse disconnectResponse) {
        System.out.println("Disconnected!");
        System.out.println("   Response: " + disconnectResponse.toString());
        System.out.println();
    }

    @Override
    public void mqttErrorOccurred(MqttException exception) {
        System.out.println("MqttException!");
        System.out.println("   Exception: " + exception.toString());
        System.out.println();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Message arrived!");
        System.out.println("   Topic: " + topic);
        System.out.println("   Message: " + message.toString());
        System.out.println();
        String[] topicParts = topic.split("/");

        switch (topicParts[2]) {
            case "template":
                templateRequestAllocatorService.handleRequest(TemplateRequestType.fromString(topicParts[3]), message);
                break;

            default:
                throw new UnsupportedOperationException("Unknown request type.");
            // break;
        }
    }

    @Override
    public void deliveryComplete(IMqttToken token) {
        System.out.println("Delivery complete!");
        System.out.println("   Token: " + token.toString());
        System.out.println();
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        System.out.println("Connect complete!");
        System.out.println("   Reconnect: " + reconnect);
        System.out.println("   Server URI: " + serverURI);
        System.out.println();
    }

    @Override
    public void authPacketArrived(int reasonCode, MqttProperties properties) {
        System.out.println("Auth packet arrived!");
        System.out.println("   Reason code: " + reasonCode);
        System.out.println("   Properties: " + properties.toString());
        System.out.println();
    }

}
