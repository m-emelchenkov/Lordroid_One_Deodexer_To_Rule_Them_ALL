/*
 *  Lordroid One Deodexer To Rule Them All
 * 
 *  Copyright 2016 Rachid Boudjelida <rachidboudjelida@gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

import deodex.tools.Logger;
import deodex.tools.StringUtils;

public class R {
	private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 40);
	public static final Font SMALL_TITLE_FONT = new Font("Arial", Font.BOLD, 18);
	private static final Font NORMAL_FONT = new Font("Arial", Font.BOLD, 16);
	private static final Font SMALL_FONT = new Font("Arial", Font.BOLD, 12);

	private static final Font COURIER_NORMAL = new Font("Arial", Font.BOLD, 18);
	private static final Font COURIER_LOGGER = new Font("Arial", Font.BOLD, 16);

	private static Font titleFont;
	private static Font normalFont;
	private static Font smallFont;
	private static Font couriernormal;
	private static Font courierLogger;

	// public static final LogoPane LOGO_PANE =
	public static final String LOG_HEADER = "[R]";

	public static final Color BUTTONS_BACK_COLOR = new Color(89, 195, 216);

	public static final Color FIELDS_BACK_COLOR = new Color(220, 237, 193);

	public static final Color PANELS_BACK_COLOR = new Color(189, 195, 199);

	private static ArrayList<String> strings = new ArrayList<String>();

	public static Image icon;

	/**
	 * @return the courierLogger
	 */
	public static Font getCourierLogger() {
		if (courierLogger == null) {
			return R.COURIER_LOGGER;
		}
		return courierLogger;
	}

	/**
	 * @return the couriernormal
	 */
	public static Font getCouriernormal() {
		if (couriernormal == null)
			return R.COURIER_NORMAL;
		return couriernormal;
	}
	/**
	 * @return the normalFont
	 */
	public static Font getNormalFont() {
		if (normalFont == null)
			return R.NORMAL_FONT;
		return normalFont;
	}
	/**
	 * @return the smallFont
	 */
	public static Font getSmallFont() {
		if (R.smallFont == null)
			return R.SMALL_FONT;
		return smallFont;
	}

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
		System.out.println(value);
		return legalize(value);
	}
	/**
	 * @return the titleFont
	 */
	public static Font getTitleFont() {
		if (titleFont == null) {
			return R.TITLE_FONT;
		}
		return titleFont;
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
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Logger.writLog("[R][EX]" + e.getStackTrace());
		}

		try {
			icon = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource("images/icon.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.writLog("[R][EX]" + e.getStackTrace());
		}

	}

	private static String legalize(String str) throws NullPointerException {
		String tmp = "";
		// if(str == null){
		// return null;
		// }
		for (int i = 0; i < str.length() - 1; i++) {
			String spec = "" + str.charAt(i) + str.charAt(i + 1);
			if (spec.equals("<>")) {
				tmp = tmp + '\n';
				i++;
			}else if (spec.equals(">>")){
				tmp = tmp +"=";
				i++;
			} else {
				tmp = tmp + str.charAt(i);
			}
		}
		tmp = tmp + str.charAt(str.length() - 1);
		return tmp;
	}

	public static void setFont(String fontName) {
		titleFont = new Font(fontName, Font.BOLD, 40);
		normalFont = new Font(fontName, Font.BOLD, 16);
		smallFont = new Font(fontName, Font.BOLD, 12);
		couriernormal = new Font(fontName, Font.BOLD, 18);
		courierLogger = new Font(fontName, Font.BOLD, 16);
	}

}
