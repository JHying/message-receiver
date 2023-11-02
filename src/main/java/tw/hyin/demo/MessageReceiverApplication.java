package tw.hyin.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRetry
@SpringBootApplication
@EnableScheduling
@EnableEurekaClient
public class MessageReceiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageReceiverApplication.class, args);
	}

}
