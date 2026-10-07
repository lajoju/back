package com.senai.lajoju.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RequisicaoServico(
		@NotBlank @Size(max = 120) String name,
		@Positive int tempoMedioMinutos,
		@NotNull @DecimalMin("0.00") BigDecimal price) {
}
