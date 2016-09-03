package com.iworks.hateoas.controller.v2;

import com.iworks.hateoas.domain.Album;
import com.iworks.hateoas.domain.Artist;
import com.iworks.hateoas.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller("AlbumControllerV2")
@RequestMapping("/v2/albums")
@ExposesResourceFor(Album.class)
public class AlbumController {

    @Autowired
    private EntityLinks links;

    @Autowired
    private MusicService musicService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<Resource<Album>> getAllAlbums() {
        Collection<Album> albums = musicService.getAllAlbums();
        List<Resource<Album>> resources = new ArrayList<Resource<Album>>();

        for (Album album : albums) {
            resources.add(this.getAlbumResource(album));
        }

        return resources;

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
        Resource<Album> resource = new Resource<Album>(album);

        // Link to Album
        resource.add(links.linkToSingleResource(Album.class, album.getId()).withSelfRel());
        // Link to Artist
        resource.add(links.linkToSingleResource(Artist.class, album.getArtist().getId()).withRel("artist"));
        // Option to purchase Album
        if (album.getStockLevel() > 0) {
            resource.add(links.linkToSingleResource(Album.class, album.getId()).withRel("album.purchase"));
            // resource.add(linkTo(methodOn(AlbumController.class).purchaseAlbum(album.getId())).withRel("album.purchase"));
        }

        return resource;

    }
}
