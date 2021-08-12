package it.epicode.be.prenotazioni.controller.dto;

import it.epicode.be.prenotazioni.model.Edificio;
import it.epicode.be.prenotazioni.model.Postazione;
import it.epicode.be.prenotazioni.model.TipoPostazione;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostazioneDto {
	private long id;
	private String descrizione;
	private TipoPostazione tipo;
	private int numMaxOccupanti;
	private long edificioId;
	private String edificioNome;
	private String edificioCitta;

	public static PostazioneDto fromPostazione(Postazione p) {
		return new PostazioneDto(p.getId(), p.getDescrizione(), p.getTipo(), p.getMaxPartecipanti(),
				p.getEdificio().getId(), p.getEdificio().getNome(), p.getEdificio().getCitta());
	}
	
	public Postazione toPostazione() {
		Postazione p = new Postazione();
		p.setId(id);
		p.setDescrizione(descrizione);
		p.setTipo(tipo);
		p.setMaxPartecipanti(numMaxOccupanti);
		
		Edificio ed = new Edificio();
		ed.setId(edificioId);
		ed.setNome(edificioNome);
		ed.setCitta(edificioCitta);
		
		p.setEdificio(ed);
		
		return p;
	}

}
