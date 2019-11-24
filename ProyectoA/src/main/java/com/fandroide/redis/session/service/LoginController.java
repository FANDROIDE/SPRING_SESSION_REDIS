package com.fandroide.redis.session.service;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.codec.binary.Base64;

@RestController
@RequestMapping("/redis/login")
public class LoginController {
	
	@RequestMapping(value = "/crear", method = RequestMethod.POST,consumes=MediaType.TEXT_PLAIN_VALUE, produces=MediaType.TEXT_PLAIN_VALUE)
	public String login(String req, HttpServletRequest serv) {
		HttpSession sesion=serv.getSession(true);
		int contador=sesion.getAttribute("contador")==null?0:Integer.parseInt(sesion.getAttribute("contador").toString());
		System.out.println("hola mundo: "+contador);
		System.out.println("id session: "+sesion.getId());
		
		contador++;
		sesion.setAttribute("contador", contador);
		sesion.setAttribute("algo", "ola k ace");
		peticion(sesion, "http://localhost:8082/fandroide/redis/saldos/obtener");
		if(sesion.getAttribute("otro")!=null)
			System.out.println("hola mundo********: "+sesion.getAttribute("otro").toString());
		
		new PrimeThread(sesion).start();
		if(sesion.getAttribute("nombre")!=null) {
			System.out.println("hola mundoPPPPPPPPPPPPPPP: "+sesion.getAttribute("nombre").toString());
		}else {
			System.out.println("no existe");
		}
		
		return "hola mundo k ace: "+contador;
	}
	
	
	class PrimeThread extends Thread {
        HttpSession  sesion;
        PrimeThread(HttpSession sesion) {
            this.sesion = sesion;
        }

        public void run() {
        	System.out.println("Inicia hilo");
        	try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	System.out.println("se sube a sesion hilo");
        	sesion.setAttribute("nombre", "feliciano martinez");
        	System.out.println("se subio a sesion hilo");
        }
    }
	
	private static String peticion(HttpSession sesion, String url) {
		RestTemplate restTemplate = new RestTemplate(); 
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Cookie", "JSESSIONID=" + encodeB64(sesion.getId()));
		requestHeaders.setContentType(MediaType.TEXT_PLAIN);
		requestHeaders.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
		HttpEntity requestEntity = new HttpEntity("{}", requestHeaders);
		ResponseEntity rssResponse = restTemplate.exchange(url,
		    HttpMethod.POST,
		    requestEntity,
		    String.class);
		String rss = rssResponse.getBody().toString();
		return rss;
	}
	
	private static String encodeB64(String entrada) {
		byte[] bytesEncoded = Base64.encodeBase64(entrada.getBytes());
		System.out.println("encoded value is " + new String(bytesEncoded));
		return new String(bytesEncoded);
	}
	
	@RequestMapping(value = "/eliminar", method = RequestMethod.POST,consumes=MediaType.TEXT_PLAIN_VALUE, produces=MediaType.TEXT_PLAIN_VALUE)
	public String eliminar(String req, HttpServletRequest serv) {
		HttpSession sesion=serv.getSession(false);
		if(sesion!=null) {
			sesion.invalidate();
			System.out.println("cerrando session");
		}
		return "hola mundo:";
	}

}
