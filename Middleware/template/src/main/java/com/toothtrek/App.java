package com.toothtrek;

import com.middleware.Logs.mqtt.MqttHandler;

public class App {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java -jar <jar-file> <broker-address> <client-id> <qos>");
            System.exit(1);
        }
        String broker = args[0];
        String clientId = args[1];
        int qos = Integer.parseInt(args[2]);

        MqttHandler handler = new MqttHandler(broker, clientId, qos);
        handler.connect(true, true);

        // *** Example usage ***
        // String topic = "a/b/c";
        // String content = "lorem ipsum";
        // handler.subscribe(topic);
        // handler.publish(topic, content);

        // fyi: program will continue running, use ctrl+c to exit
        // specify how to handle callbacks in /mqtt/MqttCallbackHandler.java
    }
}
