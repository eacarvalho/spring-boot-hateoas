package com.iworks.hateoas.controller.v2;

import com.iworks.hateoas.domain.Artist;
import com.iworks.hateoas.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller("ArtistControllerV2")
@RequestMapping("/v2")
public class ArtistController {

    @Autowired
    private MusicService musicService;

    @RequestMapping(value = "/artist/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resource<Artist> getArtist(@PathVariable(value = "id") String id) {
        Artist a = musicService.getArtist(id);
        Resource<Artist> resource = new Resource<Artist>(a);
        resource.add(linkTo(methodOn(ArtistController.class).getArtist(id)).withSelfRel());
        return resource;
    }
}