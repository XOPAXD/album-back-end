package com.jhone.album.service;

import com.jhone.album.dto.ArtistasDto;

import com.jhone.album.entity.Artistas;
import com.jhone.album.repository.ArtistasRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArtistasService {
    private final ArtistasRepository artistasRepository;

    public ArtistasService(ArtistasRepository artistasRepository) {
        this.artistasRepository = artistasRepository;
    }

    public Page<ArtistasDto> findAll(Pageable pageable){
        var page = artistasRepository.findAll(pageable);
        return page.map(this::convertToArtistasDto);

    }


    private ArtistasDto convertToArtistasDto(Artistas artistas){
        return ArtistasDto.create(artistas);
    }
}
