package com.senai.lajoju.service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;

import com.senai.lajoju.dto.AtualizacaoUsuarioRequest;
import com.senai.lajoju.dto.FuncionarioStatusResponse;
import com.senai.lajoju.dto.UsuarioResponse;
import com.senai.lajoju.mapper.UsuarioMapper;
import com.senai.lajoju.model.Usuario;
import com.senai.lajoju.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

	private final UsuarioRepository users;
	private final UsuarioMapper mapper;
	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public UsuarioResponse findById(UUID id) {
		return mapper.toResponse(findUser(id));
	}

	@Transactional(readOnly = true)
	public FuncionarioStatusResponse findEmployeeStatus(UUID id) {
		return new FuncionarioStatusResponse(findUser(id).isFlgFuncionario());
	}

	@Transactional
	public UsuarioResponse updateProfile(UUID id, AtualizacaoUsuarioRequest request) {
		Usuario user = findUser(id);
		String email = normalizeEmail(request.email());
		if (users.existsByEmailAndIdNot(email, id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado.");
		}
		if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha excede o limite suportado.");
		}
		user.updateProfile(request.name().trim(), email, passwordEncoder.encode(request.password()));
		return mapper.toResponse(users.save(user));
	}

	private Usuario findUser(UUID id) {
		return users.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
	}

	private static String normalizeEmail(String email) {
		return email.trim().toLowerCase(Locale.ROOT);
	}
}
