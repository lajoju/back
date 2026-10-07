package com.senai.lajoju.security;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Clock;
import java.util.Base64;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	private static final String JWT_KEY_ID = "lajoju-rsa-1";

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
						.requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login").permitAll()
						.anyRequest().authenticated())
				.oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()))
				.build();
	}

	@Bean
	RSAPublicKey jwtPublicKey(
			@Value("${security.jwt.public-key-location:file:../secrets/jwt-public.pem}") Resource resource) {
		try {
			RSAPublicKey key = (RSAPublicKey) KeyFactory.getInstance("RSA")
					.generatePublic(new X509EncodedKeySpec(decodePem(resource, "PUBLIC KEY")));
			if (key.getModulus().bitLength() < 2048) {
				throw new IllegalStateException("JWT RSA keys must be at least 2048 bits.");
			}
			return key;
		} catch (IOException | GeneralSecurityException | IllegalArgumentException exception) {
			throw new IllegalStateException("Unable to load JWT RSA public key.", exception);
		}
	}

	@Bean
	RSAPrivateKey jwtPrivateKey(
			@Value("${security.jwt.private-key-location:file:../secrets/jwt-private.pem}") Resource resource) {
		try {
			RSAPrivateKey key = (RSAPrivateKey) KeyFactory.getInstance("RSA")
					.generatePrivate(new PKCS8EncodedKeySpec(decodePem(resource, "PRIVATE KEY")));
			if (key.getModulus().bitLength() < 2048) {
				throw new IllegalStateException("JWT RSA keys must be at least 2048 bits.");
			}
			return key;
		} catch (IOException | GeneralSecurityException | IllegalArgumentException exception) {
			throw new IllegalStateException("Unable to load JWT RSA private key.", exception);
		}
	}

	@Bean
	JwtDecoder jwtDecoder(RSAPublicKey jwtPublicKey) {
		return NimbusJwtDecoder.withPublicKey(jwtPublicKey)
				.signatureAlgorithm(SignatureAlgorithm.RS256)
				.build();
	}

	@Bean
	JwtEncoder jwtEncoder(RSAPublicKey jwtPublicKey, RSAPrivateKey jwtPrivateKey) {
		if (!jwtPublicKey.getModulus().equals(jwtPrivateKey.getModulus())) {
			throw new IllegalStateException("JWT RSA public and private keys do not match.");
		}
		RSAKey rsaKey = new RSAKey.Builder(jwtPublicKey)
				.privateKey(jwtPrivateKey)
				.keyID(JWT_KEY_ID)
				.build();
		return new NimbusJwtEncoder(new ImmutableJWKSet<SecurityContext>(new JWKSet(rsaKey)));
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	Clock clock() {
		return Clock.systemUTC();
	}

	private static byte[] decodePem(Resource resource, String keyType) throws IOException {
		String pem;
		try (InputStream input = resource.getInputStream()) {
			pem = new String(input.readAllBytes(), StandardCharsets.US_ASCII);
		}
		String body = pem
				.replace("-----BEGIN " + keyType + "-----", "")
				.replace("-----END " + keyType + "-----", "");
		return Base64.getMimeDecoder().decode(body);
	}
}
