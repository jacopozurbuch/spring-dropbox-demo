package com.example.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.api.Service.implementations.UserServiceImpl;
import com.example.demo.api.models.User;
import com.example.demo.exception.UserExistentException;

import org.springframework.boot.SpringApplication;

import java.time.LocalDate;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		ApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
         try {
			UserServiceImpl userService = applicationContext.getBean(UserServiceImpl.class);
			userService.save(new User("john lennon", "jony", LocalDate.of(1950, 5, 5), "john.lennon@gmail.com", "test1234"));
			userService.save(new User("max lennon", "max", LocalDate.of(1955, 10, 10), "john.lennon@gmail.com", "test1234"));
		} catch (BeansException | UserExistentException e) {
			e.printStackTrace();
		} 
	}

}
