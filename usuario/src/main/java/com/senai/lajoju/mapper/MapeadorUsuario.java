package com.senai.lajoju.mapper;

import com.senai.lajoju.dto.RespostaUsuario;
import com.senai.lajoju.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class MapeadorUsuario {

	public RespostaUsuario toResponse(Usuario user) {
		return new RespostaUsuario(user.getIdentificador(), user.getNome(), user.getEmail(), user.isFlgFuncionario());
	}
}
