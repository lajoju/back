package com.senai.lajoju.mapper;

import com.senai.lajoju.dto.RequisicaoServico;
import com.senai.lajoju.dto.RespostaServico;
import com.senai.lajoju.model.Servico;
import org.springframework.stereotype.Component;

@Component
public class MapeadorServico {

	public Servico toModel(RequisicaoServico request) {
		return new Servico(request.name().trim(), request.tempoMedioMinutos(), request.price());
	}

	public RespostaServico toResponse(Servico product) {
		return new RespostaServico(
				product.getIdentificador(), product.getNome(), product.getTempoMedioMinutos(), product.getPreco());
	}

	public void update(Servico product, RequisicaoServico request) {
		product.update(request.name().trim(), request.tempoMedioMinutos(), request.price());
	}
}
