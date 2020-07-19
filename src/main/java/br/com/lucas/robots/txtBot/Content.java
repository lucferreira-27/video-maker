package br.com.lucas.robots.txtBot;

import br.com.lucas.textBot.Setences;

public class Content {
	private String search;
	private String prefixText;
	private String sourceContentOriginal;
	private String sourceContentSanized;
	
	private Setences sentences = new Setences();
	
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

	@Override
	public String toString() {
		return "Content [search=" + search + ", prefixText=" + prefixText + "]";
	}

	public Setences getSentences() {
		return sentences;
	}

	public void setSentences(Setences sentences) {
		this.sentences = sentences;
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
	
	



	
	
	
}
