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
import com.algorithmia.algo.AlgoResponse;
import com.algorithmia.algo.Algorithm;

import br.com.lucas.applicaiton.Content;
import br.com.lucas.util.RobotTextUtil;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class RobotText {

	private Content content;

	public RobotText(Content content) {
		// TODO Auto-generated constructor stub
		this.content = content;
	}

	public void start() {
		System.out.println(content);

		featchContentFromWikipedia(content);
		sanitizeContent(content);
		breakContentIntoSentes(content);
	}

	private void featchContentFromWikipedia(Content content) {

		AlgorithmiaClient client = Algorithmia.client("");
		Algorithm algo = client.algo("web/WikipediaParser/0.1.2");
		algo.setTimeout(300L, TimeUnit.SECONDS);

		AlgoResponse result;
		try {

			JSONObject jsonParser = new JSONObject(algo.pipe(content.getSearch()).asJsonString());
		
			content.setSourceContentOriginal(jsonParser.getString("content"));

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
			is = new FileInputStream(
					"D:\\OneDrive - Fatec Centro Paula Souza\\Workspace\\Java\\workspace\\video-maker\\src\\main\\java\\br\\com\\lucas\\robots\\txtBot\\en-sent.bin");
			SentenceModel model = new SentenceModel(is);
			SentenceDetectorME sdetector = new SentenceDetectorME(model);
			List<Sentences> listSenteces = new ArrayList<>();
			String[] setencesSplit = sdetector.sentDetect(content.getSourceContentSanized());
			for (String s : setencesSplit) {
				Sentences setence = new Sentences();
				setence.setText(s);
				listSenteces.add(setence);
			}
			System.out.println(listSenteces);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
