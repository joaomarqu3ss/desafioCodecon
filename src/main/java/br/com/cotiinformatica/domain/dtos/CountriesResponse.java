package br.com.cotiinformatica.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CountriesResponse {

	private String name;
	
	private long total;

}
