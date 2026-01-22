package com.jhone.album.service;

import com.jhone.album.Exception.ResourceNotFoundException;
import com.jhone.album.dto.AlbumDTO;
import com.jhone.album.entity.Album;
import com.jhone.album.repository.AlbumRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;

    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public Page<AlbumDTO> findAll(Pageable pageable, String artista){
        var page = albumRepository.buscarPorAlbumPorNome(pageable,artista);
        return page.map(this::convertToAlbumDto);

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
