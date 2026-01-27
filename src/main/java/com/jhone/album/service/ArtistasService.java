package com.jhone.album.service;

import com.jhone.album.Exception.ResourceNotFoundException;
import com.jhone.album.dto.ArtistasDTO;

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

    public Page<ArtistasDTO> findAll(Pageable pageable){
        var page = artistasRepository.findAll(pageable);
        return page.map(this::convertToArtistasDto);

    }

    public ArtistasDTO create(ArtistasDTO artistasDTO){
        ArtistasDTO artistas = ArtistasDTO.create(artistasRepository.save(Artistas.create(artistasDTO)));
        return artistas;
    }

    public ArtistasDTO findById(Long id){
        var entity = artistasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("O artista não foi encontrado!"));

        return ArtistasDTO.create(entity);
    }

    public ArtistasDTO update(ArtistasDTO artistasDTO){
        artistasRepository.findById(artistasDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("O artista não foi encontrado!"));

        return ArtistasDTO.create(artistasRepository.save(Artistas.create(artistasDTO)));
    }

    private ArtistasDTO convertToArtistasDto(Artistas artistas){
        return ArtistasDTO.create(artistas);
    }
}
