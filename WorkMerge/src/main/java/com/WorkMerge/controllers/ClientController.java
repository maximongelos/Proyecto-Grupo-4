package com.WorkMerge.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ClientController {
	@GetMapping("/register")
	public String register() {
		return "registroDatoCliente";	
	}
}
