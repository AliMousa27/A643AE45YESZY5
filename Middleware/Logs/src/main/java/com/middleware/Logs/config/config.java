package com.middleware.Logs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.middleware.Logs.LogsService;
import com.middleware.Logs.mqtt.MqttCallbackHandler;

@Configuration
public class config {

    @Bean    
    public MqttCallbackHandler mqttCallbackHandler(LogsService logsService) {
        return new MqttCallbackHandler(logsService);
    }
}
