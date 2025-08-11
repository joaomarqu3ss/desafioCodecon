package br.com.cotiinformatica.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.cotiinformatica.domain.dtos.ActiveUsersPerDayResponse;
import br.com.cotiinformatica.domain.dtos.SuperuserResponse;
import br.com.cotiinformatica.domain.dtos.TeamInsightsResponse;
import br.com.cotiinformatica.domain.dtos.TopCountriesResponse;
import br.com.cotiinformatica.domain.dtos.UserCountResponse;
import br.com.cotiinformatica.domain.services.FileStorageService;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

	private final FileStorageService fileStorageService;
	
	public UsuarioController(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}
	
	
	@PostMapping("upload")
	public ResponseEntity<UserCountResponse> upload(@RequestParam("file") MultipartFile file) {
		return ResponseEntity.ok(fileStorageService.uploadFile(file));
	}

	@GetMapping("superusers")
	public ResponseEntity<SuperuserResponse> getSuperusers(){
		return ResponseEntity.ok(fileStorageService.getSuperUsers());
	}
	
	@GetMapping("top-countries")
	public ResponseEntity<TopCountriesResponse> getTopCountries() {
		return ResponseEntity.ok(fileStorageService.getTopCountries());
	}
	
	@GetMapping("team-insights")
	public ResponseEntity<TeamInsightsResponse> getTeamInsights() {
		return ResponseEntity.ok(fileStorageService.getTeamInsights());
	}
	
	@GetMapping("active-users-per-day")
	public ResponseEntity<ActiveUsersPerDayResponse> getActiveUsersPerDay() {
		return ResponseEntity.ok(fileStorageService.getLoginsPerDay());
	}
	
}
