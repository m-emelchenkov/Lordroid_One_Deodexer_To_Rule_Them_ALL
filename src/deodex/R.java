/*
 * 
 * 
 * Copyright 2016 Rachid Boudjelida <rachidboudjelida@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package deodex;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import deodex.tools.StringUtils;

public class R {
	public static final Font TITLE_FONT = new Font("Courier 10 Pitch", Font.BOLD, 40);
	public static final Font NORMAL_FONT = new Font("Courier 10 Pitch", Font.BOLD, 16);
	public static final Font SMALL_FONT = new Font("Arial", Font.BOLD, 12);

	public static final Font COURIER_NORMAL = new Font("Courier 10 Pitch", Font.PLAIN, 18);
	public static final Font COURIER_LOGGER = new Font("Courier 10 Pitch", Font.PLAIN, 16);

	// public static final LogoPane LOGO_PANE =
	public static final String LOG_HEADER = "[R]";

	public static final Color BUTTONS_BACK_COLOR = new Color(89, 195, 216);
	public static final Color FIELDS_BACK_COLOR = new Color(220, 237, 193);
	public static final Color PANELS_BACK_COLOR = new Color(206, 194, 229);
	
	
	private static ArrayList<String> strings = new ArrayList<String>();
	public static Image borderRight;
	public static Image borderLeft;
	public static Image icon;

	public static String getString(String prop) {
		String value = null;
		for (String str : strings) {
			String tmp = StringUtils.getCropString(str, str.lastIndexOf("="));
			tmp = StringUtils.removeSpaces(tmp);
			if (tmp.equals(prop)) {
				value = StringUtils.getSubString(str, str.lastIndexOf("="));
				break;
			}

		}

		return legalize(value);
	}

	public static void initResources() {
		File langFile = Cfg.getLangFile();
		BufferedReader br = null;
		strings = new ArrayList<String>();
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(langFile));
			br = new BufferedReader(
					new InputStreamReader(new BufferedInputStream(dis), Charset.forName("UTF-8").newDecoder()));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0 && line.charAt(0) != '#' && line.lastIndexOf("=") > 0) {
					strings.add(line);
					// Logger.logToStdIO(LOG_HEADER + Logger.INFO + " String
					// loaded : " + line);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			borderRight = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("images/borderRight.png"));
			borderLeft = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResource("images/borderLeft.png"));
			icon = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("images/icon.png"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String legalize(String str) {
		String tmp = "";
		for (int i = 0; i < str.length() - 1; i++) {
			String spec = "" + str.charAt(i) + str.charAt(i + 1);
			if (spec.equals("<>")) {
				tmp = tmp + '\n';
				i++;
			} else {
				tmp = tmp + str.charAt(i);
			}
		}
		tmp = tmp + str.charAt(str.length() - 1);
		// Logger.logToStdIO(LOG_HEADER + Logger.INFO + " String legalized :" +
		// tmp);
		return tmp;
	}

}
