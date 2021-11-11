package com.WorkMerge.controllers;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.WorkMerge.entities.Company;
import com.WorkMerge.exceptions.ServiceException;
import com.WorkMerge.services.CompanyService;

@Controller
@RequestMapping("/company")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	private final String viewPath = "empresa/";
	
	@GetMapping("/form")
	public String registerCompany() {
		return this.viewPath.concat("registroInicialEmpresa");
	}
		
		
	@PostMapping("/save")
	public String createCompany(@RequestParam("email") String email, @RequestParam("password") String password,
			@RequestParam("password2") String password2) {
		try {
			companyService.newCompany(email, password, password2);
			Company c = companyService.obtenerPorMail(email);
			String id = c.getId();
			return "redirect:/company/loadCom/".concat(id);
		} catch (ServiceException e) {
			e.printStackTrace();
			return "redirect:/company/form";
		}
	}
	
	@GetMapping("/loadCom/{id}")
	public String registerCom(ModelMap modelo, @PathVariable("id") String id) {
		try {
			modelo.addAttribute("company", companyService.obtenerPorId(id));
			return this.viewPath.concat("registroDatoEmpresa");
		} catch (ServiceException e) {
			e.printStackTrace();
			return "redirect:/company/form";
		}
		
	}
	
	@PostMapping("/saveCom/{id}")
	public String createCom(@PathVariable("id") String id, @RequestParam("name") String name) {
		try {
			companyService.loadData(id, name);
			return "redirect:/";
		} catch (ServiceException e) {
			e.printStackTrace();
			return "redirect:/";
		} catch (ParseException e) {
			e.printStackTrace();
			return "redirect:/";
		}
		
	}
				
}
