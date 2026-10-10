package com.senai.lajoju.service;

import java.util.UUID;

import com.senai.lajoju.dto.ProdutoRemotoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ClienteProdutoService {

	private final RestClient productClient;

	public ClienteProdutoService(@Value("${services.produto.base-url}") String productBaseUrl) {
		this.productClient = RestClient.builder().baseUrl(productBaseUrl).build();
	}

	public ProdutoRemotoResponse getProduct(UUID id, String bearerToken) {
		return productClient.get()
				.uri("/produtos/{id}", id)
				.header(HttpHeaders.AUTHORIZATION, bearerToken)
				.retrieve()
				.body(ProdutoRemotoResponse.class);
	}
}
