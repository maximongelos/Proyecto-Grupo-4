package com.WorkMerge.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.WorkMerge.entities.Client;
import com.WorkMerge.entities.Curriculum;
import com.WorkMerge.entities.Photo;
import com.WorkMerge.enums.Rol;
import com.WorkMerge.exceptions.ServiceException;
import com.WorkMerge.repositories.ClientRepository;



@Service
public class ClientService {
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private CurriculumService curriculumService;
	
	
	
	//Registrar cliente

	public void registerClient(String email, String password,String password2) throws ServiceException{ //BUSCAMOS UNA CLIENTE Y DEVOLVEMOS UN OPTIONAL
		validar(email,password,password2);
		
		Client cliente = new Client();
			cliente.setEmail(email);
			String encript = new BCryptPasswordEncoder().encode(password); //ENCRIPTANDO PASSWORD
			cliente.setPassword(encript);
			cliente.setRol(Rol.CLIENT);
			cliente.setActive(true);
			clientRepository.save(cliente);
		}
	
	public Client loadData(String id, String name, String surname, Integer dni, String  gender,
			String nationality, String address, String  city, Date birthday, Integer phone, String education,
			String workexperience, String language, String skills ) throws ServiceException {
		
		Optional<Client> client = clientRepository.findById(id);
		
		Curriculum cv = curriculumService.newCurriculum(name, surname, dni, gender, nationality, address, city, birthday, phone, education, workexperience, language, skills);
		
		Client c = client.get();
		c.setCurriculum(cv);
		
		return clientRepository.save(c);
	}
		
	//MODIFICAR CLIENTE

	 //Transactional (se pone porque cambia algo en la base de datos)

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })//Transactional (se pone porque cambia algo en la base de datos)

	public Client modifyClient(String id, String email, String password,String password2, Photo photo) throws ServiceException {
		validar(email,password,password2);
		Optional<Client> respuesta = clientRepository.findById(id);
		if(respuesta.isPresent()) {
		Client p = respuesta.get();
		p.setEmail(email);
		String encript = new BCryptPasswordEncoder().encode(password); //ENCRIPTANDO PASSWORD
		p.setPassword(encript);
		//p.setCurriculum(curriculum);
		p.setPhoto(photo);
		p.setActive(true);
		return clientRepository.save(p);
	}else {
		throw new ServiceException("No se encontro el cliente solicitado");
	}	
	}
	
	

	//Eliminar cliente

	public void delete(String id) throws ServiceException {
		Optional<Client> respuesta = clientRepository.findById(id);
		if(respuesta.isPresent()) {
		clientRepository.deleteById(id);
		}else {
			throw new ServiceException("No se encontro el cliente ");
		}
	}
	

	//Dar de baja cliente
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void lowCustomer(String id) {
		Optional<Client> respuesta = clientRepository.findById(id);
		if(respuesta.isPresent()) {
			Client cliente = respuesta.get();
			cliente.setActive(false);
		}
	}
	
	

	//Dar de alta cliente
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void hightCustomer(String id) {
		Optional<Client> respuesta = clientRepository.findById(id);
		if(respuesta.isPresent()) {
			Client cliente = respuesta.get();
			cliente.setActive(true);
		}
	}
	
	
	
	//Metodo validación
	public void validar(String email,String password,String password2)throws ServiceException{
		if(email==null || email.isEmpty()) {
			throw new ServiceException("El email no puede estar vacío");
		}
		if(password==null || password.isEmpty()) {
			throw new ServiceException("La contraseña no puede estar vacía");
		}
		
		if(!password.equals(password2)){
			throw new ServiceException("La contraseñas tienen que coincidir");
		}
		if(clientRepository.existByEmail(email)) {
			throw new ServiceException("Ya existe un usuario registrado con ese email.");
		}
	}
	}
	
	

