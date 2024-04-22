package com.e2s.cam.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e2s.cam.module.SCIMAuthorization;
import com.e2s.cam.request.Request;
import com.e2s.cam.response.ListResponse;
import com.e2s.cam.response.User;
import com.e2s.cam.service.UserService;



@RestController
@CrossOrigin()
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserService service;
	
	private static final Logger logger = LogManager.getLogger(UserController.class);


	@PostMapping("/info")
	@SCIMAuthorization
	public Object getUserInfo(@RequestBody Request request) {
		logger.info("parameters = " + request);
		User result = service.getByStaffId(request.getUserId());
		return result;
	}

	@PostMapping("/findbycriteria")
	@SCIMAuthorization
	public Object getAllUsers(@RequestBody(required = false) Request request) {
		logger.info("parameters = " + request);
		ListResponse result = service.getAllUsers(request);
		return result;
	}

	@PostMapping("/update")
	@SCIMAuthorization
	public Object updateUsers(@RequestBody Request request) {
		logger.info("parameters = " + request);
		User result = service.updateByStaffId(request.getUserId(), request.getOperations().get(0).getValue());
		return result;
	}

	@PostMapping("/remove")
	@SCIMAuthorization
	public void deleteUser(@RequestBody Request request) {
		logger.info("parameters = " + request);
		service.updateByStaffId(request.getUserId(),"false");
//		service.deleteUser(request.getUserId());
	}

	@GetMapping("/test")
	public String test() {
		return "api working fine";
	}

	//test git push

}
