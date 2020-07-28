package br.com.lucas.robots.videoBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessStarter;

import br.com.lucas.applicaiton.Content;
import br.com.lucas.config.Configuration;
import br.com.lucas.robots.fileBot.RobotFile;
import br.com.lucas.util.RWFilesUtil;
import br.com.lucas.util.RobotVideoUtil;

public class RobotVideo {
	private Content content;
	private Configuration config;
	private RobotFile botFile = new RobotFile();


	public RobotVideo(Content content, Configuration config) throws Exception {
		// TODO Auto-generated constructor stub
		this.content = content;
		this.config = config;
		ProcessStarter.setGlobalSearchPath(loadImageMagick());
		System.out.println(System.getProperty("user.dir") + "\\resourses\\images\\");

	}

	public String loadImageMagick() throws Exception {
		File folder = new File(botFile.getFolderImageMagick());
		Map<String, Integer> map = new HashMap<>();
		List<String> findFolders;
		List<String> imageMagickVersions = new ArrayList<>();
		findFolders = Arrays.asList(folder.list()).stream()
				.filter(file -> file.toLowerCase().contains("portable") && file.toLowerCase().contains("imagemagick"))
				.collect(Collectors.toList());
		
		
		
		if(findFolders.size() == 1) {
			return botFile.getFolderImageMagick() + findFolders.get(0);
		}
		else if(findFolders.size() == 0)
			throw new Exception("ImageMagick doesn't found in ImageMagick folder");
		
		for (String s : findFolders) {
			String sub = s.substring(s.indexOf("-") + 1, s.indexOf("portable") - 1);
			map.put(s, Integer.parseInt(sub.replace("-", "").replace(".", "")));
		}
		for (int i = 0; i < findFolders.size(); i++) {
			if(map.get(findFolders.get(i)) == Collections.max(map.values())){
				return botFile.getFolderImageMagick() + findFolders.get(i);
			}
		}
		return null;

	}

	public void start() {
		convertAllImages(content);
		createAllSentecesImages(content);
		createYoutubeThumbnail();
		createAfterEffectsScript();
		//renderVideoWitchAfterEffetcs();
	}

	private void convertAllImages(Content content) {
		for (int i = -1; i < content.getMaximumSenteces(); i++) {
			convertImage(String.valueOf(i));
		}
	}

	private void convertImage(String index) {

		String destOriginal = "\\imagens-original\\";
		String destConverted = "\\imagens-converted\\";

		String inputName = index + "-original.png";
		String outputName = index + "-converted.png";

		try {

			IMOperation op = new IMOperation();
			ConvertCmd cmd = new ConvertCmd();
			int width = 1920;
			int heiht = 1080;
			op.addImage(botFile.getFolderImage() + destOriginal + inputName);
			op.openOperation();
			op.clone(0);
			op.background("white");
			op.blur(0.0, 9.0);
			op.resize(width, heiht, "^");
			op.closeOperation();
			op.openOperation();
			op.clone(0);
			op.background("white");
			op.resize(width, heiht);
			op.closeOperation();
			op.delete(0);
			op.gravity("center");
			op.compose("over");
			op.composite();
			op.extent(width, heiht);
			op.addImage(botFile.getFolderImage() + destConverted + outputName);

			System.out.println("> [Working] ...");

			cmd.run(op);
			System.out.println("> [Complete] " + inputName + " converted to " + outputName);
			System.out.println("> [Save] in " + botFile.getFolderImage() + destConverted + outputName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		} catch (IM4JavaException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void createAllSentecesImages(Content content) {
		for (int i = 0; i < content.getListSentences().size(); i++)
			createSenteceImage(i, content.getListSentences().get(i).getText());

	}

	private void createSenteceImage(int index, String text) {
		try {
			IMOperation op = new IMOperation();
			ConvertCmd cmd = new ConvertCmd();
			String destSentences = "\\sentences\\";

			String resolution = RobotVideoUtil.templateSenttings()[index].getResolution().toLowerCase();
			String outputname = botFile.getFolderImage() + destSentences + index + "-sentence.png";

			int height = Integer.parseInt(resolution.split("x")[0]); // RobotVideoUtil.templateSenttings()[index].getHeight();
			int width = Integer.parseInt(resolution.split("x")[1]); // RobotVideoUtil.templateSenttings()[index].getWidth();

			op.size(height, width);
			op.gravity(RobotVideoUtil.templateSenttings()[index].getGravity());
			op.background("transparent");
			op.fill("white");
			op.kerning(-1);
			op.font("white");
			op.addRawArgs("caption: " + text);
			op.addImage(outputname);
			System.out.println("> [Working] ...");

			cmd.run(op);

			System.out.println("> [Created] Sentece created: " + text);
			System.out.println("> [Save] in " + outputname);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		} catch (IM4JavaException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createYoutubeThumbnail() {
		try {
			IMOperation op = new IMOperation();
			ConvertCmd cmd = new ConvertCmd();
			String destThumbnail = "\\youtube-thumbnail\\";
			String destConverted = "\\imagens-converted\\";
			op.addImage(botFile.getFolderImage() + destConverted + "-1-converted.png");
			op.addImage(botFile.getFolderImage() + destThumbnail + "youtube-thumbnail.jpg");

			cmd.run(op);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		} catch (IM4JavaException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createAfterEffectsScript() {
		RWFilesUtil rw = new RWFilesUtil();
		String script = "var content = " + rw.loadJson(botFile.getFolderResource() + "content.json").toJSONString();
		rw.saveScript(script);

	}

	public void renderVideoWitchAfterEffetcs() {
		String aerenderPath = "C:\\Program Files\\Adobe\\Adobe After Effects CC 2019\\Support Files\\aerender.exe\"";
		String templatePath = "\"" + botFile.getFolderVideoResources()
				+ "templates\\Template by Filipe Deschamps.aep\"";
		String outputPath = "\"" + botFile.getFolderVideoResources() + "video-output\\" + content.getPrefixText() + " "
				+ content.getSearch() + ".mov";
		String cmd = aerenderPath + " -project " + templatePath + " -comp \"main\" -output " + outputPath;

		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream is = process.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
