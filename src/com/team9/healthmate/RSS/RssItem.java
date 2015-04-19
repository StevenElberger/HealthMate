package com.team9.healthmate.RSS;
/**
 * This code encapsulates RSS item data.
 * Our application needs title and link data.
 */
public class RssItem {
	
	//item title
	private String title;
	//item link
	private String link;
	//item description
	private String description;
	//item image
	private String image;
		
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
		}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
		
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	
	
}
