package br.com.cotiinformatica.domain.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cotiinformatica.configurations.FileStorageConfig;
import br.com.cotiinformatica.domain.dtos.ActiveUsersPerDayResponse;
import br.com.cotiinformatica.domain.dtos.CountriesResponse;
import br.com.cotiinformatica.domain.dtos.LoginsResponse;
import br.com.cotiinformatica.domain.dtos.SuperuserResponse;
import br.com.cotiinformatica.domain.dtos.TeamInsightsResponse;
import br.com.cotiinformatica.domain.dtos.TeamStatsResponse;
import br.com.cotiinformatica.domain.dtos.TopCountriesResponse;
import br.com.cotiinformatica.domain.dtos.UserCountResponse;
import br.com.cotiinformatica.domain.entities.Logs;
import br.com.cotiinformatica.domain.entities.User;

@Service
public class FileStorageService {

	private final ObjectMapper objectMapper;
	private final Path fileStorageLocation;

	private List<User> users = new ArrayList<>();

	public FileStorageService(FileStorageConfig fileStorageConfig, ObjectMapper objectMapper) {
		this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
		this.objectMapper = objectMapper;

		try {
			Files.createDirectories(this.fileStorageLocation);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public UserCountResponse uploadFile(MultipartFile file) {

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		var response = new UserCountResponse();

		try {

			Path targetLocation = fileStorageLocation.resolve(fileName);
			file.transferTo(targetLocation);
			Path filePath = this.fileStorageLocation.resolve(file.getOriginalFilename()).normalize();

			users = objectMapper.readValue(new File(filePath.toString()), new TypeReference<List<User>>() {
			});

			response.setStatus(HttpStatus.OK.value());
			response.setMessage("Arquivo recebido com sucesso!");
			response.setUsers_count(users.size());

		}

		catch (IOException ex) {

			ex.printStackTrace();

		}

		return response;

	}

	public SuperuserResponse getSuperUsers() {

		long start = System.currentTimeMillis();

		var resp = new SuperuserResponse();

		resp.setStatus(HttpStatus.OK.value());
		resp.setTimestamp(LocalDateTime.now());
		resp.setData(users.stream().filter(user -> user.getScore() >= 900 && user.getActive() == true)
				.collect(Collectors.toList()));
		resp.setExecution_time_ms((System.currentTimeMillis() - start));

		return resp;
	}

	public TopCountriesResponse getTopCountries() {

		long start = System.currentTimeMillis();

		var resp = new TopCountriesResponse();

		Map<String, List<User>> countriesByUser = users.stream()
				.filter(u -> u.getScore() >= 900 && u.getActive() == true && u.getCountry() != null)
				.collect(Collectors.groupingBy(u -> u.getCountry()));
		
		List<CountriesResponse> countries = new ArrayList<>();
		
		for(Map.Entry<String, List<User>> country : countriesByUser.entrySet()) {
			
			String countryName = country.getKey();
			List<User> countryUsers = country.getValue();
			
			var totalUsers = countryUsers.size();
			
			countries.add(new CountriesResponse(
					countryName,
					totalUsers
					));
		}
		
		List<CountriesResponse> top5 = countries.stream()
				.sorted(Comparator.comparingLong(CountriesResponse::getTotal).reversed())
				.limit(5)
				.collect(Collectors.toList());
				
		resp.setCountries(top5);

		resp.setTimestamp(LocalDateTime.now());

		resp.setStatus(HttpStatus.OK.value());

		resp.setExecution_time_ms((System.currentTimeMillis() - start));

		return resp;

	}

	public TeamInsightsResponse getTeamInsights() {

		long start = System.currentTimeMillis();

		var resp = new TeamInsightsResponse();
		
		Map<String, List<User>> teams = users.stream().filter(u -> u.getTeam() != null && u.getTeam().getName() != null)
				.collect(Collectors.groupingBy(u -> u.getTeam().getName()));
		
		List<TeamStatsResponse> stats = new ArrayList<>();

		for(Map.Entry<String, List<User>> team : teams.entrySet()) {
			var name = team.getKey();
			List<User> teamUsers = team.getValue();
			
			var totalUsers = teamUsers.size();
			var totalLeaders = teamUsers.stream()
					.filter(u -> Boolean.TRUE.equals(u.getTeam().getLeader()))
					.count();
			var totalProjects = teamUsers.stream().flatMap(u -> Optional.ofNullable(u.getTeam().getProjects())
					.stream().flatMap(List::stream))
					.filter(p -> Boolean.TRUE.equals(p.getCompleted()))
					.count();
			
			var activeUsers = teamUsers.stream()
					.filter(u -> Boolean.TRUE.equals(u.getActive()))
					.count();
			
			double activesPercentage = totalUsers > 0 ? (activeUsers * 100.00) / totalUsers : 0.0;
			
			String parse = String.format("%.1f", activesPercentage) + "%";
			
			stats.add(
					new TeamStatsResponse(
					name, 
					totalUsers, 
					totalLeaders, 
					totalProjects, 
					parse));
		}
		
		resp.setTimestamp(LocalDateTime.now());

		resp.setStatus(HttpStatus.OK.value());
		
		resp.setTeamStats(stats);

		resp.setExecution_time_ms((System.currentTimeMillis() - start));

		return resp;
	}
	
	public ActiveUsersPerDayResponse getLoginsPerDay() {
		
		long start = System.currentTimeMillis();
		
		var resp = new ActiveUsersPerDayResponse();
		
		Map<Object, List<Logs>> logs = users.stream().filter(u -> Boolean.TRUE.equals(u.getActive()) && u.getLogs() != null)
				.flatMap(l -> Optional.ofNullable(l.getLogs()).stream().flatMap(List::stream))
				.collect(Collectors.groupingBy(l -> l.getDate()));
		
		List<LoginsResponse> loginsPerDay = new ArrayList<>();
		
		for(Entry<Object, List<Logs>> logins : logs.entrySet()) {
			
			String dates = (String) logins.getKey();
			
			List<Logs> logsPerUsers = logins.getValue();
			
			var activeUsers = logsPerUsers.size();
						
			loginsPerDay.add(new LoginsResponse(
					dates,
					activeUsers
					));
		}
		
		resp.setLogins(loginsPerDay);
		
		resp.setStatus(HttpStatus.OK.value());
		resp.setTimestamp(LocalDateTime.now());
		resp.setExecution_time_ms(System.currentTimeMillis() - start);
		
		return resp;
	}

}
