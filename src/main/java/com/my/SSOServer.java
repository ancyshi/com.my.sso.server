package com.my;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SSOServer {

	public static void main(String[] args) {
		SpringApplication.run(SSOServer.class, args);
	}

}
