package com.toothtrek.bookings.request.booking;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.context.annotation.Configuration;

import com.toothtrek.bookings.request.RequestHandlerInterface;

@Configuration
public class BookingRejectRequestHandler implements RequestHandlerInterface{

    @Override
    public void handle(MqttMessage request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handle'");
    }

}
