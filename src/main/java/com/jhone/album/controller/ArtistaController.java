package com.jhone.album.controller;

import com.jhone.album.dto.ArtistasDto;
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
        PagedModel<EntityModel<ArtistasDto>> pagedModel = assembler.toModel(artistas);

        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}",produces = {"application/json","application/xml","application/x-yaml"})
    public ArtistasDto findByid(@PathVariable("id") Long id){
        ArtistasDto retorno = artistasService.findById(id);
        retorno.add(linkTo(methodOn(ArtistaController.class).findByid(id)).withSelfRel());
        return retorno;
    }

    @PostMapping(produces = {"application/json","application/xml","application/x-yaml"},
            consumes = {"application/json","application/xml","application/x-yaml"})
    public ArtistasDto create(@RequestBody ArtistasDto artistasDTO){
        ArtistasDto retorno = artistasService.create(artistasDTO);
        retorno.add(linkTo(methodOn(ArtistaController.class).findByid(retorno.getId())).withSelfRel());
        return retorno;
    }

    @PutMapping(produces = {"application/json","application/xml","application/x-yaml"},
            consumes = {"application/json","application/xml","application/x-yaml"})
    public ArtistasDto update(@RequestBody ArtistasDto categoriasDTO){
        ArtistasDto retorno = artistasService.update(categoriasDTO);
        retorno.add(linkTo(methodOn(ArtistaController.class).findByid(retorno.getId())).withSelfRel());
        return retorno;
    }
}
