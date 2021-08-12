package it.epicode.be.service.abstraction;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.epicode.be.prenotazioni.model.Postazione;
import it.epicode.be.prenotazioni.model.TipoPostazione;

public interface AbstractPostazioneService {
	public Page<Postazione> findByTypeAndCity(TipoPostazione tipo, String citta, Pageable pageable);
	public Page<Postazione> findAvailableByTypeAndCity(TipoPostazione tipo, String citta,LocalDate dataUtilizzo, Pageable pageable);
	

}
