package br.com.lucas.enums;

public enum TemplateSettings {
	CENTER_1920x400("1920X400", "center"), CENTER_1920x1080("1920x1080", "center"), WEST_800x1080("800x1080", "west");

	private String resolution;
	private int height;
	private int width;
	private String gravity;

	private TemplateSettings(final String resolution, final String gravity) {
		this.resolution = resolution;
		this.gravity = gravity;

		//this.width = Integer.parseInt(resolution.split("x")[0]);
		//this.height = Integer.parseInt(resolution.split("x")[1]);
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getGravity() {
		return gravity;
	}

	public void setGravity(String gravity) {
		this.gravity = gravity;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	

}
