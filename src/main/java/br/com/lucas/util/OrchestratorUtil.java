package br.com.lucas.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import br.com.lucas.applicaiton.Content;

public class OrchestratorUtil {

	private static Scanner scan = new Scanner(System.in);

	public static String selectLanguage() {
		String[] prefixes = { "pt", "en" };

		Map<Integer, String> map = new HashMap<>();

		for (int i = 0; i < prefixes.length; i++) {
			map.put(i, prefixes[i]);
		}

		System.out.println("Escolha a Linguagem: ");

		for (int i = 0; i < prefixes.length; i++) {
			System.out.println(i + " - " + map.get(i));
		}

		int index = scan.nextInt();
		return map.get(index);
	}

	public static String searchTerm() {
		System.out.print("Informe termo para pesquisa na Wikipedia: ");
		return scan.nextLine();
	}

	public static String prefixTerm(Content content) {

		String[] prefixes;
		String[] prefixesEn = { "Who is ", "What is ", "The history of " };
		String[] prefixesPt = { "Quem é ", "O que é ", "A História de " };

		if (content.getLanguage().equals("en"))
			prefixes = prefixesEn;
		else
			prefixes = prefixesPt;

		Map<Integer, String> map = new HashMap<>();

		for (int i = 0; i < prefixes.length; i++) {
			map.put(i, prefixes[i]);
		}

		System.out.println("Escolha um prefix: ");

		for (int i = 0; i < prefixes.length; i++) {
			System.out.println(i + " - " + map.get(i));
		}

		int index = scan.nextInt();

		return map.get(index);

	}
}
