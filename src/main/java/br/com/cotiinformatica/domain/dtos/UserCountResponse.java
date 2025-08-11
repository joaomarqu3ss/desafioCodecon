package br.com.cotiinformatica.domain.dtos;

import lombok.Data;

@Data
public class UserCountResponse {

	private Integer status;
	private String message;
	private Integer users_count;
	
}
