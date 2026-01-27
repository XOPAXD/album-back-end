package com.jhone.album.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "regionais")
public class Regional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interno")
    private Long idInternal;

    @Column(name = "id_origem")
    private Integer id;

    public Regional(Integer id, String nome, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
    }

    private String nome;
    private boolean ativo = true;
}
