package br.com.lucas.robots.fileBot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

import br.com.lucas.applicaiton.Content;
import br.com.lucas.robots.txtBot.Sentences;

public class RobotFile {
	private final String folderResource = System.getProperty("user.dir") + "\\resourses\\";
	private final String folderImage = System.getProperty("user.dir") + "\\resourses\\images\\";
	private final String folderCredencias = System.getProperty("user.dir") + "\\resourses\\credenciais\\";
	private final String folderVideoResources = System.getProperty("user.dir") + "\\resourses\\video-resources\\";
	private final String folderTemplates = System.getProperty("user.dir") + "\\resourses\\video-resources\\templates\\";
	private final String folderVideoOutput = System.getProperty("user.dir") + "\\resourses\\video-resources\\video-output\\";
	private final String folderImageMagick = System.getProperty("user.dir") + "\\resourses\\ImageMagick\\";
	
	public Content loadContentWithJson(Content content, JSONObject json) {

		List<JSONObject> list = (List<JSONObject>) json.get("listSentences");
		List<Sentences> listSentences = new ArrayList<>();
		for (JSONObject obj : list) {
			Sentences s = new Sentences();
			List<String> images = (List<String>) obj.get("images");

			// images.stream().filter(line -> line.contains("//")).forEach(line ->
			// System.out.println(line));

			List<String> keywords = (List<String>) obj.get("keywrods");
			String text = (String) obj.get("text");
			s.setImages(images);
			s.setKeywrods(keywords);
			s.setText(text);
			listSentences.add(s);

		}
		content.setListSentences(listSentences);
		content.setSearch((String) json.get("search"));
		content.setPrefixText((String) json.get("prefixText"));
		content.setSourceContentOriginal((String) json.get("sourceContentOriginal"));
		content.setSourceContentSanized((String) json.get("sourceContentSanized"));
		return content;
	}

	public String getFolderResource() {
		return folderResource;
	}

	public String getFolderImage() {
		return folderImage;
	}

	public String getFolderCredencias() {
		return folderCredencias;
	}

	public String getFolderVideoResources() {
		return folderVideoResources;
	}

	public String getFolderTemplates() {
		return folderTemplates;
	}

	public String getFolderVideoOutput() {
		return folderVideoOutput;
	}

	public String getFolderImageMagick() {
		return folderImageMagick;
	}

	

}
