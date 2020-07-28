package br.com.lucas.util;

import java.util.List;

import br.com.lucas.enums.TemplateSettings;

public class RobotVideoUtil {

	public static TemplateSettings[] templateSenttings() {
		TemplateSettings[] templateSettings = { 
				TemplateSettings.CENTER_1920x400, 
				TemplateSettings.CENTER_1920x1080,
				TemplateSettings.WEST_800x1080, 
				TemplateSettings.CENTER_1920x400, 
				TemplateSettings.CENTER_1920x1080,
				TemplateSettings.WEST_800x1080, 
				TemplateSettings.CENTER_1920x400 };
		
		return templateSettings;
	}

}
