package com.middleware.Logs.mqtt;
import java.sql.Timestamp;
import java.util.Date;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.springframework.beans.factory.annotation.Autowired;

import org.json.JSONObject;

import com.middleware.Logs.Logs;
import com.middleware.Logs.LogsService;

public class MqttCallbackHandler implements MqttCallback {
    private final LogsService logService;

    @Autowired
    public MqttCallbackHandler(LogsService logService) {
        this.logService = logService;
    }

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
        try{
        String jsonString = message.toString();
        JSONObject jsonObject = new JSONObject(jsonString);
        String email = jsonObject.getString("email");
        //have to wrap with object since sometimes we send null
        Object sqlStatement = jsonObject.get("sql_statement");
        String messageString = jsonObject.getString("message");
        Timestamp time = new Timestamp(new Date().getTime());
        Logs log = new Logs(email, sqlStatement.toString(), time, messageString, topic);
        logService.saveLog(log);}
        catch(Exception e){
            System.out.println("Error: " + e);
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
