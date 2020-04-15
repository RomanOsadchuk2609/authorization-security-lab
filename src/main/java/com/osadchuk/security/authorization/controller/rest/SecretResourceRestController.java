/*
 * Copyright (c) 2020 Roman Osadchuk. ALL RIGHTS RESERVED.
 */

package com.osadchuk.security.authorization.controller.rest;

import com.osadchuk.security.authorization.model.ApiResponse;
import com.osadchuk.security.authorization.model.SecretResource;
import com.osadchuk.security.authorization.service.SecretResourceService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/secretResource", produces = MediaType.APPLICATION_JSON_VALUE)
public class SecretResourceRestController {

	private final SecretResourceService secretResourceService;

	@Autowired
	public SecretResourceRestController(SecretResourceService secretResourceService) {
		this.secretResourceService = secretResourceService;
	}

	@GetMapping("/ids")
	public List<Integer> getResourcesIds(){
		return secretResourceService.getResourcesIds();
	}

	@PostMapping
	public ApiResponse createResource(@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
	                                  @RequestBody SecretResource secretResource) {
		return secretResourceService.createResource(userDetails.getUsername(), secretResource);
	}

	@GetMapping
	public ApiResponse readResource(@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
	                                @RequestParam int id) {
		return secretResourceService.readResource(userDetails.getUsername(), id);
	}

	@PutMapping
	public ApiResponse updateResource(@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
	                                  @RequestBody SecretResource secretResource) {
		return secretResourceService.updateResource(userDetails.getUsername(), secretResource);
	}
}
