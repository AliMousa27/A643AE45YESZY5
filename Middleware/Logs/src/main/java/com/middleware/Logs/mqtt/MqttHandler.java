package com.middleware.Logs.mqtt;

import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;
@Component

public class MqttHandler {

    // Paho Java Client
    private MqttAsyncClient client;
    private IMqttToken token;

    // Options
    private String brokerAddress;
    private String clientId;
    private int qos;
    private MemoryPersistence persistence;
    
    List<String> topics = List.of(
        "toothtrek/patient/booking/cancel",
        "toothtrek/patient/booking/create",
        "toothtrek/patient/booking/get-all",
        "toothtrek/patient/notification/get-new",
        "toothtrek/patient/notification/get-all",
        "toothtrek/dentist/timeslot/create",
        "toothtrek/dentist/timeslot/cancel",
        "toothtrek/dentist/booking/confirm",
        "toothtrek/dentist/booking/reject",
        "toothtrek/dentist/notification/get-new",
        "toothtrek/dentist/notification/get-all",
        "toothtrek/authentication/registrations",
        "toothtrek/authentication/logins"
        );
    private MqttCallbackHandler mqttCallbackHandler;

    /**
     * MqttHandler constructor.
     * 
     * @param brokerAddress Broker address (e.g. tcp://localhost:1883)
     * @param clientId      Client ID (e.g. my-client-id)
     * @param qos           Quality of Service (0, 1, 2)
     */
    public MqttHandler(String brokerAddress, String clientId, int qos, MqttCallbackHandler mqttCallbackHandler) {

        this(brokerAddress, clientId, qos, new MemoryPersistence(), mqttCallbackHandler);
    }

    public MqttHandler() {
        // default constructor
    }
    /**
     * MqttHandler constructor.
     * 
     * @param brokerAddress Broker address (e.g. tcp://localhost:1883)
     * @param clientId      Client ID (e.g. my-client-id)
     * @param qos           Quality of Service (0, 1, 2)
     * @param persistence   Persistence (e.g. new MemoryPersistence())
     */
    public MqttHandler(String brokerAddress, String clientId, int qos, MemoryPersistence persistence, MqttCallbackHandler mqttCallbackHandler) {
        this.qos = qos;
        this.brokerAddress = brokerAddress;
        this.clientId = clientId;
        this.persistence = persistence;
        this.mqttCallbackHandler = mqttCallbackHandler;
        
    }

    /**
     * Connect to broker.
     * 
     * @param cleanStart         - Sets whether the client and server should
     *                           remember state across restarts and reconnects.
     * @param automaticReconnect - If the client should automatically attempt to
     *                           reconnect to the server if the connection is lost.
     * @see MqttConnectionOptions
     */
    public void connect(boolean cleanStart, boolean automaticReconnect) {
        try {
            // Options
            MqttConnectionOptions connectionOptions = new MqttConnectionOptions();
            connectionOptions.setCleanStart(cleanStart);
            connectionOptions.setAutomaticReconnect(automaticReconnect);

            // Client
            this.client = new MqttAsyncClient(this.brokerAddress, this.clientId, this.persistence);
            //MqttCallbackHandler callbackHandler = new MqttCallbackHandler();
            this.client.setCallback(this.mqttCallbackHandler);

            // Connection
            this.token = this.client.connect(connectionOptions);

            token.waitForCompletion();

            //connect to each topic defined to log them
            for (String topic : topics) {
                subscribe(topic);
            }
        } catch (MqttException me) {
            printException(me);
        }
    }

    /**
     * Subscribe to topic.
     * 
     * @param topic Topic (e.g. a/b/c)
     */
    public void subscribe(String topic) {
        subscribe(topic, this.qos);
    }

    /**
     * Subscribe to topic.
     * 
     * @param topic Topic (e.g. a/b/c)
     * @param qos   Quality of Service (0, 1, 2)
     */
    public void subscribe(String topic, int qos) {
        try {
            this.token = this.client.subscribe(topic, qos);
            this.token.waitForCompletion();
        } catch (MqttException me) {
            printException(me);
        }
    }

    /**
     * Publish a payload to specified topic.
     * 
     * @param topic   Topic (e.g. a/b/c)
     * @param content Payload (e.g. lorem ipsum)
     */
    public void publish(String topic, String content) {
        publish(topic, content, this.qos);
    }

    /**
     * Publish a payload to specified topic.
     * 
     * @param topic   Topic (e.g. a/b/c)
     * @param content Payload (e.g. lorem ipsum)
     * @param qos     Quality of Service (0, 1, 2)
     */
    public void publish(String topic, String content, int qos) {
        try {
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            this.token = this.client.publish(topic, message);
            this.token.waitForCompletion();
        } catch (MqttException me) {
            printException(me);
        }
    }

    /**
     * Disconnect from broker.
     */
    public void disconnect() {
        try {
            this.token = this.client.disconnect();
            token.waitForCompletion();
        } catch (MqttException me) {
            printException(me);
        }
    }

    /**
     * Close connection.
     */
    public void close() {
        try {
            this.client.close();
        } catch (MqttException me) {
            printException(me);
        }
    }

    /**
     * Check if client is connected.
     * 
     * @return boolean
     */
    public boolean isConnected() {
        return this.client.isConnected();
    }

    public MqttAsyncClient getClient() {
        return this.client;
    }

    public void printException(MqttException me) {
        System.out.println("reason " + me.getReasonCode());
        System.out.println("msg " + me.getMessage());
        System.out.println("loc " + me.getLocalizedMessage());
        System.out.println("cause " + me.getCause());
        System.out.println("excep " + me);
        me.printStackTrace();
    }
}