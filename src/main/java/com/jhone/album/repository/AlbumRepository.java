package com.jhone.album.repository;

import com.jhone.album.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album,Long> {
    @Query("SELECT o FROM Album o JOIN o.artista c WHERE COALESCE(c.nome, :nome) LIKE %:nome%")
    Page<Album> buscarPorAlbumPorNome(Pageable pageable, @Param("nome") String nome);
}
