package com.senai.lajoju.service;

import java.util.UUID;

import com.senai.lajoju.dto.UsuarioRemotoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ClienteUsuarioService {

	private final RestClient userClient;

	public ClienteUsuarioService(@Value("${services.usuario.base-url}") String userBaseUrl) {
		this.userClient = RestClient.builder().baseUrl(userBaseUrl).build();
	}

	public UsuarioRemotoResponse getUser(UUID id, String bearerToken) {
		return userClient.get()
				.uri("/usuarios/{id}", id)
				.header(HttpHeaders.AUTHORIZATION, bearerToken)
				.retrieve()
				.body(UsuarioRemotoResponse.class);
	}
}
