package it.epicode.be.prenotazioni.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.epicode.be.exception.BusinessLogicException;
import it.epicode.be.exception.EntityNotFoundException;
import it.epicode.be.prenotazioni.controller.dto.ElencoRegole;
import it.epicode.be.prenotazioni.controller.dto.PrenotazioneDto;
import it.epicode.be.prenotazioni.model.Prenotazione;
import it.epicode.be.service.abstraction.AbstractReservationService;

@RestController
@RequestMapping("/prenotazione")
public class PrenotazioneRestController {
	private AbstractReservationService prenotService;

	@Autowired
	public PrenotazioneRestController(AbstractReservationService prenotService) {
		this.prenotService = prenotService;
	}

	@Value("${delete.failed}")
	private String deleteFail;

	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public ResponseEntity<List<PrenotazioneDto>> listaPrenotazioni() {
		List<Prenotazione> prenotazioni = prenotService.listaPrenotazioni();
		List<PrenotazioneDto> prenotazioniDto = prenotazioni.stream().map(PrenotazioneDto::fromPrenotazione)
				.collect(Collectors.toList());

		return new ResponseEntity<List<PrenotazioneDto>>(prenotazioniDto, HttpStatus.OK);
	}
	
	@GetMapping("/myReservation")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public ResponseEntity<List<PrenotazioneDto>> myListaPrenotazioni(@RequestParam int pageNum, @RequestParam int pageSize) throws EntityNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		Page<Prenotazione> prenotazioni = prenotService.listaPrenotazioniByUser(username, pageable);
		List<PrenotazioneDto> prenotazioniDto = prenotazioni.stream().map(PrenotazioneDto::fromPrenotazione)
				.collect(Collectors.toList());

		return new ResponseEntity<List<PrenotazioneDto>>(prenotazioniDto, HttpStatus.OK);
	}
	

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public ResponseEntity<?> getPrenotazioneById(@PathVariable long id) {
		Optional<Prenotazione> cercata = prenotService.getPrenotazioneById(id);
		if (cercata.isEmpty()) {
			return new ResponseEntity<>("Utente non trovato", HttpStatus.NOT_FOUND);
		}
		PrenotazioneDto prenotDto = PrenotazioneDto.fromPrenotazione(cercata.get());
		return new ResponseEntity<>(prenotDto, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> insertPrenotazione(@RequestBody PrenotazioneDto prenotazioneDto) {

		Prenotazione pre = prenotazioneDto.toPrenotazione();
		try {
			Prenotazione inserita = prenotService.insertPrenotazione(pre);
			return new ResponseEntity<>(PrenotazioneDto.fromPrenotazione(inserita), HttpStatus.CREATED);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (BusinessLogicException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/regole/{lingua}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public ResponseEntity<String> getRisposta(@PathVariable String lingua) {
		try {
			ElencoRegole regole = prenotService.getRulesByLang(lingua);
			return new ResponseEntity<String>(regole.getRegole(), HttpStatus.OK);
		} catch (BusinessLogicException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deletePrenotazioneById(@PathVariable long id) {
		try {
			prenotService.deleteReservationsById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (BusinessLogicException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updatePrenotazione(@PathVariable long id, @RequestBody PrenotazioneDto prenotazioneDto) {
		if (id != prenotazioneDto.getId()) {
			return new ResponseEntity<>("Gli id non coincidono", HttpStatus.BAD_REQUEST);
		}

		Prenotazione pre = prenotazioneDto.toPrenotazione();
		try {
			Prenotazione inserita = prenotService.updatePrenotazione(pre);
			return new ResponseEntity<>(PrenotazioneDto.fromPrenotazione(inserita), HttpStatus.CREATED);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (BusinessLogicException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
