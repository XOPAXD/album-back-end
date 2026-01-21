package com.jhone.album.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jhone.album.entity.Artistas;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({"id","nome"})
@NoArgsConstructor
public class ArtistasDto extends RepresentationModel<ArtistasDto> {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("nome")
    private String nome;

    public static ArtistasDto create(Artistas artistas){
        return new ModelMapper().map(artistas, ArtistasDto.class);
    }
}
