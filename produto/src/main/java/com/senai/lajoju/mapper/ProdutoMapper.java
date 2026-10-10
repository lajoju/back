package com.senai.lajoju.mapper;

import com.senai.lajoju.dto.ProdutoRequest;
import com.senai.lajoju.dto.ProdutoResponse;
import com.senai.lajoju.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

	public Produto toModel(ProdutoRequest request) {
		return new Produto(request.name().trim(), request.tempoMedioMinutos(), request.price());
	}

	public ProdutoResponse toResponse(Produto product) {
		return new ProdutoResponse(
				product.getIdentificador(), product.getNome(), product.getTempoMedioMinutos(), product.getPreco());
	}

	public void update(Produto product, ProdutoRequest request) {
		product.update(request.name().trim(), request.tempoMedioMinutos(), request.price());
	}
}
