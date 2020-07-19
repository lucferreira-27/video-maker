package br.com.lucas.robots.txtBot;

import java.util.Arrays;

public class Sentences {
	private String text;
	private String[] keywrods;
	private String[] images;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String[] getKeywrods() {
		return keywrods;
	}
	public void setKeywrods(String[] keywrods) {
		this.keywrods = keywrods;
	}
	public String[] getImages() {
		return images;
	}
	public void setImages(String[] images) {
		this.images = images;
	}
	@Override
	public String toString() {
		return "Sentences [text=" + text + ", keywrods=" + Arrays.toString(keywrods) + ", images="
				+ Arrays.toString(images) + "]\n";
	}
	
	
	
}
