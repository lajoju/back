package com.senai.lajoju.controller;

import java.util.UUID;

import com.senai.lajoju.dto.AtualizacaoUsuarioRequest;
import com.senai.lajoju.dto.FuncionarioStatusResponse;
import com.senai.lajoju.dto.UsuarioResponse;
import com.senai.lajoju.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioService userService;

	@GetMapping("/me")
	public UsuarioResponse findMyProfile(@AuthenticationPrincipal Jwt jwt) {
		return userService.findById(authenticatedUserId(jwt));
	}

	@PutMapping("/me")
	public UsuarioResponse updateMyProfile(
			@Valid @RequestBody AtualizacaoUsuarioRequest request,
			@AuthenticationPrincipal Jwt jwt) {
		return userService.updateProfile(authenticatedUserId(jwt), request);
	}

	@GetMapping("/{id}/funcionario")
	public FuncionarioStatusResponse findEmployeeStatus(@PathVariable UUID id) {
		return userService.findEmployeeStatus(id);
	}

	private UUID authenticatedUserId(Jwt jwt) {
		try {
			return UUID.fromString(jwt.getClaimAsString("uid"));
		} catch (IllegalArgumentException | NullPointerException exception) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token sem identificador de usuário válido.",
					exception);
		}
	}
}
