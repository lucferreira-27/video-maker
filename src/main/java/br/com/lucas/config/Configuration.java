package br.com.lucas.config;

public class Configuration {
	
	private GoogleImagesConfig imagesConfig;
	private DownloadConfig downloadConfig;
	private RenderConfig renderConfig;

	public Configuration(GoogleImagesConfig imagesConfig, DownloadConfig downloadConfig, RenderConfig renderConfig) {

		this.imagesConfig = imagesConfig;
		this.downloadConfig = downloadConfig;
		this.renderConfig = renderConfig;
	}

	public GoogleImagesConfig getImagesConfig() {
		return imagesConfig;
	}

	public void setImagesConfig(GoogleImagesConfig imagesConfig) {
		this.imagesConfig = imagesConfig;
	}

	public DownloadConfig getDownloadConfig() {
		return downloadConfig;
	}

	public void setDownloadConfig(DownloadConfig downloadConfig) {
		this.downloadConfig = downloadConfig;
	}

	public RenderConfig getRenderConfig() {
		return renderConfig;
	}

	public void setRenderConfig(RenderConfig renderConfig) {
		this.renderConfig = renderConfig;
	}
	
	

}
