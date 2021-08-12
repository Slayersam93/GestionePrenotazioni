package it.epicode.be.prenotazioni.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.epicode.be.prenotazioni.model.User;

public interface UtenteRepository extends JpaRepository<User, Long>{
	

}
