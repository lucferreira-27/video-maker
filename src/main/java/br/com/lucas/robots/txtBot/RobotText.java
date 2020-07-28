package br.com.lucas.robots.txtBot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.algorithmia.APIException;
import com.algorithmia.AlgorithmException;
import com.algorithmia.Algorithmia;
import com.algorithmia.AlgorithmiaClient;
import com.algorithmia.algo.Algorithm;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.cloud.sdk.core.service.exception.RequestTooLargeException;
import com.ibm.cloud.sdk.core.service.exception.ServiceResponseException;
import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.natural_language_understanding.v1.model.Features;
import com.ibm.watson.natural_language_understanding.v1.model.KeywordsOptions;

import br.com.lucas.applicaiton.Content;
import br.com.lucas.config.Configuration;
import br.com.lucas.robots.fileBot.RobotFile;
import br.com.lucas.util.RWFilesUtil;
import br.com.lucas.util.RobotTextUtil;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class RobotText {
	
	private RobotFile botFile = new RobotFile();
	//private final String folderResource = System.getProperty("user.dir") + "\\resourses\\";
	private Content content;
	private NaturalLanguageUnderstanding nlc;
	private AlgorithmiaClient client;
	private Algorithm algo;
	private RWFilesUtil rw = new RWFilesUtil();
	private Configuration config;
	public RobotText(Content content, Configuration config) {
		// TODO Auto-generated constructor stub
		this.content = content;
		this.config = config;
	}

	private void ini() {
		// Watson Instantiation
		Authenticator authenticator = new IamAuthenticator(
				(String) (rw.loadJson(botFile.getFolderCredencias() + "watson_apikey.json").get("watson_apikey")));
		nlc = new NaturalLanguageUnderstanding("2019-07-12", authenticator);

		// Algoritm Instantiation

		client = Algorithmia.client((String) rw.loadJson(botFile.getFolderCredencias() + "algorithmia_apikey.json").get("algorithmia_apikey"));
		algo = client.algo("web/WikipediaParser/0.1.2");
		algo.setTimeout(300L, TimeUnit.SECONDS);
	}

	public void start() {
		ini();

		featchContentFromWikipedia(content);
		System.out.println("> [Clearing Content] ...");
		sanitizeContent(content);
		breakContentIntoSentes(content);
		limitMaximumSentecs(content);
		System.out.println("> [Work complete]");
		featchWatsonAndReturnKeywords(content);
		rw.saveJson(content);
	}

	private void featchContentFromWikipedia(Content content) {
		String input = "{"
				 + "  \"articleName\": \""+content.getSearch()+"\","
				 + "  \"lang\": \""+content.getLanguage()+"\""
				 + "}";
		
		try {
			System.out.println("> [Searching Wikipedia] ...");
			JSONObject jsonParser = new JSONObject(algo.pipeJson(input).asJsonString());
			content.setWikipediaFont(jsonParser.getString("url"));
			content.setSourceContentOriginal(jsonParser.getString("content"));
			System.out.println("> [Wikipedia Search Completed]");
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlgorithmException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void sanitizeContent(Content content) {
		String newContentText = RobotTextUtil.removeBlanksAndMarkdownLines(content.getSourceContentOriginal());
		newContentText = RobotTextUtil.removeDatesInParentheses(newContentText);
		content.setSourceContentSanized(newContentText);
	}

	private void breakContentIntoSentes(Content content) {
		// TODO Auto-generated method stub
		InputStream is;
		try {
			is = new FileInputStream(botFile.getFolderResource() + "en-sent.bin");
			SentenceModel model = new SentenceModel(is);
			SentenceDetectorME sdetector = new SentenceDetectorME(model);
			List<Sentences> listSenteces = new ArrayList<>();
			String[] setencesSplit = sdetector.sentDetect(content.getSourceContentSanized());
			for (String s : setencesSplit) {
				Sentences setence = new Sentences();
				setence.setText(s);
				listSenteces.add(setence);
			}
			content.setListSentences(listSenteces);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void limitMaximumSentecs(Content content) {
		content.setListSentences(content.getListSentences().subList(0, content.getMaximumSenteces()));
	}

	private void featchWatsonAndReturnKeywords(Content content) {
		try {
			System.out.println("> [Geting keywords] ...");
			KeywordsOptions keywordsOptions = new KeywordsOptions.Builder().build();

			Features features = new Features.Builder().keywords(keywordsOptions).build();

			for (int j = 0; j < content.getListSentences().size(); j++) {
				AnalyzeOptions parameters = new AnalyzeOptions.Builder()
						.text(content.getListSentences().get(j).getText()).features(features).build();
				AnalysisResults resuls = nlc.analyze(parameters).execute().getResult();
				List<String> keywords = new ArrayList<>();

				for (int i = 0; i < resuls.getKeywords().size(); i++) {
					JSONObject jsonParser = new JSONObject(resuls.getKeywords().get(i));
					keywords.add(jsonParser.getString("text"));
				}
				content.getListSentences().get(j).setKeywrods(keywords);
			}
			System.out.println("> [Work Complete]");
		} catch (NotFoundException e) {
			// Handle Not Found (404) exception
			e.printStackTrace();
		} catch (RequestTooLargeException e) {
			// Handle Request Too Large (413) exception
			e.printStackTrace();
		} catch (ServiceResponseException e) {
			// Base class for all exceptions caused by error responses from the service
			System.out.println("Service returned status code " + e.getStatusCode() + ": " + e.getMessage());
		}
	}

}
