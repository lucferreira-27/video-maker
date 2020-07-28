package br.com.lucas.applicaiton;

import java.util.Scanner;

import br.com.lucas.config.DownloadConfig;
import br.com.lucas.config.GoogleImagesConfig;
import br.com.lucas.config.RenderConfig;
import br.com.lucas.config.Configuration;
import br.com.lucas.robots.fileBot.RobotFile;
import br.com.lucas.robots.imgBot.RobotImage;
import br.com.lucas.robots.txtBot.RobotText;
import br.com.lucas.robots.videoBot.RobotVideo;
import br.com.lucas.robots.ytBot.RobotYoutube;
import br.com.lucas.util.OrchestratorUtil;
import br.com.lucas.util.RWFilesUtil;

public class Orchestrator {
	private static Content content = new Content();
	private static RobotText txtBot;
	private static RobotImage imgBot;
	private static RobotFile fileBot;
	private static RobotVideo videoBot;
	private static RobotYoutube ytBot;

	private static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) {
		start();
		System.out.println("========= END =========");
		System.exit(0);
	}

	public static void start() {

		RWFilesUtil rw = new RWFilesUtil();

		content.setSearch(OrchestratorUtil.searchTerm());
		content.setLanguage(OrchestratorUtil.selectLanguage());
		content.setPrefixText(OrchestratorUtil.prefixTerm(content));
		Configuration config = new Configuration(new GoogleImagesConfig(), new DownloadConfig(), new RenderConfig());
		// content.setConfig(config);
		try {
			fileBot = new RobotFile();
			txtBot = new RobotText(content, config);
			imgBot = new RobotImage(content, config);
			videoBot = new RobotVideo(content, config);
			ytBot = new RobotYoutube(content, config);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println("===== CREATE A VIDEO (" + content.getPrefixText() + content.getSearch() + ") ");
//
		System.out.println("> [RobotText] - Start!");
		txtBot.start();
		System.out.println("> [RobotText] - Done!");
		rw.saveJson(content);
		System.out.println("> [Opening Content.json]");
		content = fileBot.loadContentWithJson(content, rw.loadJson(fileBot.getFolderResource() + "content.json"));

		System.out.println("> [Done!]");

		System.out.println("> [RobotImage] - Start!");

		imgBot.start();
		System.out.println("> [RobotImage] - Done!");

		System.out.println("> [RobotVideo] - Start!");
		videoBot.start();
		System.out.println("> [RobotVideo] - Done!");

		System.out.println("[Saving Content in JSON...]");
		rw.saveJson(content);

		System.out.println("> [RobotYoutube] - Start!");
//		content = fileBot.loadContentWithJson(content, rw.loadJson(fileBot.getFolderResource() + "content.json"));
		ytBot.start();
		System.out.println("> [RobotYoutube] - Done!");

	}
}
