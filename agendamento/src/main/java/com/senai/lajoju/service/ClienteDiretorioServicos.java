package com.senai.lajoju.service;

import java.util.UUID;

import com.senai.lajoju.dto.RespostaServicoRemoto;
import com.senai.lajoju.dto.RespostaUsuarioRemoto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ClienteDiretorioServicos {

	private final RestClient userClient;
	private final RestClient productClient;

	public ClienteDiretorioServicos(
			@Value("${services.usuario.base-url}") String userBaseUrl,
			@Value("${services.produto.base-url}") String productBaseUrl) {
		this.userClient = RestClient.builder().baseUrl(userBaseUrl).build();
		this.productClient = RestClient.builder().baseUrl(productBaseUrl).build();
	}

	public RespostaUsuarioRemoto getUser(UUID id, String bearerToken) {
		return userClient.get()
				.uri("/usuarios/{id}", id)
				.header(HttpHeaders.AUTHORIZATION, bearerToken)
				.retrieve()
				.body(RespostaUsuarioRemoto.class);
	}

	public RespostaServicoRemoto getProduct(UUID id, String bearerToken) {
		return productClient.get()
				.uri("/produtos/{id}", id)
				.header(HttpHeaders.AUTHORIZATION, bearerToken)
				.retrieve()
				.body(RespostaServicoRemoto.class);
	}
}
