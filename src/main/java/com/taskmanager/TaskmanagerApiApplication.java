package com.taskmanager;

import com.taskmanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@EnableCaching
@SpringBootApplication
public class TaskmanagerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskmanagerApiApplication.class, args);
	}

}
