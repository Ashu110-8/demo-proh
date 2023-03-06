package com.springrest.fnt.springrestfnt.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.springrest.fnt.springrestfnt.service.Iphone;
import com.springrest.fnt.springrestfnt.service.Servicelayer;
import com.springrest.fnt.springrestfnt.service.SiteQueryResponse;

@RestController
public class MyController {
	private final Servicelayer sl;
	
@Autowired
public MyController(Servicelayer servicelayer) {
	this.sl=servicelayer;
}

@GetMapping("/")
public ResponseEntity<Iphone> getData() {
	return new ResponseEntity<Iphone> (sl.consumeAPI(),HttpStatus.OK);
}
@PostMapping("/token")
public ResponseEntity<String> getToken() {
	return  sl.consumeAPIToken();
}
@GetMapping("/mandator")
public String getMandator() {
	ResponseEntity<String> rs = sl.consumeAPIToken();
	return  sl.consumeAPIMandator();
}

//@GetMapping("/sitequery")
//public ResponseEntity<String> getSite() {
	//return sl.sitequeryAPI( String s);
	
//}

@GetMapping("/sitequery1")
public ResponseEntity<String> getSite(@RequestParam double radius, double lati, double longi ) {
	return sl.sitequeryradius(radius,lati,longi);
}
@GetMapping("/networkelement")
public ResponseEntity<String> getNetworkElement(@RequestParam String networkElement) {
	return  sl.consumeNetworkElement(networkElement);
}

}
