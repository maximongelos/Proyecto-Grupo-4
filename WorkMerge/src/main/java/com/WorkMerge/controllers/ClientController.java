package com.WorkMerge.controllers;


import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.WorkMerge.entities.Client;
import com.WorkMerge.entities.Job;
import com.WorkMerge.exceptions.ServiceException;
import com.WorkMerge.services.ClientService;
import com.WorkMerge.services.JobService;
import com.WorkMerge.services.NotificationService;

@Controller
@RequestMapping("/client")
public class ClientController {
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private NotificationService notificationService;
	
	private final String viewPath = "cliente/";
	
	@GetMapping("/form")
	public String register() {
		return this.viewPath.concat("registroInicialCliente");
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENT')")
	@GetMapping("/perfilCli/{id}")
	public String perfilClient (@PathVariable("id") String id, ModelMap modelo)	{
		try {
			Client c = clientService.obtenerPorId(id);
			modelo.addAttribute("client", c);	
			return this.viewPath.concat("PerfilCliente");
		} catch (ServiceException e) {
			e.printStackTrace();
			return "index";
		}
	}	

	@PreAuthorize("hasAnyRole('ROLE_CLIENT')")
	@GetMapping("/hubCli/{id}")
	public String inicioClient (@PathVariable("id") String id, ModelMap modelo,@RequestParam(required = false) String q)	{
		

		if(q != null) {
			modelo.addAttribute("jobs", jobService.findActiveByQ(q));
		}else {
			List<Job> jobs = jobService.listActives();
			modelo.addAttribute("jobs",jobs);
			modelo.addAttribute("jobs", jobService.listActives());
		}
		return this.viewPath.concat("InicioCliente");
	}
	
	@PostMapping("/save")
	public String createClient(@RequestParam("email") String email, @RequestParam("password") String password,
			@RequestParam("password2") String password2,@RequestParam("photo") MultipartFile file) {
		try {
			clientService.registerClient(email, password, password2,file);
			Client c = clientService.obtenerPorMail(email);
			String id = c.getId();
			return "redirect:/client/loadCv/".concat(id);
		} catch (ServiceException e) {
			e.printStackTrace();
			return "redirect:/client/form";
		}
	}
	
	@GetMapping("/loadCv/{id}")
	public String registerCv(ModelMap modelo, @PathVariable("id") String id) {
		try {
			Client c = clientService.obtenerPorId(id);
			modelo.addAttribute("cliente", c);
			return this.viewPath.concat("registroDatoCliente");
		} catch (ServiceException e) {
			e.printStackTrace();
			return "redirect:/client/form";
		}
		
	}
	
	@PostMapping("/saveCv/{id}")
	public String createCv(@PathVariable("id") String id, @RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido, 
			@RequestParam("dni") String dni, @RequestParam("genero") String genero, @RequestParam("nacionalidad") String nacionalidad,
			@RequestParam("ciudad") String ciudad, @RequestParam("domicilio") String domicilio, @RequestParam("fecha") String fecha, @RequestParam("telefono") String telefono, 
			@RequestParam("educacion") String educacion, 
			@RequestParam("experienciaLaboral") String experienciaLaboral, @RequestParam("idiomas") String idiomas,
			@RequestParam("habilidadesInformáticas") String habilidadesInformáticas) {
		try {
			clientService.loadData(id, nombre, apellido, dni, genero, nacionalidad, ciudad, domicilio, fecha, telefono, educacion, experienciaLaboral, idiomas, habilidadesInformáticas);
			return "redirect:/login";
		} catch (ServiceException e) {
			e.printStackTrace();
			return "redirect:/";
		} catch (ParseException e) {
			e.printStackTrace();
			return "redirect:/";
		}
		
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENT')")
	@GetMapping("/editCv/{id}")
	public String editCv(ModelMap modelo, @PathVariable("id") String id) {
		try {	
			modelo.addAttribute("cliente", clientService.obtenerPorId(id));
			return this.viewPath.concat("EditarCliente");
		} catch (ServiceException e) {
			e.printStackTrace();
			return "redirect:/client/form";
		}
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENT')")
	@PostMapping("/updateCv/{id}")
	public String updateCv(@PathVariable("id") String id, @RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido, 
			@RequestParam("dni") String dni, @RequestParam("genero") String genero, @RequestParam("nacionalidad") String nacionalidad,
			@RequestParam("ciudad") String ciudad, @RequestParam("domicilio") String domicilio, @RequestParam("fecha") String fecha, @RequestParam("telefono") String telefono, 
			@RequestParam("educacion") String educacion, 
			@RequestParam("experienciaLaboral") String experienciaLaboral, @RequestParam("idiomas") String idiomas,
			@RequestParam("habilidadesInformáticas") String habilidadesInformáticas,@RequestParam("photo") MultipartFile file) {
		try {			
			clientService.modifyClient(id, nombre, apellido, dni, genero, nacionalidad, domicilio, ciudad, fecha, telefono, educacion, experienciaLaboral, idiomas, habilidadesInformáticas, file);
			return "redirect:/client/perfilCli/".concat(id);
		} catch (ServiceException e) {
			e.printStackTrace();
			return "redirect:/";
		} catch (ParseException e) {
			e.printStackTrace();
			return "redirect:/";
		}
		
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENT')")
	@GetMapping("/eliminar/{id}")
	public String deleteClint(@PathVariable("id") String id) {
		try {
			clientService.delete(id);
			return "redirect:/admin/adminClientes";
		} catch (ServiceException e) {
			e.printStackTrace();
			return "redirect:/admin";
		}
		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_CLIENT')")
	@PostMapping("/mailsender/{idJob}/{idCli}")
	public String enviarMail(@PathVariable String idJob, @PathVariable String idCli) {

		try {
			String mailCon = jobService.obtenerPorId(idJob).getCompany().getEmail();
			String mailCli = clientService.obtenerPorId(idCli).getEmail();
			System.out.println(mailCli);
			notificationService.notificar(idJob, idCli, mailCli, mailCon);
			return "redirect:/client/hubCli/".concat(idCli);
		} catch (Exception e) {
			return "redirect:/";
		}
	}
}
