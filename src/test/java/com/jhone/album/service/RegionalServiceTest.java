package com.jhone.album.service;

import com.jhone.album.dto.RegionalDTO;
import com.jhone.album.entity.Regional;
import com.jhone.album.repository.RegionalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegionalServiceTest {
    @Mock
    private RegionalRepository repository;

    @InjectMocks
    private RegionalService regionalService;

    @Captor
    private ArgumentCaptor<List<Regional>> listCaptor;

    @Test
    @DisplayName("Deve inserir uma nova regional quando não existir no banco")
    void deveInserirNovaRegional() {
        RegionalDTO dto = new RegionalDTO(1, "Regional Norte");
        Mockito.when(repository.findByAtivoTrue()).thenReturn(List.of());

        regionalService.syncRegionais(List.of(dto));

        Mockito.verify(repository).saveAll(listCaptor.capture());
        List<Regional> salvos = listCaptor.getValue();

        assertEquals(1, salvos.size());
        assertEquals("Regional Norte", salvos.get(0).getNome());
        assertTrue(salvos.get(0).isAtivo());
    }

    @Test
    @DisplayName("Deve inativar regional que está no banco mas ausente no endpoint")
    void deveInativarRegionalAusente() {
        Regional local = new Regional(1, "Regional Sul", true);
        Mockito.when(repository.findByAtivoTrue()).thenReturn(List.of(local));

        regionalService.syncRegionais(List.of());

        Mockito.verify(repository).saveAll(listCaptor.capture());
        List<Regional> salvos = listCaptor.getValue();

        assertEquals(1, salvos.size());
        assertFalse(salvos.get(0).isAtivo(), "A regional local deveria ser inativada");
    }

    @Test
    @DisplayName("Deve inativar registro antigo e criar novo quando o nome for alterado")
    void deveInativarECriarQuandoAtributoAlterado() {
        Regional local = new Regional(1, "Nome Antigo", true);
        RegionalDTO remote = new RegionalDTO(1, "Nome Novo");

        Mockito.when(repository.findByAtivoTrue()).thenReturn(List.of(local));

        regionalService.syncRegionais(List.of(remote));

        Mockito.verify(repository).saveAll(listCaptor.capture());
        List<Regional> salvos = listCaptor.getValue();

        assertEquals(2, salvos.size());

        boolean temInativado = salvos.stream().anyMatch(r -> r.getNome().equals("Nome Antigo") && !r.isAtivo());
        boolean temNovoAtivo = salvos.stream().anyMatch(r -> r.getNome().equals("Nome Novo") && r.isAtivo());

        assertTrue(temInativado, "Deveria conter o registro antigo inativado");
        assertTrue(temNovoAtivo, "Deveria conter o novo registro ativo");
    }
}