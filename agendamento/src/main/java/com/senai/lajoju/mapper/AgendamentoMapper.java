package com.senai.lajoju.mapper;

import com.senai.lajoju.dto.AgendamentoResponse;
import com.senai.lajoju.model.Agendamento;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMapper {

	public AgendamentoResponse toResponse(Agendamento appointment) {
		return new AgendamentoResponse(
				appointment.getIdentificador(),
				appointment.getUsuarioId(),
				appointment.getFuncionarioId(),
				appointment.getProdutoId(),
				appointment.getData(),
				appointment.getHoraInicio(),
				appointment.getHoraFim());
	}
}
