package br.com.cotiinformatica.domain.entities;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class User {

	private UUID id;
	
	private String name;
	
	private Integer age;
	
	private Integer score;
	
	private Boolean active;
	
	private String country;
	
	private Team team;
	
	private List<Logs> logs;
}
