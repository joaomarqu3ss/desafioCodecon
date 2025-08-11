package br.com.cotiinformatica.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginsResponse {

	private String date;
	
	private long total;
	
}
