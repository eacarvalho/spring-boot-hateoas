package com.iworks.hateoas.controller.v2.links;

import com.iworks.hateoas.controller.v2.AlbumController;
import com.iworks.hateoas.domain.Album;
import com.iworks.hateoas.domain.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class AlbumLink {

    private static final String ARTIST = "artist";
    private static final String ALBUM_PURCHASE = "album.purchase";
    private static final String ALBUM_PAGINATION = "album.pagination";

    @Autowired
    private EntityLinks links;

    public Link getSelfLink(Album album) {
        return links.linkToSingleResource(Album.class, album.getId()).withSelfRel();
    }

    public Link getArtistLink(Album album) {
        return links.linkToSingleResource(Artist.class, album.getArtist().getId()).withRel(ARTIST);
    }

    public Link getPurchaseAlbumLink(Album album) {
        return linkTo(methodOn(AlbumController.class).purchaseAlbum(album.getId())).withRel(ALBUM_PURCHASE);
    }

    public Link getAlbumPaginationLink(Album album) {
        return linkTo(methodOn(AlbumController.class).getAllAlbumsPagination(null)).withRel(ALBUM_PAGINATION);
    }
}