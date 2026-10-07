package com.senai.lajoju.controller;

import java.util.List;
import java.util.UUID;

import com.senai.lajoju.dto.RequisicaoCriacaoUsuario;
import com.senai.lajoju.dto.RespostaUsuario;
import com.senai.lajoju.dto.RequisicaoAtualizacaoUsuario;
import com.senai.lajoju.service.ServicoUsuario;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

	private final ServicoUsuario userService;

	@GetMapping
	public List<RespostaUsuario> findAll() {
		return userService.findAll();
	}

	@GetMapping("/{id}")
	public RespostaUsuario findById(@PathVariable UUID id) {
		return userService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RespostaUsuario create(@Valid @RequestBody RequisicaoCriacaoUsuario request) {
		return userService.create(request);
	}

	@PutMapping("/{id}")
	public RespostaUsuario update(@PathVariable UUID id, @Valid @RequestBody RequisicaoAtualizacaoUsuario request) {
		return userService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID id) {
		userService.delete(id);
	}
}
