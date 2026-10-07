package com.senai.lajoju.security;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import com.senai.lajoju.dto.TokenResponse;
import com.senai.lajoju.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

	private static final String KEY_ID = "lajoju-rsa-1";
	private static final Duration TOKEN_LIFETIME = Duration.ofHours(1);

	private final JwtEncoder jwtEncoder;
	private final Clock clock;

	public TokenResponse generateToken(Usuario usuario) {
		Instant issuedAt = clock.instant();
		Instant expiresAt = issuedAt.plus(TOKEN_LIFETIME);
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.subject(usuario.getEmail())
				.issuedAt(issuedAt)
				.expiresAt(expiresAt)
				.claim("uid", usuario.getIdentificador().toString())
				.build();
		String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(
				JwsHeader.with(SignatureAlgorithm.RS256).keyId(KEY_ID).build(), claims)).getTokenValue();
		return new TokenResponse(accessToken, "Bearer", expiresAt);
	}
}
