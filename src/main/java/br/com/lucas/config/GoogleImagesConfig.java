package br.com.lucas.config;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GoogleImagesConfig {

	private String linkDefault = "https://www.google.com/search?q=";

	private final String size = "isz:"; // m = medium
	private final String color = "ic:"; // gray
	private final String time = "qdr:"; // w = week
	private final String license = "sur:"; // fmc =
	private final String c = ",";
	private String querry;
	private String defineSize;
	private String defineColor;
	private String defineTime;
	private String defineLicense;

	// String test =
	// "https://www.google.com/search?q=anime&tbm=isch&tbs=ic:gray,isz:lt,qdr:w,sur:fmc";
	public String encode() {
		String link = linkDefault+querry + "&tbm=isch&tbs=" + size + defineSize + c + color + defineColor + c + time + defineTime
				+ c + license + defineLicense;
		return link;

	}

	public String getDefineSize() {
		return defineSize;
	}

	public void setDefineSize(String defineSize) {
		this.defineSize = defineSize;
	}

	public String getDefineColor() {
		return defineColor;
	}

	public void setDefineColor(String defineColor) {
		this.defineColor = defineColor;
	}

	public String getDefineTime() {
		return defineTime;
	}

	public void setDefineTime(String defineTime) {
		this.defineTime = defineTime;
	}

	public String getDefineLicense() {
		return defineLicense;
	}

	public void setDefineLicense(String defineLicense) {
		this.defineLicense = defineLicense;
	}

	public String getQuerry() {
		return querry;
	}

	public void setQuerry(String querry) {
		this.querry = querry;
	}

}
