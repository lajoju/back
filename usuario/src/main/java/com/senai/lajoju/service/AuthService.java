package com.senai.lajoju.service;

import com.senai.lajoju.dto.LoginRequest;
import com.senai.lajoju.dto.RegisterRequest;
import com.senai.lajoju.dto.TokenResponse;
import com.senai.lajoju.dto.RespostaUsuario;
import com.senai.lajoju.mapper.MapeadorUsuario;
import com.senai.lajoju.model.Usuario;
import com.senai.lajoju.repository.RepositorioUsuario;
import com.senai.lajoju.security.JwtUtil;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final RepositorioUsuario users;
	private final MapeadorUsuario userMapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Transactional
	public RespostaUsuario register(RegisterRequest request) {
		String email = normalizeEmail(request.email());
		if (users.existsByEmail(email)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado.");
		}
		if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha excede o limite suportado.");
		}

		Usuario user = users.saveAndFlush(
				new Usuario(request.name().trim(), email, passwordEncoder.encode(request.password()), false));
		return userMapper.toResponse(user);
	}

	@Transactional(readOnly = true)
	public TokenResponse login(LoginRequest request) {
		if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas.");
		}
		String email = normalizeEmail(request.email());
		Usuario user = users.findByEmail(email)
				.filter(account -> passwordEncoder.matches(request.password(), account.getHashSenha()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas."));

		return jwtUtil.generateToken(user);
	}

	private static String normalizeEmail(String email) {
		return email.trim().toLowerCase(Locale.ROOT);
	}
}
