package com.senai.lajoju;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
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

}
