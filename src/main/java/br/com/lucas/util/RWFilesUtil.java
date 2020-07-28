package br.com.lucas.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.lucas.applicaiton.Content;
import br.com.lucas.robots.fileBot.RobotFile;

public class RWFilesUtil {
	RobotFile botFile = new RobotFile();

	public void saveJson(Content content) {
		ObjectMapper obj = new ObjectMapper();
		JSONParser parser = new JSONParser();
		try {
			String json = obj.writeValueAsString(content);
			Object object = parser.parse(json);
			JSONObject jsonObject = (JSONObject) object;
			// System.out.println(jsonObject.get("sourceContentSanized"));
			try {
				FileWriter file = new FileWriter(botFile.getFolderResource() + "content.json");
				file.write(json);
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (JsonProcessingException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveScript(String script) {
		RobotFile botFile = new RobotFile();
		String dest  = botFile.getFolderVideoResources();
		try {
			FileWriter file = new FileWriter(dest + "after-effects-script.js");
			file.write(script);
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JSONObject loadJson(String dest) {
		JSONParser parser = new JSONParser();
		try {
			Object object = parser.parse(
					new FileReader(dest));
			JSONObject jsonObject = (JSONObject) object;
			return jsonObject;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void saveContent() {

	}

	public static void loadFile() {

	}
}
