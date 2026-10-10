package com.senai.lajoju.controller;

import java.util.List;
import java.util.UUID;

import com.senai.lajoju.dto.AgendamentoRequest;
import com.senai.lajoju.dto.AgendamentoResponse;
import com.senai.lajoju.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

	private final AgendamentoService appointmentService;

	@GetMapping
	public List<AgendamentoResponse> findAll() {
		return appointmentService.findAll();
	}

	@GetMapping("/{id}")
	public AgendamentoResponse findById(@PathVariable UUID id) {
		return appointmentService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AgendamentoResponse create(
			@Valid @RequestBody AgendamentoRequest request,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
			@AuthenticationPrincipal Jwt jwt) {
		return appointmentService.create(request, bearerToken, jwt.getSubject());
	}

	@PutMapping("/{id}")
	public AgendamentoResponse update(
			@PathVariable UUID id,
			@Valid @RequestBody AgendamentoRequest request,
			@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
			@AuthenticationPrincipal Jwt jwt) {
		return appointmentService.update(id, request, bearerToken, jwt.getSubject());
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID id) {
		appointmentService.delete(id);
	}
}
