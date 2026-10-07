package com.senai.lajoju.service;

import java.util.List;
import java.util.UUID;

import com.senai.lajoju.dto.RequisicaoServico;
import com.senai.lajoju.dto.RespostaServico;
import com.senai.lajoju.mapper.MapeadorServico;
import com.senai.lajoju.model.Servico;
import com.senai.lajoju.repository.RepositorioServico;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CatalogoServicos {

	private final RepositorioServico products;
	private final MapeadorServico mapper;

	@Transactional(readOnly = true)
	public List<RespostaServico> findAll() {
		return products.findAll().stream().map(mapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public RespostaServico findById(UUID id) {
		return mapper.toResponse(findProduct(id));
	}

	@Transactional
	public RespostaServico create(RequisicaoServico request) {
		return mapper.toResponse(products.saveAndFlush(mapper.toModel(request)));
	}

	@Transactional
	public RespostaServico update(UUID id, RequisicaoServico request) {
		Servico product = findProduct(id);
		mapper.update(product, request);
		return mapper.toResponse(products.save(product));
	}

	@Transactional
	public void delete(UUID id) {
		products.delete(findProduct(id));
	}

	private Servico findProduct(UUID id) {
		return products.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado."));
	}
}
