package com.WorkMerge.controllers;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.WorkMerge.entities.Company;
import com.WorkMerge.entities.Job;
import com.WorkMerge.exceptions.ServiceException;
import com.WorkMerge.services.CompanyService;
import com.WorkMerge.services.JobService;

@Controller
@RequestMapping("/company")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
<<<<<<< HEAD
	@Autowired 
=======
	@Autowired
>>>>>>> 9f9fe2c0447620aa9b67f4b2c7d60133b8af3177
	private JobService jobService;
	
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
			return "redirect:/login";
		} catch (ServiceException e) {
			e.printStackTrace();
			return "redirect:/company/loadCom/".concat(id);
		} catch (ParseException e) {
			e.printStackTrace();
			return "redirect:/company/loadCom/".concat(id);
		}
		
	}
	
	@GetMapping("/perfil/{id}")
	public String perfilCompany (@PathVariable("id") String id, ModelMap modelo)	{
		try {
			List<Job> listJobs = companyService.listMyJobs(id);
			modelo.addAttribute("jobs", listJobs );
			modelo.addAttribute("company", companyService.obtenerPorId(id));
			
			return this.viewPath.concat("perfilEmpresa");
		} catch (ServiceException e) {
			e.printStackTrace();
			return "index";
		}
	}	
	
	@GetMapping("/delete/{id}")
	public String deleteJob(@PathVariable("id") String id) {
		try {
<<<<<<< HEAD
			jobService.deleteJob(id);
			return "redirect:/company/perfil/".concat(id);
=======
			companyService.deleteCompany(id);
			return "redirect:/admin/adminEmpresas";
		} catch (ServiceException e) {
			e.printStackTrace();
			return "redirect:/admin";
		}
		
	}
	
	@GetMapping("/eliminarTrabajo/{id}")
	public String deleteJob(@PathVariable("id") String id) {
		try {
			jobService.deleteJob(id);
			return "redirect:/admin/adminEmpresas";
>>>>>>> 9f9fe2c0447620aa9b67f4b2c7d60133b8af3177
		} catch (ServiceException e) {
			e.printStackTrace();
			return "redirect:/admin";
		}
		
	}
	
	@GetMapping("loadJob/{id}")
	public String createJob(ModelMap modelo, @PathVariable("id") String id) {
		try {
			modelo.addAttribute("company", companyService.obtenerPorId(id));
			return this.viewPath.concat("trabajo-form");
		} catch (ServiceException e) {
			e.printStackTrace();
			return "redirect:/company/perfil/".concat(id);
		}
	}
	
	@PostMapping("saveJob/{id}")
	public String registerJob(@PathVariable("id") String id, @RequestParam("titulo") String titulo, @RequestParam("fecha") String fecha,
			@RequestParam("disponibilidad") String disponibilidad, @RequestParam("categoria") String categoria, @RequestParam("descripcion") String descripcion,
			@RequestParam("salario") String salario, @RequestParam("experiencia") String experiencia) {
		try {
			companyService.uploadJobs(id, titulo, fecha, disponibilidad, categoria, descripcion, salario, experiencia);
			return "redirect:/company/perfil/".concat(id);
		} catch (ServiceException e) {
			e.getMessage("ERROR COMUN");
			return "redirect:/";
		} catch (ParseException e) {
			e.printStackTrace();
			return "redirect:/";
		}
		
	}
	
	@GetMapping("alta/{id}")
	public String alta(@PathVariable("id") String id) {
		try {
			jobService.upgradeJob(id);
			return "redirect:/company/perfil/".concat(id);
		} catch (ServiceException e) {
			e.printStackTrace();
			return "redirect:/company/perfil/".concat(id);
		}
		
	}
	
	@GetMapping("baja/{idJob}/{idCom}")
	public String baja(@PathVariable("idJob") String idJob, @PathVariable("idCon") String idCon) {
		try {
			jobService.downgradeJob(idJob);
			return "redirect:/company/perfil/".concat(idCon);
		} catch (ServiceException e) {
			e.printStackTrace();
			return "redirect:/company/perfil/".concat(idCon);
		}
		
	}
	
	
}
