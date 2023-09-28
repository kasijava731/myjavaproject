package com.e2s.cam.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e2s.cam.annotation.Authentication;

@RestController
@RequestMapping("/api1")
public class CamController {
	
	@GetMapping("test")
	@Authentication
	  public String testMethod() {
		  return "authenticated successfully";
	  }

}
