package br.com.lucas.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OrchestratorUtil {
	
	private static Scanner scan =new Scanner(System.in);
	
	public static String serchTerm() {
		System.out.print("Informe termo para pesquisa na Wikipedia: ");
		return scan.nextLine();
	}

	public static String prefixTerm() {

		String[] prefixes = { "Who is", "What is?", "The history of" };

		Map<Integer, String> map = new HashMap<>();

		for (int i = 0; i < prefixes.length; i++) {
			map.put(i, prefixes[i]);
		}

		System.out.println("Escolha um prefix: ");

		for (int i = 0; i < prefixes.length; i++) {
			System.out.println(i + "- " + map.get(i));
		}

		int index = scan.nextInt();

		
		return map.get(index);

	}
}
