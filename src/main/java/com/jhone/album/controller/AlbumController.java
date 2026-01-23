package com.jhone.album.controller;

import com.jhone.album.dto.AlbumDTO;
import com.jhone.album.service.AlbumService;
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
@RequestMapping("/album")
@Tag(name = "Album", description = "Endpoints para manutenção de albums")
public class AlbumController {

    private final AlbumService albumService;
    private final PagedResourcesAssembler<AlbumDTO> assembler;

    public AlbumController(AlbumService albumService, PagedResourcesAssembler<AlbumDTO> assembler) {
        this.albumService = albumService;
        this.assembler = assembler;
    }

    @GetMapping(produces = {"application/json","application/xml","application/x-yaml"})
    public ResponseEntity<?> findAll(@RequestParam(value = "page",defaultValue = "0") int page,
                                     @RequestParam(value = "limit",defaultValue = "12") int limit,
                                     @RequestParam(value = "direction",defaultValue = "ASC") String direction,
                                     @RequestParam(name = "artista",defaultValue = "" ,required = false) String artista){

        var sortDirection = "ASC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection,"nome"));
        Page<AlbumDTO> albums = albumService.findAll(pageable, artista);
        albums.stream().forEach(c -> c.add(linkTo(methodOn(AlbumController.class).findByid(c.getId())).withSelfRel()));

        PagedModel<EntityModel<AlbumDTO>> pagedModel = assembler.toModel(albums);

        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @PostMapping(produces = {"application/json","application/xml","application/x-yaml"},
            consumes = {"application/json","application/xml","application/x-yaml"})
    public AlbumDTO create(@RequestBody AlbumDTO albumDTO){
        AlbumDTO retorno = albumService.create(albumDTO);
        retorno.add(linkTo(methodOn(AlbumController.class).findByid(retorno.getId())).withSelfRel());
        return retorno;
    }

    @PutMapping(produces = {"application/json","application/xml","application/x-yaml"},
            consumes = {"application/json","application/xml","application/x-yaml"})
    public AlbumDTO update(@RequestBody AlbumDTO albumDTO){
        AlbumDTO retorno = albumService.update(albumDTO);
        retorno.add(linkTo(methodOn(AlbumController.class).findByid(retorno.getId())).withSelfRel());
        return retorno;
    }

    @GetMapping(value = "/{id}",produces = {"application/json","application/xml","application/x-yaml"})
    public AlbumDTO findByid(@PathVariable("id") Long id){
        AlbumDTO retorno = albumService.findById(id);
        retorno.add(linkTo(methodOn(ArtistaController.class).findByid(id)).withSelfRel());
        return retorno;
    }
}
