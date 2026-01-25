package com.jhone.album.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.jhone.album.Exception.ResourceNotFoundException;
import com.jhone.album.dto.AlbumDTO;
import com.jhone.album.dto.ArtistasDto;
import com.jhone.album.entity.Album;
import com.jhone.album.entity.Artistas;
import com.jhone.album.repository.AlbumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistasService artistasService;

    @InjectMocks
    private AlbumService albumService;

    @Test
    @DisplayName("Deve retornar Album por ID com sucesso")
    void deveRetornarAlbumPorIdComSucesso() {
        // Arrange
        Long id = 1L;
        Album album = new Album(); // Supondo que você tenha um construtor ou builder
        album.setId(id);

        when(albumRepository.findById(id)).thenReturn(Optional.of(album));

        // Act
        AlbumDTO result = albumService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(albumRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando ID não existir")
    void deveLancarResourceNotFoundExceptionQuandoIdNaoExistir() {
        // Arrange
        Long id = 99L;
        when(albumRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> albumService.findById(id));
        verify(albumRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve criar um álbum e associar o artista com sucesso")
    void deveCriarUmAlbumEAssociarOArtistaComSucesso() {
        Long artistaId = 10L;

        ArtistasDto artistaDtoInput = new ArtistasDto();
        artistaDtoInput.setId(artistaId);

        AlbumDTO inputDto = new AlbumDTO();
        inputDto.setNome("Album Teste");
        inputDto.setArtista(artistaDtoInput);

        Album albumEntidadeSalva = new Album();
        albumEntidadeSalva.setId(1L);
        albumEntidadeSalva.setNome("Album Teste");

        Artistas artistaEntidade = new Artistas();
        artistaEntidade.setId(artistaId);
        albumEntidadeSalva.setArtista(artistaEntidade);

        ArtistasDto artistaDtoCompleto = new ArtistasDto();
        artistaDtoCompleto.setId(artistaId);
        artistaDtoCompleto.setNome("Teste");

        when(albumRepository.save(any(Album.class))).thenReturn(albumEntidadeSalva);
        when(artistasService.findById(artistaId)).thenReturn(artistaDtoCompleto);

        AlbumDTO resultado = albumService.create(inputDto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Teste", resultado.getArtista().getNome());

        verify(albumRepository, times(1)).save(any(Album.class));
        verify(artistasService, times(1)).findById(artistaId);
    }


    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar álbum inexistente")
    void deveLançarExceçãoAoTentarAtualizarAlbumInexistente() {
        // Arrange
        AlbumDTO dto = new AlbumDTO();
        dto.setId(1L);
        when(albumRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> albumService.update(dto));
        verify(albumRepository, never()).save(any());
    }
}
