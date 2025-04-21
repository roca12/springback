package co.edu.unbosque.springfirstapp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringFirstAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringFirstAppApplication.class, args);
	}

	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

	// https://mkyong.com/java/how-to-send-http-request-getpost-in-java/

}
