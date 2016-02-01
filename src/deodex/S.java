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

import java.io.File;

public class S {
	public static final String APK_EXT = ".apk";
	public static final String ODEX_EXT = ".odex";
	public static final String COMP_ODEX_EXT = ".odex.xz";

	public static final String DEX_EXT = ".dex";
	public static final String DEX2_EXT = "-classes2.dex";
	public static final String CLASSES = "classes.dex";
	public static final String CLASSES_2 = "classes2.dex";

	public static final String WORKER1 = "worker1";
	public static final String WORKER2 = "worker2";
	public static final String WORKER3 = "worker3";
	public static final String WORKER4 = "worker4";
	public static final String TMP = System.getProperty("java.io.tmpdir") + File.separator + "LUBD";

	public static final String LANGUAGE_PROP = "file.launguage";
	public static final String APP_NAME = "app.name";
	public static final String APP_WELCOME = "app.welcome";
	public static final String APP_LANG_BOX_LAB = "app.langBoxLab";
	public static final String APP_NEXT_BTN = "app.buttonNext";
	public static final String APP_WELCOME_MESSAGE = "app.welcome.message";
	public static final String BROWSE_FEILD = "browse.Field";
	
	public static final String ENGLISH = "English";
	public static final String FRENCH = "Français";
	public static final String ARABIC = "العربية";

	public static final String CFG_CUR_LANG = "cfg.current.language";
	public static final String CFG_HOST_OS = "cfg.host.os";

	public static final String WINDOWS = "windows";
	public static final String LINUX = "linux";
	public static final String MAC = "osx";

	public static void initTempFolders() {
		new File(TMP + File.separator + WORKER1).mkdirs();
		new File(TMP + File.separator + WORKER3).mkdirs();
		new File(TMP + File.separator + WORKER2).mkdirs();
		new File(TMP + File.separator + WORKER4).mkdirs();

	}
}
