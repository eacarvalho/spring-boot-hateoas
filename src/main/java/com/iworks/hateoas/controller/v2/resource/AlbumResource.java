package com.iworks.hateoas.controller.v2.resource;

import com.iworks.hateoas.domain.Artist;
import org.springframework.hateoas.ResourceSupport;

public class AlbumResource extends ResourceSupport {

	private final String title;
	private final Artist artist;
	private int stockLevel;

	public AlbumResource(final String title, final Artist artist, int stockLevel) {
		this.title = title;
		this.artist = artist;
		this.stockLevel = stockLevel;
	}

	public String getTitle() {
		return title;
	}

	public Artist getArtist() {
		return artist;
	}

	public int getStockLevel() {
		return stockLevel;
	}

	public void setStockLevel(int stockLevel) {
		this.stockLevel = stockLevel;
	}
}