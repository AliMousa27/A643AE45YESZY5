package com.toothtrek.template.request.templateEntity;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.context.annotation.Configuration;

import com.toothtrek.template.request.RequestHandlerInterface;

@Configuration
public class TemplateGetRequestHandler implements RequestHandlerInterface {

    @Override
    public void handle(MqttMessage request) {
        throw new UnsupportedOperationException("Unimplemented method 'handle'");
    }

}

