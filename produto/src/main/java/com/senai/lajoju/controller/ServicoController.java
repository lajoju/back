package com.senai.lajoju.controller;

import java.util.List;
import java.util.UUID;

import com.senai.lajoju.dto.RequisicaoServico;
import com.senai.lajoju.dto.RespostaServico;
import com.senai.lajoju.service.CatalogoServicos;
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
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ServicoController {

	private final CatalogoServicos productService;

	@GetMapping
	public List<RespostaServico> findAll() {
		return productService.findAll();
	}

	@GetMapping("/{id}")
	public RespostaServico findById(@PathVariable UUID id) {
		return productService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RespostaServico create(@Valid @RequestBody RequisicaoServico request) {
		return productService.create(request);
	}

	@PutMapping("/{id}")
	public RespostaServico update(@PathVariable UUID id, @Valid @RequestBody RequisicaoServico request) {
		return productService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID id) {
		productService.delete(id);
	}
}
