package it.epicode.be.prenotazioni.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.epicode.be.prenotazioni.controller.dto.PostazioneDto;
import it.epicode.be.prenotazioni.model.Postazione;
import it.epicode.be.prenotazioni.model.TipoPostazione;
import it.epicode.be.service.abstraction.AbstractPostazioneService;

@RestController
@RequestMapping("/postazione")
public class PostazioneRestController {
	@Autowired
	private AbstractPostazioneService postazioneService;
	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public ResponseEntity<Page<PostazioneDto>> findPostazioni(@RequestParam TipoPostazione tipo,
			@RequestParam String citta, @RequestParam int pageNum, @RequestParam int pageSize,
			@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Optional<LocalDate> dataUtilizzo) {

		Pageable pageable = PageRequest.of(pageNum, pageSize);
		Page<Postazione> paginaPostazioni = null;
		if (dataUtilizzo.isEmpty()) {
			paginaPostazioni = postazioneService.findByTypeAndCity(tipo, citta, pageable);
			System.out.println("Senza data");
		} else {
			paginaPostazioni = postazioneService.findAvailableByTypeAndCity(tipo, citta, dataUtilizzo.get(), pageable);
			System.out.println("Con data");
		}

		Page<PostazioneDto> paginaDto = paginaPostazioni.map(PostazioneDto::fromPostazione);

		return new ResponseEntity<Page<PostazioneDto>>(paginaDto, HttpStatus.OK);

	}
	
}
