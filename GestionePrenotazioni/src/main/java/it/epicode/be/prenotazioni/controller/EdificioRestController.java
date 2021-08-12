package it.epicode.be.prenotazioni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.epicode.be.prenotazioni.repository.EdificioRepository;

@RestController
@RequestMapping("/edificio")
public class EdificioRestController {
	@Autowired
	EdificioRepository edRepo;
}
