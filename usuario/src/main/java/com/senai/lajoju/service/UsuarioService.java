package com.senai.lajoju.service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.senai.lajoju.dto.RequisicaoCriacaoUsuario;
import com.senai.lajoju.dto.RespostaUsuario;
import com.senai.lajoju.dto.RequisicaoAtualizacaoUsuario;
import com.senai.lajoju.mapper.MapeadorUsuario;
import com.senai.lajoju.model.Usuario;
import com.senai.lajoju.repository.RepositorioUsuario;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicoUsuario {

	private final RepositorioUsuario users;
	private final MapeadorUsuario mapper;
	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public List<RespostaUsuario> findAll() {
		return users.findAll().stream().map(mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public RespostaUsuario findById(UUID id) {
		return mapper.toResponse(findUser(id));
	}

	@Transactional
	public RespostaUsuario create(RequisicaoCriacaoUsuario request) {
		String email = normalizeEmail(request.email());
		if (users.existsByEmail(email)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado.");
		}
		Usuario user = users.saveAndFlush(new Usuario(
				request.name().trim(), email, passwordEncoder.encode(request.password()), request.flgFuncionario()));
		return mapper.toResponse(user);
	}

	@Transactional
	public RespostaUsuario update(UUID id, RequisicaoAtualizacaoUsuario request) {
		Usuario user = findUser(id);
		String email = normalizeEmail(request.email());
		if (users.existsByEmailAndIdNot(email, id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado.");
		}
		user.update(request.name().trim(), email, passwordEncoder.encode(request.password()),
				request.flgFuncionario());
		return mapper.toResponse(users.save(user));
	}

	@Transactional
	public void delete(UUID id) {
		users.delete(findUser(id));
	}

	private Usuario findUser(UUID id) {
		return users.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
	}

	private static String normalizeEmail(String email) {
		return email.trim().toLowerCase(Locale.ROOT);
	}
}
