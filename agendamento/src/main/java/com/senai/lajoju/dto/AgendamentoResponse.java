package com.senai.lajoju.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AgendamentoResponse(
		UUID id,
		UUID usuarioId,
		UUID funcionarioId,
		UUID produtoId,
		LocalDate data,
		LocalTime horaInicio,
		LocalTime horaFim) {
}
