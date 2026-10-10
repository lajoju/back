package com.senai.lajoju.dto;

import java.util.UUID;

public record UsuarioRemotoResponse(UUID id, String name, String email, boolean flgFuncionario) {
}
