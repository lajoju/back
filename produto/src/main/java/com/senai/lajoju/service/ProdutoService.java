package com.senai.lajoju.service;

import java.util.List;
import java.util.UUID;

import com.senai.lajoju.dto.ProdutoRequest;
import com.senai.lajoju.dto.ProdutoResponse;
import com.senai.lajoju.mapper.ProdutoMapper;
import com.senai.lajoju.model.Produto;
import com.senai.lajoju.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdutoService {

	private final ProdutoRepository products;
	private final ProdutoMapper mapper;

	@Transactional(readOnly = true)
	public List<ProdutoResponse> findAll() {
		return products.findAll().stream().map(mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public ProdutoResponse findById(UUID id) {
		return mapper.toResponse(findProduct(id));
	}

	@Transactional
	public ProdutoResponse create(ProdutoRequest request) {
		return mapper.toResponse(products.saveAndFlush(mapper.toModel(request)));
	}

	@Transactional
	public ProdutoResponse update(UUID id, ProdutoRequest request) {
		Produto product = findProduct(id);
		mapper.update(product, request);
		return mapper.toResponse(products.save(product));
	}

	@Transactional
	public void delete(UUID id) {
		products.delete(findProduct(id));
	}

	private Produto findProduct(UUID id) {
		return products.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado."));
	}
}
