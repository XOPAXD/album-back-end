package com.jhone.album.repository;

import com.jhone.album.entity.Artistas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistasRepository extends JpaRepository<Artistas,Long> {

}
