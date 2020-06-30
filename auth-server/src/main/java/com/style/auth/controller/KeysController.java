package com.style.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leon
 * @date 2020-05-22 16:47:36
 */
@RestController
public class KeysController {

	@Autowired
	private JWKSet jwkSet;

	@GetMapping(value = "/oauth2/keys", produces = "application/json; charset=UTF-8")
	public String getKeys(){
		return jwkSet.toString();
	}
}
