package br.com.thais.medvoll.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/hello")
public class HelloController {
	@GetMapping
	public String olaMundo() {
		return "Olá, mundo! Spring";
	}
}
