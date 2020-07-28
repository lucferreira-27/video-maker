package br.com.lucas.robots.txtBot;

import java.util.List;

public class Sentences {
	private String text;
	private List<String> keywrods;
	private List<String> images;
	private List<String> imageThumbnail;

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public List<String> getKeywrods() {
		return keywrods;
	}
	public void setKeywrods(List<String> keywrods) {
		this.keywrods = keywrods;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	@Override
	public String toString() {
		return "Sentences [text=" + text + ", keywrods=" + keywrods + ", images=" + images + "]\n";
	}
	public List<String> getImageThumbnail() {
		return imageThumbnail;
	}
	public void setImageThumbnail(List<String> imageThumbnail) {
		this.imageThumbnail = imageThumbnail;
	}

	

	

	
	
	
}
