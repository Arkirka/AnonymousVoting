package ru.vorobyov.VotingServWithAuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class VotingServWithAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotingServWithAuthApplication.class, args);
	}

}
