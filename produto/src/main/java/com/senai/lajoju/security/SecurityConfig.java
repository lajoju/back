package com.senai.lajoju.security;

import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(
								"/swagger-ui.html",
								"/swagger-ui/**",
								"/v3/api-docs",
								"/v3/api-docs/**")
						.permitAll()
						.anyRequest().authenticated())
				.oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()))
				.build();
	}

	@Bean
	RSAPublicKey jwtPublicKey(
			@Value("${security.jwt.public-key-location:file:../secrets/jwt-public.pem}") Resource resource) {
		try {
			String pem = new String(resource.getInputStream().readAllBytes(), StandardCharsets.US_ASCII);
			String body = pem.replace("-----BEGIN PUBLIC KEY-----", "")
					.replace("-----END PUBLIC KEY-----", "");
			byte[] encodedKey = Base64.getMimeDecoder().decode(body);
			RSAPublicKey key = (RSAPublicKey) KeyFactory.getInstance("RSA")
					.generatePublic(new X509EncodedKeySpec(encodedKey));
			if (key.getModulus().bitLength() < 2048) {
				throw new IllegalStateException("JWT RSA public keys must be at least 2048 bits.");
			}
			return key;
		} catch (IOException | GeneralSecurityException | IllegalArgumentException exception) {
			throw new IllegalStateException("Unable to load JWT RSA public key.", exception);
		}
	}

	@Bean
	JwtDecoder jwtDecoder(RSAPublicKey jwtPublicKey) {
		return NimbusJwtDecoder.withPublicKey(jwtPublicKey)
				.signatureAlgorithm(SignatureAlgorithm.RS256)
				.build();
	}
}
