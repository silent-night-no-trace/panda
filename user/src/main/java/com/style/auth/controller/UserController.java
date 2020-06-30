package com.style.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leon
 * @date 2020-05-22 16:47:36
 */
@RestController
public class UserController {

	@GetMapping("/info")
	public String info() {
		return "this is info";
	}
}
