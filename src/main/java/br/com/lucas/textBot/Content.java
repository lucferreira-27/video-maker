package br.com.lucas.textBot;

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



	
	
	
}
