package com.senai.lajoju;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import com.senai.lajoju.dto.ProdutoRequest;
import com.senai.lajoju.dto.ProdutoResponse;
import com.senai.lajoju.mapper.ProdutoMapper;
import com.senai.lajoju.model.Produto;
import org.junit.jupiter.api.Test;

class ProdutoMapperTests {

	private final ProdutoMapper mapper = new ProdutoMapper();

	@Test
	void mapsDescriptionWhenCreatingAndReturningService() {
		ProdutoRequest request = new ProdutoRequest(
				" Corte ", "  Descrição do serviço  ", 45, new BigDecimal("85.00"));

		Produto product = mapper.toModel(request);
		ProdutoResponse response = mapper.toResponse(product);

		assertEquals("Corte", product.getNome());
		assertEquals("Descrição do serviço", product.getDescricao());
		assertEquals("Descrição do serviço", response.description());
		assertEquals(45, response.tempoMedioMinutos());
	}

	@Test
	void updatesServiceDescription() {
		Produto product = new Produto("Corte", "Descrição antiga", 30, new BigDecimal("55.00"));

		mapper.update(product, new ProdutoRequest(
				"Corte", " Nova descrição ", 45, new BigDecimal("85.00")));

		assertEquals("Nova descrição", product.getDescricao());
		assertEquals(45, product.getTempoMedioMinutos());
		assertEquals(new BigDecimal("85.00"), product.getPreco());
	}
}
