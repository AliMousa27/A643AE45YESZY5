package com.toothtrek.notifications.mqtt;

import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MqttHandler {

    // Paho Java Client
    private MqttAsyncClient client;
    private IMqttToken token;

    @Autowired
    private Environment env;

    // Options
    private String brokerAddress;
    private String clientId;
    private int qos;
    private MemoryPersistence persistence;

    /**
     * Empty MqttHandler constructor, required by Spring Boot.
     */
    public MqttHandler() {
    }

    /**
     * Initialize MqttHandler by setting up attributes and creating a client.
     * Requires a MqttCallbackHandler object.
     * <p>
     * Hint: Use Autowired to inject MqttCallbackHandler object.
     * 
     * @param mqttCallbackHandler MqttCallbackHandler object.
     */
    public void initialize(MqttCallbackHandler mqttCallbackHandler) {
        try {
            // Setup options
            this.clientId = env.getProperty("mqtt.clientId");
            List<String> validIds = Arrays.asList("random", "r", "uuid");
            if (validIds.contains(this.clientId.toLowerCase())) {
                // random client id
                this.clientId = UUID.randomUUID().toString();
            }
            this.brokerAddress = env.getProperty("mqtt.broker");
            this.qos = Integer.parseInt(env.getProperty("mqtt.qos"));
            this.persistence = new MemoryPersistence();

            // Client
            this.client = new MqttAsyncClient(this.brokerAddress, this.clientId, this.persistence);
            this.client.setCallback(mqttCallbackHandler);
        } catch (MqttException me) {
            printException(me);
        }
    }

    /**
     * Connect to broker using set attributes. Use initialize() before calling this.
     * 
     * @param cleanStart         - Sets whether the client and server should
     *                           remember state across restarts and reconnects.
     * @param automaticReconnect - If the client should automatically attempt to
     *                           reconnect to the server if the connection is lost.
     * @see MqttConnectionOptions
     */
    public void connect(boolean cleanStart, boolean automaticReconnect) {
        try {
            // Print connection details
            System.out.println();
            System.out.println("Attempting to connect to MQTT");
            System.out.println("   Connecting to broker: " + this.brokerAddress);
            System.out.println("   Client ID: " + this.clientId);
            System.out.println("   QoS: " + this.qos);
            System.out.println("   Clean Start: " + cleanStart);
            System.out.println("   Automatic Reconnect: " + automaticReconnect);
            System.out.println();

            // Connection Options
            MqttConnectionOptions connectionOptions = new MqttConnectionOptions();
            connectionOptions.setCleanStart(cleanStart);
            connectionOptions.setAutomaticReconnect(automaticReconnect);

            // Connection
            this.token = this.client.connect(connectionOptions);
            token.waitForCompletion();
        } catch (MqttException me) {
            printException(me);
        }
    }

    /**
     * Subscribe to topic using set QoS.
     * 
     * @param topic Topic (e.g. a/b/c)
     */
    public void subscribe(String topic) {
        subscribe(topic, this.qos);
    }

    /**
     * Subscribe to topic with specified QoS.
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
     * Unsubscribe from topic.
     * 
     * @param topic Topic (e.g. a/b/c)
     */
    public void unsubscribe(String topic) {
        try {
            this.token = this.client.unsubscribe(topic);
            this.token.waitForCompletion();
        } catch (MqttException me) {
            printException(me);
        }
    }

    /**
     * Publish a payload to specified topic using set QoS.
     * 
     * @param topic   Topic (e.g. a/b/c)
     * @param content Payload (e.g. lorem ipsum)
     */
    public void publish(String topic, String content) {
        publish(topic, content, this.qos);
    }

    /**
     * Publish a payload to specified topic with specified QoS.
     * 
     * @param topic   Topic (e.g. a/b/c)
     * @param content Payload (e.g. lorem ipsum)
     * @param qos     Quality of Service (0, 1, 2)
     */
    public void publish(String topic, String content, int qos) {
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        publish(topic, message);
    }

    /**
     * Publish a payload to specified topic.
     * 
     * @param topic   Topic (e.g. a/b/c)
     * @param message MqttMessage object.
     */
    public void publish(String topic, MqttMessage message) {
        try {
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
            System.out.println("Disconnecting from broker: " + this.brokerAddress);
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

    /**
     * Get client.
     * 
     * @return MqttAsyncClient
     */
    public MqttAsyncClient getClient() {
        return this.client;
    }

    /**
     * Get client id.
     * 
     * @return String
     */
    public String getClientId() {
        return this.clientId;
    }

    /**
     * Get broker address.
     * 
     * @return String
     */
    public String getBrokerAddress() {
        return this.brokerAddress;
    }

    /**
     * Get QoS.
     * 
     * @return int
     */
    public int getQos() {
        return this.qos;
    }

    /**
     * Print exception to console.
     * 
     * *NOTE* Further exception handling may be needed on a service by series basis.
     * 
     * @param mqttException MqttException object.
     */
    public void printException(MqttException mqttException) {
        System.out.println("reason " + mqttException.getReasonCode());
        System.out.println("msg " + mqttException.getMessage());
        System.out.println("loc " + mqttException.getLocalizedMessage());
        System.out.println("cause " + mqttException.getCause());
        System.out.println("excep " + mqttException);
        mqttException.printStackTrace();
    }
}
