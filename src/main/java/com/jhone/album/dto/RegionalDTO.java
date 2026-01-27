package com.jhone.album.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class RegionalDTO {
    @JsonProperty("id")
    Integer id;

    @JsonProperty("nome")
    String nome;

    public RegionalDTO(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
