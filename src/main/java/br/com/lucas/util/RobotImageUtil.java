package br.com.lucas.util;

import java.io.File;

import javax.imageio.ImageIO;

public class RobotImageUtil {
	public static boolean imageIsCorruped(String dest) {
		boolean isValid = false;
		try {
			ImageIO.read(new File(dest)).flush();
		} catch (Exception e) {
			isValid = true;
		}
		return isValid;
	}
}
