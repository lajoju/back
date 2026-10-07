package com.senai.lajoju.controller;

import com.senai.lajoju.dto.LoginRequest;
import com.senai.lajoju.dto.RegisterRequest;
import com.senai.lajoju.dto.TokenResponse;
import com.senai.lajoju.dto.RespostaUsuario;
import com.senai.lajoju.service.AuthService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	@Operation(security = {})
	@ResponseStatus(HttpStatus.CREATED)
	public RespostaUsuario register(@Valid @RequestBody RegisterRequest request) {
		return authService.register(request);
	}

	@PostMapping("/login")
	@Operation(security = {})
	public TokenResponse login(@Valid @RequestBody LoginRequest request) {
		return authService.login(request);
	}
}
