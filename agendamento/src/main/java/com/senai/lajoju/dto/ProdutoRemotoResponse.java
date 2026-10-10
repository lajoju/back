package com.senai.lajoju.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoRemotoResponse(UUID id, String name, int tempoMedioMinutos, BigDecimal price) {
}
