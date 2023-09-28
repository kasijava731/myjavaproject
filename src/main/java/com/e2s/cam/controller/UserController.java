package com.e2s.cam.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e2s.cam.annotation.Authentication;
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

	@PostMapping("/info")
	@SCIMAuthorization
	public Object getUserInfo(@RequestBody Request request) {
		User result = service.getByStaffId(request.getUserId());
		return result;
	}

	@PostMapping("/findbycriteria")
	@SCIMAuthorization
	public Object getAllUsers(@RequestBody Request request) {
		ListResponse result = service.getAllUsers(request.getFilter(), request.getStartIndex(),
				request.getItemsPerPage(), request.getAscOrderBy(), request.getDescOrderBy());
		return result;
	}

	@PostMapping("/update")
	@SCIMAuthorization
	public Object updateUsers(@RequestBody Request request) {
		User result = service.updateByStaffId(request.getUserId(), request.getOperations().get(0).getValue());
		return result;
	}

	@PostMapping("/remove")
	@SCIMAuthorization
	public void deleteUser(@RequestBody Request request) {
		service.deleteUser(request.getUserId());
	}

	@GetMapping("/test")
	public String test() {
		return "api working fine";
	}

}
