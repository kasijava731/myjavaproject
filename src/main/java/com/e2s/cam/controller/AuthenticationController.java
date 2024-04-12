package com.e2s.cam.controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e2s.cam.entity.TokenDetails;
import com.e2s.cam.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
@RestController
@CrossOrigin()
@RequestMapping("/api")
public class AuthenticationController {
	private static final Logger logger = LogManager.getLogger(AuthenticationController.class);
	@Autowired
	private AuthenticationService authenticationService;

//	@PostMapping("/users")
//	public ResponseEntity<String> createUser(HttpServletRequest request,
//			@RequestHeader("Authorization") String authorizationHeader,
//			@RequestParam Map<String, String> requestParams) {
//
//		return authenticationService.checkAuthentication(request, authorizationHeader, requestParams);
//	}

	@PostMapping("/generateAuthToken")
	private String generateAuthToken(HttpServletRequest request,@RequestBody TokenDetails tokenDetails) {
		String authtoken = authenticationService.generateToken(request,tokenDetails);
		logger.info("test logger");
		return authtoken;
	}

}