package it.epicode.be.prenotazioni.repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.epicode.be.prenotazioni.model.Postazione;
import it.epicode.be.prenotazioni.model.Prenotazione;
import it.epicode.be.prenotazioni.model.User;
;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
public List<Prenotazione> findByUtenteId(long id);
	
	@Query("SELECT p FROM Prenotazione p WHERE p.utente.id= :idUtenteParam AND p.dataPrenotata= :dataPrenotataParam")
	public List<Prenotazione> trovaPrenotazioniPerData(long idUtenteParam, LocalDate dataPrenotataParam);
	
	public Page<Prenotazione>findByUtenteUsername(String username, Pageable pageable);
	
	public Page<Prenotazione> findByUtenteAndDataPrenotata(User u, LocalDate dataPrenotata, Pageable pageable);
	
	public Page<Prenotazione> findByPostazioneAndDataPrenotata(Postazione postazione, LocalDate dataPrenotata, Pageable pageable);

}

