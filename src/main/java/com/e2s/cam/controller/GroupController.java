package com.e2s.cam.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e2s.cam.module.SCIMAuthorization;
import com.e2s.cam.request.Request;
import com.e2s.cam.response.GroupResponse;
import com.e2s.cam.response.ListResponse;
import com.e2s.cam.service.GroupService;


@RestController
@CrossOrigin()
@RequestMapping("groups")
public class GroupController {
	private static final Logger logger = LogManager.getLogger(GroupController.class);

	@Autowired
	private GroupService service;

	@PostMapping("/info")
    @SCIMAuthorization
	public Object getGroupById(@RequestBody Request request) {
		logger.info("parameters = " + request);
		GroupResponse result = service.getById(Long.valueOf(request.getGroupId()));
		return result;
	}

	@PostMapping("/findbycriteria")
	@SCIMAuthorization
	public Object getAllUsers(@RequestBody(required = false) Request request) {
		ListResponse result = service.getAllGroups(request);
		return result;
	}

	@PostMapping("/update")
	@SCIMAuthorization
	public void updateUsers(@RequestBody Request request) {
		logger.info("parameters = " + request);
		service.updateById(request.getGroupId(), request.getOperations().get(0));
	}

	@PostMapping("/remove")
	@SCIMAuthorization
	public void removegroup(@RequestBody Request request) {
		logger.info("parameters = " + request);
		service.removeGroup(request.getGroupId());
	}
	
}
