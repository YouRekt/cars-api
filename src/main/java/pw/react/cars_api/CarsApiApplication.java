package pw.react.cars_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarsApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(CarsApiApplication.class, args);
	}
}
