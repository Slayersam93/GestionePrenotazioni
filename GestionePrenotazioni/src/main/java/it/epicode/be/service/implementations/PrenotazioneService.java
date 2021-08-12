package it.epicode.be.service.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.epicode.be.exception.BusinessLogicException;
import it.epicode.be.exception.EntityNotFoundException;
import it.epicode.be.prenotazioni.controller.dto.ElencoRegole;
import it.epicode.be.prenotazioni.model.Postazione;
import it.epicode.be.prenotazioni.model.Prenotazione;
import it.epicode.be.prenotazioni.model.TipoPostazione;
import it.epicode.be.prenotazioni.model.User;
import it.epicode.be.prenotazioni.repository.PostazioneRepository;
import it.epicode.be.prenotazioni.repository.PrenotazioneRepository;
import it.epicode.be.prenotazioni.repository.UtenteRepository;
import it.epicode.be.service.abstraction.AbstractReservationService;
@Service
public class PrenotazioneService implements AbstractReservationService {
	private PrenotazioneRepository prenotRepo;
	private UtenteRepository utenteRepo;
	private PostazioneRepository postRepo;

	@Value("${exception.lessthantwodays}")
	private String lessThanTwoDays;

	@Value("${exception.entitynotfound}")
	private String entityNotFound;

	@Value("${exception.userhasreservation}")
	private String userHasReservation;

	@Value("${exception.workspacealreadyreserved}")
	private String workSpaceAlreadyReserved;

	@Value("${rules.ita}")
	private String rulesIta;

	@Value("${rules.eng}")
	private String rulesEng;

	@Autowired
	public PrenotazioneService(PrenotazioneRepository prenotRepo, UtenteRepository utenteRepo,
			PostazioneRepository postRepo) {

		this.prenotRepo = prenotRepo;
		this.utenteRepo = utenteRepo;
		this.postRepo = postRepo;
	}

	@Override
	public List<Prenotazione> listaPrenotazioni() {
		List<Prenotazione> prenotazioni = prenotRepo.findAll();

		return prenotazioni;
	}
	@Override
	public Page <Prenotazione> listaPrenotazioniByUser(String username, Pageable pageable) {
		Page <Prenotazione> prenotazioni = prenotRepo.findByUtenteUsername(username , pageable);
		
		return prenotazioni;
	}


	@Override
	public Optional<Prenotazione> getPrenotazioneById(long id) {
		Optional<Prenotazione> cercata = prenotRepo.findById(id);

		return cercata;
	}
	private boolean isWorkSpaceAvaliable(Postazione p, LocalDate data) {
		Pageable pageable = PageRequest.of(0, 1);
		Page<Prenotazione> paginaPrenotazione = prenotRepo.findByPostazioneAndDataPrenotata(p, data, pageable);

		return paginaPrenotazione.isEmpty();
	}

	private boolean userHasReservationForDay(User u, LocalDate date, long idres) {
		Pageable pageable = PageRequest.of(0, 1);
		Page<Prenotazione> paginaPrenotazione = prenotRepo.findByUtenteAndDataPrenotata(u, date, pageable);
		if (idres == 0) {
		return paginaPrenotazione.hasContent();
		}
		if (paginaPrenotazione.isEmpty()) {
			return false;
		}
		return paginaPrenotazione.get().findFirst().get().getId() != idres;
	}
		

	private boolean diffInDaysLessThan(int numDays, LocalDate firstDate, LocalDate secondDate) {
		LocalDate numDaysBefore = secondDate.minusDays(numDays);
		return firstDate.isAfter(numDaysBefore);
	}

	@Override
	public Prenotazione insertPrenotazione(Prenotazione prenotazione)
			throws BusinessLogicException, EntityNotFoundException {
		applicaRegoleBusiness(prenotazione);
		Prenotazione saved = prenotRepo.save(prenotazione);

		return saved;
	}

	@Override
	public ElencoRegole getRulesByLang(String lingua) throws BusinessLogicException {
		ElencoRegole risposta = new ElencoRegole();
		risposta.setLingua(lingua);

		if (lingua.equalsIgnoreCase("ita")) {
			risposta.setRegole(rulesIta);
		} else if (lingua.equalsIgnoreCase("eng")) {
			risposta.setRegole(rulesEng);
		} else {
			throw new BusinessLogicException("Lingua non supportata");
		}

		return risposta;
	}
	public Page<Postazione> getPostazioneByTipoAndCitta(TipoPostazione tipo, String citta, Pageable pageable){
		
		return postRepo.findByTipoAndEdificioCitta(tipo, citta,pageable);
	}

	@Override
	public void deleteReservationsById(Long id) throws EntityNotFoundException {
		Optional<Prenotazione> cercata = prenotRepo.findById(id);
		if (cercata.isEmpty()) {
			throw new EntityNotFoundException(entityNotFound, Prenotazione.class);
		}
		prenotRepo.deleteById(id);
	}

	@Override
	public Prenotazione updatePrenotazione(Prenotazione prenotazione)
			throws BusinessLogicException, EntityNotFoundException {
		Optional<Prenotazione> cercata = prenotRepo.findById(prenotazione.getId());
		if (cercata.isEmpty()) {
			throw new EntityNotFoundException(entityNotFound, Prenotazione.class);
		}
		applicaRegoleBusiness(prenotazione);
		Prenotazione saved = prenotRepo.save(prenotazione);

		return saved;
	}
	
	private void applicaRegoleBusiness (Prenotazione prenotazione)
			throws BusinessLogicException, EntityNotFoundException {
		if (diffInDaysLessThan(2, prenotazione.getDataPrenotazione(), prenotazione.getDataPrenotata())) {
			throw new BusinessLogicException(lessThanTwoDays);
		}

		Optional<User> u = utenteRepo.findById(prenotazione.getUtente().getId());
		if (u.isEmpty()) {
			throw new EntityNotFoundException(entityNotFound, User.class);
		}
		Optional<Postazione> p = postRepo.findById(prenotazione.getPostazione().getId());
		if (p.isEmpty()) {
			throw new EntityNotFoundException(entityNotFound, Postazione.class);
		}

		if (userHasReservationForDay(u.get(), prenotazione.getDataPrenotata(),prenotazione.getId())) {
			throw new BusinessLogicException(userHasReservation);
		}

		if (!isWorkSpaceAvaliable(p.get(), prenotazione.getDataPrenotata())) {
			throw new BusinessLogicException(workSpaceAlreadyReserved);
		}
		
	}

}
