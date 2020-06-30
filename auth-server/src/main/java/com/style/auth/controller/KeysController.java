package com.style.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接口`测试
 *
 * @author leon
 * @date 2020-05-22 16:47:36
 */
@RestController
public class KeysController {

	@GetMapping(value = "/oauth2/keys", produces = "application/json; charset=UTF-8")
	public String getKeys() {
		return "key";
	}
}
