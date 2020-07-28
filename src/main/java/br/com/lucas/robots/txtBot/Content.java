package br.com.lucas.robots.txtBot;


public class Content {
	private String search;
	private String prefixText;
	private String sourceContentOriginal;
	private String sourceContentSanized;
	private final int maximumSenteces = 7;
	
	private Sentences sentences = new Sentences();
	
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

	public Sentences getSentences() {
		return sentences;
	}

	public void setSentences(Sentences sentences) {
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

	public int getMaximumSenteces() {
		return maximumSenteces;
	}
	
	
	



	
	
	
}
