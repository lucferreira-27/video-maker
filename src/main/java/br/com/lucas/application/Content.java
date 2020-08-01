package br.com.lucas.application;

import java.util.List;

import br.com.lucas.config.Configuration;
import br.com.lucas.robots.txtBot.Sentences;

public class Content {

	private String language;
	private String search;
	private String prefixText;
	private String sourceContentOriginal;
	private String sourceContentSanized;
	private String wikipediaFont;
	private final int maximumSenteces = 7;
	private List<Sentences> listSentences;
	private Sentences thumbnailSentence = new Sentences();

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getPrefixText() {
		return prefixText;
	}

	public void setPrefixText(String prefix) {
		this.prefixText = prefix;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "Content [language=" + language + ", search=" + search + ", prefixText=" + prefixText
				+ ", sourceContentOriginal=" + sourceContentOriginal + ", sourceContentSanized=" + sourceContentSanized
				+ ", maximumSenteces=" + maximumSenteces + ", listSentences=" + listSentences + ", thumbnailSentence="
				+ thumbnailSentence + "]";
	}

	public String getSourceContentOriginal() {
		return sourceContentOriginal;
	}

	public void setSourceContentOriginal(String sourceContentOriginal) {
		this.sourceContentOriginal = sourceContentOriginal;
	}

	public String getSourceContentSanized() {
		return sourceContentSanized;
	}

	public void setSourceContentSanized(String sourceContentSanized) {
		this.sourceContentSanized = sourceContentSanized;
	}

	public List<Sentences> getListSentences() {
		return listSentences;
	}

	public void setListSentences(List<Sentences> listSentences) {
		this.listSentences = listSentences;
	}

	public int getMaximumSenteces() {
		return maximumSenteces;
	}

	public Sentences getThumbnailSentence() {
		return thumbnailSentence;
	}

	public void setThumbnailSentence(Sentences thumbnailSentence) {
		this.thumbnailSentence = thumbnailSentence;
	}

	public String getWikipediaFont() {
		return wikipediaFont;
	}

	public void setWikipediaFont(String wikipediaFont) {
		this.wikipediaFont = wikipediaFont;
	}

}
