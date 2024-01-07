package com.toothtrek.notifications.request;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface RequestHandlerInterface {
    // Reminder to add 'synchronized' keyword to handle methods that requires it.
    // (e.g. creations)
    public void handle(MqttMessage message);
}
