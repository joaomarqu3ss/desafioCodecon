package br.com.cotiinformatica.domain.dtos;

import java.time.LocalDateTime;
import java.util.List;

import br.com.cotiinformatica.domain.entities.User;
import lombok.Data;

@Data
public class SuperuserResponse {

	private Integer status;
	
	private LocalDateTime timestamp;
	
	private long execution_time_ms;
	
	private List<User> data;
}
