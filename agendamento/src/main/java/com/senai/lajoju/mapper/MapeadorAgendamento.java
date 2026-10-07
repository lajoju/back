package com.senai.lajoju.mapper;

import com.senai.lajoju.dto.RespostaAgendamento;
import com.senai.lajoju.model.Agendamento;
import org.springframework.stereotype.Component;

@Component
public class MapeadorAgendamento {

	public RespostaAgendamento toResponse(Agendamento appointment) {
		return new RespostaAgendamento(
				appointment.getIdentificador(),
				appointment.getUsuarioId(),
				appointment.getFuncionarioId(),
				appointment.getProdutoId(),
				appointment.getData(),
				appointment.getHoraInicio(),
				appointment.getHoraFim());
	}
}
