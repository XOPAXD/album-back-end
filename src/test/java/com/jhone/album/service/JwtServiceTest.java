package com.jhone.album.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String USERNAME = "usuario.teste";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    @DisplayName("Deve gerar um token válido e não nulo")
    void deveGerarUmTokenValidoeNaoNulo() {
        String token = jwtService.generateToken(USERNAME);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Deve extrair o username correto do token")
    void deveExtrairoUsernameCorretoDoToken() {
        String token = jwtService.generateToken(USERNAME);
        String extractedUsername = jwtService.extractUsername(token);

        assertEquals(USERNAME, extractedUsername);
    }

    @Test
    @DisplayName("Deve validar um token com sucesso")
    void deveValidarTokenComSucesso() {
        String token = jwtService.generateToken(USERNAME);
        boolean isValid = jwtService.validateToken(token, USERNAME);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Deve invalidar token se o username for diferente")
    void deveInvalidarTokenSeUsernameForDiferente() {
        String token = jwtService.generateToken(USERNAME);
        boolean isValid = jwtService.validateToken(token, "outro.usuario");

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Deve verificar que um token recém criado não está expirado")
    void DeveVerificarQueUmTokenRecemCriadoNaoEstaExpirado() {
        String token = jwtService.generateToken(USERNAME);
        boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar extrair claim de token inválido")
    void deveLancarExcecaoAoTentarExtrairClaimTokenInvalido() {
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.invalid.payload";

        assertThrows(Exception.class, () -> {
            jwtService.extractUsername(invalidToken);
        });
    }
}
