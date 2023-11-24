package com.toothtrek.bookings;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(args = { "tcp://test.mosquitto.org:1883", "random", "1" })
class BookingsApplicationTests {

	@Test
	void contextLoads() {
	}

}
