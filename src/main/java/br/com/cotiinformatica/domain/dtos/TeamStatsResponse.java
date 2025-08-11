package br.com.cotiinformatica.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TeamStatsResponse {

	private String name;
	
	private long totalUsers;
	
	private long totalLeaders;
	
	private long totalProjectsfinished;
	
	private String active_percentage;
}
