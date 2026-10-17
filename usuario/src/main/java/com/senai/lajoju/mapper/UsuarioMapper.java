package com.senai.lajoju.mapper;

import com.senai.lajoju.dto.UsuarioResponse;
import com.senai.lajoju.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

	public UsuarioResponse toResponse(Usuario user) {
		return new UsuarioResponse(user.getIdentificador(), user.getNome(), user.getEmail(), user.isFlgFuncionario());
	}
}
