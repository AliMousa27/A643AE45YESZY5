package com.toothtrek.bookings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookingsApplicationConfig {
    @Bean
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }
}
