package com.middleware.Logs;
import java.sql.Timestamp;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import com.middleware.Logs.mqtt.MqttCallbackHandler;
import com.middleware.Logs.mqtt.MqttHandler;


@SpringBootApplication
@EntityScan("com.middleware.Logs")

public class LogsApplication implements CommandLineRunner{
	@Autowired
	private LogsService logService;
	
	private final MqttCallbackHandler mqtt;

    @Autowired
    public LogsApplication(MqttCallbackHandler mqtt) {
        this.mqtt = mqtt;
    }
	public static void main(String[] args) {

		SpringApplication.run(LogsApplication.class, args);
	}

@Override
public void run(String... args) {

	String address = "tcp://broker.hivemq.com:1883";
	MqttHandler handler = new MqttHandler(address, "oiahkrbvjam", 0, mqtt);

	handler.connect(true, true);


}

}
