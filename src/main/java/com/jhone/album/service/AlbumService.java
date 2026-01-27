package com.jhone.album.service;

import com.jhone.album.Exception.ResourceNotFoundException;
import com.jhone.album.dto.AlbumDTO;
import com.jhone.album.dto.ArtistasDto;
import com.jhone.album.entity.Album;
import com.jhone.album.repository.AlbumRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final ArtistasService artistasService;
    private final AlbumStorageService storageService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public AlbumService(AlbumRepository albumRepository, ArtistasService artistasService, AlbumStorageService storageService) {
        this.albumRepository = albumRepository;
        this.artistasService = artistasService;
        this.storageService = storageService;
    }

    public Page<AlbumDTO> findAll(Pageable pageable, String artista){
        var page = albumRepository.buscarPorAlbumPorNome(pageable,artista);
        return page.map(this::convertToAlbumDto);

    }

    public AlbumDTO create(AlbumDTO albumDTO){
        ArtistasDto artista;
        AlbumDTO album = AlbumDTO.create(albumRepository.save(Album.create(albumDTO)));
        artista = artistasService.findById(album.getArtista().getId());
        album.setArtista(artista);
        messagingTemplate.convertAndSend("/topic/album", album);
        return album;
    }

    @Transactional
    public Album addCapasAlbum(Long albumId, List<MultipartFile> files) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado"));

        files.forEach(file -> {
            try {
                String fileName = storageService.uploadImage(file);

                album.getCapas().add(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Falha ao processar arquivo: " + file.getOriginalFilename());
            }
        });

        // 3. Salva a atualização no banco
        return albumRepository.save(album);
    }

    public AlbumDTO update(AlbumDTO albumDTO){
        albumRepository.findById(albumDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Album não encontrado!"));

        return AlbumDTO.create(albumRepository.save(Album.create(albumDTO)));
    }

    public AlbumDTO findById(Long id){
        var entity = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album não encontrado para este id!"));

        return AlbumDTO.create(entity);
    }

    private AlbumDTO convertToAlbumDto(Album album){
        return  AlbumDTO.create(album);
    }
}
