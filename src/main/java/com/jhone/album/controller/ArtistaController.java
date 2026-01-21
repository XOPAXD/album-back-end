package com.jhone.album.controller;

import com.jhone.album.dto.ArtistasDto;
import com.jhone.album.service.ArtistasService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/artistas")
public class ArtistaController {
    private final ArtistasService artistasService;
    private final PagedResourcesAssembler<ArtistasDto> assembler;

    public ArtistaController(ArtistasService artistasService, PagedResourcesAssembler<ArtistasDto> assembler) {
        this.artistasService = artistasService;
        this.assembler = assembler;
    }

    @GetMapping(produces = {"application/json","application/xml","application/x-yaml"})
    public ResponseEntity<?> findAll(@RequestParam(value = "page",defaultValue = "0") int page,
                                     @RequestParam(value = "limit",defaultValue = "12") int limit,
                                     @RequestParam(value = "direction",defaultValue = "ASC") String direction){

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection,"nome"));
        Page<ArtistasDto> artistas = artistasService.findAll(pageable);
        //categorias.stream()
        //  .forEach(c -> c.add(linkTo(methodOn(CategoriasController.class).findByid(c.getId())).withSelfRel()));

        PagedModel<EntityModel<ArtistasDto>> pagedModel = assembler.toModel(artistas);

        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }
}
