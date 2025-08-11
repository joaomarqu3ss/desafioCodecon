package br.com.cotiinformatica.domain.entities;


import lombok.Data;

@Data
public class Projects {

	private String name;
	
	private Boolean completed;
	
	private Team team;
}
