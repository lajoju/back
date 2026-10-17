package com.senai.lajoju.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoResponse(UUID id, String name, String description, int tempoMedioMinutos, BigDecimal price) {
}
