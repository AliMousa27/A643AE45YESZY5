package com.toothtrek.Logs.mqtt;

import java.util.concurrent.ExecutorService;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.toothtrek.Logs.entity.Log;
import com.toothtrek.Logs.repository.LogRepository;

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
    private LogRepository logRepository;

    @Autowired
    ExecutorService executorService;

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        executorService.submit(() -> {
            Log log = new Log(topic, message.toString());
            logRepository.save(log);
        });
    }

    @Override
    public void connectionLost(Throwable cause) {
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

}
