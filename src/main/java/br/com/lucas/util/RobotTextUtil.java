package br.com.lucas.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class RobotTextUtil {
	public static String removeBlanksAndMarkdownLines(String content) {

		List<String> contentLines = Arrays.asList(content.split("\n"));

		contentLines = contentLines.stream().filter(line -> line.trim().length() != 0 && !line.startsWith("="))
				.collect(Collectors.toList());
		content = StringUtils.join(contentLines, " ");
		
		return content;
	}
	public static String removeDatesInParentheses(String content) {
			
		 
		return content.replaceAll("\\((.*?)\\)", "");
		
	}

}
