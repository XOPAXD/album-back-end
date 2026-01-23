package com.jhone.album.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jhone.album.entity.Album;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlbumDTO extends RepresentationModel<AlbumDTO> {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nome")
    private String nome;

    private ArtistasDto artista;

    public static AlbumDTO create(Album album){
        return new ModelMapper().map(album, AlbumDTO.class);
    }
}
