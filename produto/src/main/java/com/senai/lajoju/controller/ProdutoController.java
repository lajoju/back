package com.senai.lajoju.controller;

import java.util.List;
import java.util.UUID;

import com.senai.lajoju.dto.ProdutoRequest;
import com.senai.lajoju.dto.ProdutoResponse;
import com.senai.lajoju.service.ProdutoService;
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
public class ProdutoController {

	private final ProdutoService productService;

	@GetMapping
	public List<ProdutoResponse> findAll() {
		return productService.findAll();
	}

	@GetMapping("/{id}")
	public ProdutoResponse findById(@PathVariable UUID id) {
		return productService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoResponse create(@Valid @RequestBody ProdutoRequest request) {
		return productService.create(request);
	}

	@PutMapping("/{id}")
	public ProdutoResponse update(@PathVariable UUID id, @Valid @RequestBody ProdutoRequest request) {
		return productService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID id) {
		productService.delete(id);
	}
}
