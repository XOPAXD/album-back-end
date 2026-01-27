package com.jhone.album.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlbumStorageServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private AlbumStorageService storageService;

    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        mockFile = new MockMultipartFile(
                "files",
                "capa_album.jpg",
                "image/jpeg",
                "conteudo da imagem".getBytes()
        );
    }

    @Test
    @DisplayName("Deve realizar upload de imagem com sucesso e retornar nome do arquivo")
    void deveRealizarUploadImagemComSucessoRetornarNomeArquivo() throws IOException {
        String fileName = storageService.uploadImage(mockFile);

        assertNotNull(fileName);
        assertTrue(fileName.contains("capa_album.jpg"));
    }

    @Test
    @DisplayName("Deve capturar os argumentos enviados para o S3")
    void deveCapturarArgumentosEnviadosParaS3() throws IOException {
        ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);

        storageService.uploadImage(mockFile);

        verify(s3Client).putObject(requestCaptor.capture(), any(RequestBody.class));

        PutObjectRequest capturedRequest = requestCaptor.getValue();

        assertEquals("album-capas", capturedRequest.bucket());
        assertEquals("image/jpeg", capturedRequest.contentType());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o S3Client falhar")
    void deveLancarExcecaoQuandoS3ClientFalhar() {
        // Simulamos um erro interno do S3
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenThrow(new RuntimeException("Erro no MinIO"));

        assertThrows(RuntimeException.class, () -> {
            storageService.uploadImage(mockFile);
        });
    }

}