package com.jhone.album.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "nome", nullable = false , length = 255)
    private String nome;

    @OneToOne
    @JoinColumn(name = "artista_id")
    private Artistas artista;
}
