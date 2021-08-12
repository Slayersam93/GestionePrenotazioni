package it.epicode.be.prenotazioni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.epicode.be.prenotazioni.repository.UtenteRepository;

@RestController
@RequestMapping("/utente")
public class UtenteRestController {
	@Autowired
	UtenteRepository utenteRepo;
}
