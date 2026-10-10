package com.senai.lajoju.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AgendamentoRequest(
		@NotNull UUID usuarioId,
		@NotNull UUID funcionarioId,
		@NotNull UUID produtoId,
		@NotNull LocalDate data,
		@NotNull LocalTime horaInicio) {
}
