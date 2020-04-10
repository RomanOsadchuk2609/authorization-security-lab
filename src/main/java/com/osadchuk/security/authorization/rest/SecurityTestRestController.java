/*
 * Copyright (c) 2020 Roman Osadchuk. ALL RIGHTS RESERVED.
 */

package com.osadchuk.security.authorization.rest;

import com.osadchuk.security.authorization.model.SecretResource;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class SecurityTestRestController {

	private static final List<SecretResource> SECRET_RESOURCES = Arrays.asList(
			new SecretResource(1, "recourse 1", "value 1"),
			new SecretResource(2, "recourse 2", "value 2"),
			new SecretResource(3, "recourse 3", "value 3"));

	@GetMapping("/getResources")
	public List<SecretResource> login() {
		return SECRET_RESOURCES;
	}

	@Hidden
	@GetMapping("/test")
	public List<SecretResource> loginTest() {
		return SECRET_RESOURCES;
	}
}
