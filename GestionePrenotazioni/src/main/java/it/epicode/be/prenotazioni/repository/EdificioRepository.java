package it.epicode.be.prenotazioni.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.epicode.be.prenotazioni.model.Edificio;

public interface EdificioRepository extends JpaRepository<Edificio, Long> {

}
