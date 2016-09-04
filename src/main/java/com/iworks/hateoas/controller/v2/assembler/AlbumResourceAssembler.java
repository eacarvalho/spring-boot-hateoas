package com.iworks.hateoas.controller.v2.assembler;

import com.iworks.hateoas.controller.v2.ArtistController;
import com.iworks.hateoas.controller.v2.AlbumController;
import com.iworks.hateoas.controller.v2.resource.AlbumResource;
import com.iworks.hateoas.domain.Album;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class AlbumResourceAssembler extends ResourceAssemblerSupport<Album, AlbumResource> {

    public AlbumResourceAssembler() {
        super(AlbumController.class, AlbumResource.class);
    }

    @Override
    public AlbumResource toResource(Album album) {
        AlbumResource resource = new AlbumResource(album.getTitle(), album.getArtist(), album.getStockLevel());

        // Link to Album
        resource.add(linkTo(methodOn(AlbumController.class).getAlbum(album.getId())).withSelfRel());
        // Link to Artist
        resource.add(linkTo(methodOn(ArtistController.class).getArtist(album.getArtist().getId())).withRel("artist"));
        // Option to purchase Album
        if (album.getStockLevel() > 0) {
            resource.add(linkTo(methodOn(AlbumController.class).purchaseAlbum(album.getId())).withRel("album.purchase"));
        }

        resource.add(linkTo(methodOn(AlbumController.class).getAllAlbumsPagination(null)).withRel("albums.pagination"));

        return resource;
    }
}