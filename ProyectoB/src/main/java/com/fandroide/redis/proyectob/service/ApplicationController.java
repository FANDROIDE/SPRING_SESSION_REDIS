package com.fandroide.redis.proyectob.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis/saldos")
public class ApplicationController {
	
	@RequestMapping(value = "/obtener", method = RequestMethod.POST,consumes=MediaType.TEXT_PLAIN_VALUE, produces=MediaType.TEXT_PLAIN_VALUE)
	public String login(String req, HttpServletRequest serv) {
		
		
		for(Cookie c : serv.getCookies()) {
			System.out.println("COOKIE NAME: "+c.getName()+"--------- COOKIE VALUE: "+c.getValue());
		}
		
		
		HttpSession sesion=serv.getSession(false);
		int contador=sesion.getAttribute("contador")==null?0:Integer.parseInt(sesion.getAttribute("contador").toString());
		
		System.out.println("hola mundo de saldos: "+contador);
		if(sesion.getAttribute("algo")!=null)
			System.out.println("aquiiiiiii: "+sesion.getAttribute("algo"));
		System.out.println("id session en saldos: "+sesion.getId());
		sesion.setAttribute("otro", "adiooos");
		contador++;
		sesion.setAttribute("contador", contador);
		return "hola mundo: saldos"+contador;
	}
}
