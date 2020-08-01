package br.com.lucas.robots.voiceBot;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.security.auth.callback.TextOutputCallback;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;

import br.com.lucas.application.Content;
import br.com.lucas.config.Configuration;
import br.com.lucas.robots.fileBot.RobotFile;

public class RobotVoice {
	private RobotFile botfile = new RobotFile();
	private String lg;
	private TextToSpeech textToSpeech;
	private IamAuthenticator authenticator;
	private Content content;

	public RobotVoice(Content content, Configuration config) {
		// TODO Auto-generated constructor stub
		this.content = content;
	}

	public void start() {
		selectVoiceLanguage(content.getLanguage());
		textToSpeachAllSentences();
	}

	private void selectVoiceLanguage(String lg) {
		switch (lg) {
		case "pt":
			this.lg = "pt-BR_IsabelaV3Voice";
			break;
		case "en":
			this.lg = "en-US_MichaelV3Voice";
		default:
			break;
		}
	}

	private void textToSpeachAllSentences() {
		authenticator = new IamAuthenticator("F8ngJt9kqDbks3YW2UckIvlCzAmRrgWTU44RhU7pHqK2");
		int size = 0;
		TextToSpeech textToSpeech = new TextToSpeech(authenticator);
		for (int i = 0; i < content.getListSentences().size(); i++) {
			textToSpeachSentence(textToSpeech, content.getListSentences().get(i).getText(),i);
			size += content.getListSentences().get(i).getText().length();
			
		}
		System.out.println(size);
	}

	private void textToSpeachSentence(TextToSpeech textToSpeech, String text, int index) {
		System.out.println("[Working] ...");
		SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder().text(text).accept("audio/wav").voice(lg)
				.build();
		try {
			System.out.println("[Download] ...");
			InputStream inputStream = textToSpeech.synthesize(synthesizeOptions).execute().getResult();
			InputStream in = WaveUtils.reWriteWaveHeader(inputStream);
			
			OutputStream out = new FileOutputStream(botfile.getFolderVoiceAudio() + index+".wav");
			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			out.close();
			in.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
