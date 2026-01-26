package com.jhone.album.service;

import io.github.bucket4j.Bucket;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RateLimiterServiceTest {
    private RateLimiterService rateLimiterService;

    @BeforeEach
    void setUp() {
        rateLimiterService = new RateLimiterService();
    }

    @Test
    @DisplayName("Deve retornar o mesmo bucket para o mesmo usuário (mesma chave)")
    void deveRetornarMesmoBucketParaMesmaChave() {
        String key = "user-123";

        Bucket bucket1 = rateLimiterService.resolveBucket(key);
        Bucket bucket2 = rateLimiterService.resolveBucket(key);

        Assertions.assertThat(bucket1).isSameAs(bucket2);
    }

    @Test
    @DisplayName("Deve permitir consumir até 10 tokens para um novo usuário")
    void devePermitirConsumirAteLimite() {
        String key = "user-abc";
        Bucket bucket = rateLimiterService.resolveBucket(key);

        for (int i = 0; i < 10; i++) {
            Assertions.assertThat(bucket.tryConsume(1))
                    .as("Deveria permitir a requisição número " + (i + 1))
                    .isTrue();
        }

        // O 11º token deve falhar
        Assertions.assertThat(bucket.tryConsume(1))
                .as("Deveria bloquear a 11ª requisição")
                .isFalse();
    }

    @Test
    @DisplayName("Usuários diferentes devem ter baldes independentes")
    void usuariosDiferentesDevemTerBucketsIndependentes() {
        String userA = "192.168.0.1";
        String userB = "192.168.0.2";

        Bucket bucketA = rateLimiterService.resolveBucket(userA);
        Bucket bucketB = rateLimiterService.resolveBucket(userB);

        // Consome todo o balde do Usuário A
        for (int i = 0; i < 10; i++) {
            bucketA.tryConsume(1);
        }

        Assertions.assertThat(bucketA.tryConsume(1)).isFalse(); // A bloqueado
        Assertions.assertThat(bucketB.tryConsume(1)).isTrue();  // B ainda deve ter tokens
    }

}