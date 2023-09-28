package com.e2s.cam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.e2s.cam","com.e2s.cam.config","com.e2s.cam.repository", "com.e2s.cam.controller","com.e2s.cam.service","com.e2s.cam.entity"})
public class CamApiInterfaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamApiInterfaceApplication.class, args);
	}

    
}
