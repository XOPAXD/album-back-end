package com.jhone.album.entity;

import com.jhone.album.dto.ArtistasDto;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;

@Table(name = "artistas")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Artistas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false , length = 200)
    private String nome;

    public static Artistas create(ArtistasDto artistasDto){
        return new ModelMapper().map(artistasDto, Artistas.class);
    }
}
