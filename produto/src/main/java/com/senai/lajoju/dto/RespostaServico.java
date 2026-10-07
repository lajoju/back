package com.senai.lajoju.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record RespostaServico(UUID id, String name, int tempoMedioMinutos, BigDecimal price) {
}
