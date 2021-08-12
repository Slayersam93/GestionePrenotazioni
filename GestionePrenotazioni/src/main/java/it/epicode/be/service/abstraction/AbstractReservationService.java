package it.epicode.be.service.abstraction;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.epicode.be.exception.BusinessLogicException;
import it.epicode.be.exception.EntityNotFoundException;
import it.epicode.be.prenotazioni.controller.dto.ElencoRegole;
import it.epicode.be.prenotazioni.model.Prenotazione;

public interface AbstractReservationService {
	List<Prenotazione> listaPrenotazioni();

	Optional<Prenotazione> getPrenotazioneById(long id);

	Prenotazione insertPrenotazione(Prenotazione prenotazione) throws BusinessLogicException, EntityNotFoundException;

	ElencoRegole getRulesByLang(String lingua) throws BusinessLogicException;
	
	void deleteReservationsById (Long id) throws EntityNotFoundException;
	
	Prenotazione updatePrenotazione(Prenotazione prenotazione) throws BusinessLogicException, EntityNotFoundException;

	Page<Prenotazione> listaPrenotazioniByUser(String username, Pageable pageable);

}

