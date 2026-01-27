package com.jhone.album.controller;

import com.jhone.album.dto.ArtistasDTO;
import com.jhone.album.service.ArtistasService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequestMapping("/artistas")
@Tag(name = "Artistas", description = "Endpoints para manutenção de artistas")
public class ArtistaController {
    private final ArtistasService artistasService;
    private final PagedResourcesAssembler<ArtistasDTO> assembler;

    public ArtistaController(ArtistasService artistasService, PagedResourcesAssembler<ArtistasDTO> assembler) {
        this.artistasService = artistasService;
        this.assembler = assembler;
    }

    @GetMapping(produces = {"application/json","application/xml","application/x-yaml"})
    public ResponseEntity<?> findAll(@RequestParam(value = "page",defaultValue = "0") int page,
                                     @RequestParam(value = "limit",defaultValue = "12") int limit,
                                     @RequestParam(value = "direction",defaultValue = "ASC") String direction){

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection,"nome"));
        Page<ArtistasDTO> artistas = artistasService.findAll(pageable);
        PagedModel<EntityModel<ArtistasDTO>> pagedModel = assembler.toModel(artistas);

        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}",produces = {"application/json","application/xml","application/x-yaml"})
    public ArtistasDTO findByid(@PathVariable("id") Long id){
        ArtistasDTO retorno = artistasService.findById(id);
        retorno.add(linkTo(methodOn(ArtistaController.class).findByid(id)).withSelfRel());
        return retorno;
    }

    @PostMapping(produces = {"application/json","application/xml","application/x-yaml"},
            consumes = {"application/json","application/xml","application/x-yaml"})
    public ArtistasDTO create(@RequestBody ArtistasDTO artistasDTO){
        ArtistasDTO retorno = artistasService.create(artistasDTO);
        retorno.add(linkTo(methodOn(ArtistaController.class).findByid(retorno.getId())).withSelfRel());
        return retorno;
    }

    @PutMapping(produces = {"application/json","application/xml","application/x-yaml"},
            consumes = {"application/json","application/xml","application/x-yaml"})
    public ArtistasDTO update(@RequestBody ArtistasDTO categoriasDTO){
        ArtistasDTO retorno = artistasService.update(categoriasDTO);
        retorno.add(linkTo(methodOn(ArtistaController.class).findByid(retorno.getId())).withSelfRel());
        return retorno;
    }
}
