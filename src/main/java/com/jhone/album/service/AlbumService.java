package com.jhone.album.service;

import com.jhone.album.Exception.ResourceNotFoundException;
import com.jhone.album.dto.AlbumDTO;
import com.jhone.album.dto.ArtistasDto;
import com.jhone.album.entity.Album;
import com.jhone.album.repository.AlbumRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final ArtistasService artistasService;

    public AlbumService(AlbumRepository albumRepository, ArtistasService artistasService) {
        this.albumRepository = albumRepository;
        this.artistasService = artistasService;
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
        return album;
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
