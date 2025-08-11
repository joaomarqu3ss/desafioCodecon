package br.com.cotiinformatica.domain.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class TopCountriesResponse {

	private Integer status;
	
	private LocalDateTime timestamp;
	
	private long execution_time_ms;
	
	private List<CountriesResponse> countries;
}
