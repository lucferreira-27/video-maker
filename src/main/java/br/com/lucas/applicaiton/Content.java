package br.com.lucas.applicaiton;

import java.util.List;

import br.com.lucas.robots.txtBot.Sentences;


public class Content {
	private String search;
	private String prefixText;
	private String sourceContentOriginal;
	private String sourceContentSanized;
	
	private List<Sentences> listSentences;
	
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

	
	
	



	
	
	
}
