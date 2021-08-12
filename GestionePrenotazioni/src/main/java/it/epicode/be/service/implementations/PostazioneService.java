package it.epicode.be.service.implementations;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.epicode.be.prenotazioni.model.Postazione;
import it.epicode.be.prenotazioni.model.TipoPostazione;
import it.epicode.be.prenotazioni.repository.PostazioneRepository;
import it.epicode.be.service.abstraction.AbstractPostazioneService;

@Service
public class PostazioneService implements AbstractPostazioneService {
	@Autowired
	private PostazioneRepository postazioneRepo;

	@Override
	public Page<Postazione> findByTypeAndCity(TipoPostazione tipo, String citta, Pageable pageable) {
		return postazioneRepo.findByTipoAndEdificioCitta(tipo, citta, pageable);
	}

	@Override
	public Page<Postazione> findAvailableByTypeAndCity(TipoPostazione tipo, String citta, LocalDate dataUtilizzo,
			Pageable pageable) {
		return postazioneRepo.findAvailableByTypeAndCity(citta, tipo, dataUtilizzo, pageable);
	}

}
