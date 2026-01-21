package com.jhone.album.service;

import com.jhone.album.Exception.ResourceNotFoundException;
import com.jhone.album.dto.ArtistasDto;

import com.jhone.album.entity.Artistas;
import com.jhone.album.repository.ArtistasRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public ArtistasDto create(ArtistasDto artistasDTO){
        ArtistasDto artistas = ArtistasDto.create(artistasRepository.save(Artistas.create(artistasDTO)));
        return artistas;
    }

    public ArtistasDto findById(Long id){
        var entity = artistasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("O artista não foi encontrado!"));

        return ArtistasDto.create(entity);
    }

    public ArtistasDto update(ArtistasDto artistasDTO){
        artistasRepository.findById(artistasDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("O artista não foi encontrado!"));

        return ArtistasDto.create(artistasRepository.save(Artistas.create(artistasDTO)));
    }

    private ArtistasDto convertToArtistasDto(Artistas artistas){
        return ArtistasDto.create(artistas);
    }
}
