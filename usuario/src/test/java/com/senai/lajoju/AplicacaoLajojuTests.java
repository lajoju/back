package com.senai.lajoju;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
		"security.jwt.private-key-location=classpath:jwt-test-private.pem",
		"security.jwt.public-key-location=classpath:jwt-test-public.pem",
		"spring.datasource.url=jdbc:h2:mem:lajoju;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.flyway.enabled=false"
})
@AutoConfigureMockMvc
class AplicacaoLajojuTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void rejectsProtectedRequestsWithoutBearerToken() throws Exception {
		mockMvc.perform(get("/protected-resource"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void exposesOpenApiDocumentationWithoutBearerToken() throws Exception {
		String openApi = mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		assertTrue(openApi.contains("\"Bearer Authentication\""));
		assertTrue(openApi.contains("\"scheme\":\"bearer\""));
	}

	@Test
	void permitsRegistrationAndLoginAndAcceptsIssuedBearerToken() throws Exception {
		mockMvc.perform(post("/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"Test User","email":"user@example.com","password":"correct-horse-battery"}
								"""))
				.andExpect(status().isCreated());

		String loginResponse = mockMvc.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"email":"user@example.com","password":"correct-horse-battery"}
								"""))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		String accessToken = objectMapper
				.readTree(loginResponse)
				.get("accessToken")
				.asText();
		String jwtHeader = new String(
				Base64.getUrlDecoder().decode(accessToken.substring(0, accessToken.indexOf('.'))),
				StandardCharsets.UTF_8);
		assertTrue(jwtHeader.contains("\"alg\":\"RS256\""));

		mockMvc.perform(get("/protected-resource")
						.header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isNotFound());
	}
}
