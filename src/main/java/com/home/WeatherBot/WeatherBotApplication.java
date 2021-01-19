package com.home.WeatherBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@SpringBootApplication
public class WeatherBotApplication {

	public static void main(String[] args) {

		SpringApplication.run(WeatherBotApplication.class, args);
	}

}
