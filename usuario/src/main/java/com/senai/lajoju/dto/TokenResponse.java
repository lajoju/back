package com.senai.lajoju.dto;

import java.time.Instant;

public record TokenResponse(String accessToken, String tokenType, Instant expiresAt) {
}
