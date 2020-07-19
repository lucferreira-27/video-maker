package br.com.lucas.applicaiton;

import java.util.Scanner;

import br.com.lucas.robots.txtBot.RobotText;
import br.com.lucas.util.OrchestratorUtil;

public class Orchestrator {
	private static Content content = new Content();
	private static RobotText txtBot;
	private static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) {
		start();
	}

	public static void start() {

		content.setSearch(OrchestratorUtil.serchTerm());

		content.setPrefixText(OrchestratorUtil.prefixTerm());
		txtBot = new RobotText(content);
		txtBot.start();
		System.out.println(content);

	}
}
