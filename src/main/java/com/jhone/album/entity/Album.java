package com.jhone.album.entity;

import com.jhone.album.dto.AlbumDTO;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;

@Table(name = "album")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "artista_id")
    private Artistas artista;

    public static Album create(AlbumDTO albumDTO){
        return new ModelMapper().map(albumDTO, Album.class);
    }
}
