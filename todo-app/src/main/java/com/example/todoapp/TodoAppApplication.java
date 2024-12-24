package com.example.todoapp;

import com.example.todoapp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoAppApplication {

	@Autowired
	private NotificationService notificationService;

	public static void main(String[] args) {
		SpringApplication.run(TodoAppApplication.class, args);
	}
}