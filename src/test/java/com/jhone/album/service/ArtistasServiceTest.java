package com.jhone.album.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.jhone.album.Exception.ResourceNotFoundException;
import com.jhone.album.dto.ArtistasDto;
import com.jhone.album.entity.Artistas;
import com.jhone.album.repository.ArtistasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ArtistasServiceTest {

    @Mock
    private ArtistasRepository artistasRepository;

    @InjectMocks
    private ArtistasService artistasService;

    private Artistas artista;
    private ArtistasDto artistaDto;

    @BeforeEach
    void setUp() {
        artista = new Artistas();
        artista.setId(1L);
        artista.setNome("Nome do Artista");

        artistaDto = ArtistasDto.create(artista);
    }

    @Test
    @DisplayName("Deve retornar lista de artistas com sucesso")
    void deveRetornarListaDeArtistasComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Artistas> page = new PageImpl<>(List.of(artista));

        when(artistasRepository.findAll(pageable)).thenReturn(page);

        Page<ArtistasDto> result = artistasService.findAll(pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent().get(0).getId()).isEqualTo(artista.getId());
        verify(artistasRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve criar um artista com sucesso")
    void deveCriarArtistaComSucesso() {
        when(artistasRepository.save(any(Artistas.class))).thenReturn(artista);

        ArtistasDto result = artistasService.create(artistaDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(artista.getId());
        verify(artistasRepository, times(1)).save(any(Artistas.class));
    }

    @Test
    @DisplayName("Deve retornar artista por ID com sucesso")
    void deveRetornarArtistaPorIdComSucesso() {
        when(artistasRepository.findById(1L)).thenReturn(Optional.of(artista));

        ArtistasDto result = artistasService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void deveLancarExcecaoAoBuscarIdInexistente() {
        when(artistasRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> artistasService.findById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("O artista não foi encontrado!");
    }

    @Test
    @DisplayName("Deve atualizar artista com sucesso")
    void deveAtualizarArtistaComSucesso() {
        when(artistasRepository.findById(artistaDto.getId())).thenReturn(Optional.of(artista));
        when(artistasRepository.save(any(Artistas.class))).thenReturn(artista);

        ArtistasDto result = artistasService.update(artistaDto);

        assertThat(result).isNotNull();
        verify(artistasRepository).save(any(Artistas.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar artista inexistente")
    void DeveLancarExcecaoAoTentarAtualizarArtistaInexistente() {
        when(artistasRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> artistasService.update(artistaDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("O artista não foi encontrado!");

        verify(artistasRepository, never()).save(any());
    }
}
