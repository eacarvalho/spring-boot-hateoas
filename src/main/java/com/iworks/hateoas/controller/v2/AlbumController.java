package com.iworks.hateoas.controller.v2;

import com.iworks.hateoas.controller.v2.assembler.AlbumResourceAssembler;
import com.iworks.hateoas.controller.v2.links.AlbumLink;
import com.iworks.hateoas.controller.v2.resource.AlbumResource;
import com.iworks.hateoas.domain.Album;
import com.iworks.hateoas.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller("AlbumControllerV2")
@RequestMapping("/v2/albums")
@ExposesResourceFor(Album.class)
public class AlbumController {

    /*
    EntityLinks ativa o padrão HAL - Anotação @EnableHypermediaSupport(type = {HypermediaType.HAL})
    Dessa forma é só usar EntityLinks como links.linkToSingleResource(Album.class, album.getId()).withSelfRel()
    Porém, prefiro usar Building links pointing to methods que é mais flexível e não precisa ativar @EnableHypermediaSupport
    http://docs.spring.io/spring-hateoas/docs/current/reference/html/
     */
    @Autowired
    private EntityLinks links;

    @Autowired
    private MusicService musicService;

    @Autowired
    private AlbumResourceAssembler albumResourceAssembler;

    @Autowired
    private PagedResourcesAssembler pagedResourcesAssembler;

    @Autowired
    private AlbumLink albumLink;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<AlbumResource>> getAllAlbums() {
        Collection<Album> albums = musicService.getAllAlbums();

        List<AlbumResource> resources = albumResourceAssembler.toResources(albums);

        return ResponseEntity.ok(resources);
    }

    @RequestMapping(value = "/pagination", method = RequestMethod.GET, produces = "application/hal+json")
    @ResponseBody
    public PagedResources<Album> getAllAlbumsPagination(@PageableDefault Pageable page) {
        Collection<Album> albums = musicService.getAllAlbums();

        // Page<User> users = userRepository.findAll(new PageRequest(0, 10));

        // Adicionado spring-data para ter o objeto assim quando subir o spring lança exceção de conexão
        Page<Album> albumsPaged = new PageImpl<>(new ArrayList<>(albums), new PageRequest(0, 1), albums.size());

        // TODO: Criar a PagedResourcesAssembler assim como AlbumResourceAssembler
        PagedResources<Album> pagedResources = pagedResourcesAssembler.toResource(albumsPaged, new AlbumResourceAssembler());

        pagedResources.add(links.linkToCollectionResource(Album.class).withRel("albums"));

        return pagedResources;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resource<Album> getAlbum(@PathVariable(value = "id") String id) {
        Album album = musicService.getAlbum(id);
        return getAlbumResource(album);
    }

    @RequestMapping(value = "/purchase/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resource<Album> purchaseAlbum(@PathVariable(value = "id") String id) {
        Album a = musicService.getAlbum(id);
        a.setStockLevel(a.getStockLevel() - 1);
        Resource<Album> resource = new Resource<Album>(a);
        resource.add(links.linkToSingleResource(Album.class, id).withSelfRel());
        return resource;

    }

    private Resource<Album> getAlbumResource(Album album) {
        Resource<Album> resource = new Resource<>(album);

        // Link to Album
        resource.add(albumLink.getSelfLink(album));
        // Link to Artist
        resource.add(albumLink.getArtistLink(album));
        // Option to purchase Album
        if (album.getStockLevel() > 0) {
            // resource.add(links.linkToSingleResource(Album.class, album.getId()).withRel("album.purchase"));
            resource.add(albumLink.getPurchaseAlbumLink(album));
        }
        resource.add(albumLink.getAlbumPaginationLink(album));

        return resource;
    }
}
