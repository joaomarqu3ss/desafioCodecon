package br.com.cotiinformatica.domain.entities;



import br.com.cotiinformatica.domain.enums.Action;
import lombok.Data;

@Data
public class Logs {
	
	private String date;
	
	private Action action;
	
}
