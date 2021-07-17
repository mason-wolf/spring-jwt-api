package com.fitnessapp;


import java.io.FileNotFoundException;

import java.text.ParseException;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringBootAuthApp {

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		SpringApplication.run(SpringBootAuthApp.class, args);
	}
	
}