package br.com.cotiinformatica.domain.entities;

import java.util.List;

import lombok.Data;

@Data
public class Team {
	
	private String name;
	
	private Boolean leader;
	
	private User user;
	
	private List<Projects> projects;
}
