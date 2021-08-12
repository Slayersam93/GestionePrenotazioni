package it.epicode.be.prenotazioni.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Prenotazione {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private User utente;
	@ManyToOne
	private Postazione postazione;
	// data del giorno della prenotazione
	private LocalDate dataPrenotata;
	//data di quando ho effettuato la prenotazione
	private LocalDate dataPrenotazione;
	
	

}
