package com.toothtrek.bookings.request.timeslot;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.toothtrek.bookings.request.RequestHandlerInterface;
import com.toothtrek.bookings.response.ResponseHandler;
import com.toothtrek.bookings.response.ResponseStatus;

@Configuration
public class TimeslotCreateRequestHandler implements RequestHandlerInterface {

    @Autowired
    private ResponseHandler responseHandler;

    @Override
    public void handle(MqttMessage request) {
        responseHandler.reply(ResponseStatus.SUCCESS, request);
        // TODO: Implement
    }

}
