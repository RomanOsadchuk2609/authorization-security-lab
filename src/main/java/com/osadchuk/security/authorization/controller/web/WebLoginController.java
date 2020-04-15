/*
 * Copyright (c) 2020 Roman Osadchuk. ALL RIGHTS RESERVED.
 */

package com.osadchuk.security.authorization.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class WebLoginController {

	@GetMapping
	public String login() {
		return "login";
	}
}
