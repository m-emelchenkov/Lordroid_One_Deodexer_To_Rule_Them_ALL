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

import deodex.tools.PathUtils;

public class S {
	public static final String APK_EXT = ".apk";
	public static final String ODEX_EXT = ".odex";
	public static final String COMP_ODEX_EXT = ".odex.xz";
	public static final String SYSTEM_APP = "app";
	public static final String SYSTEM_PRIV_APP = "priv-app";
	public static final String SYSTEM_BUILD_PROP = "build.prop";
	public static final String SYSTEM_FRAMEWORK = "framework";
	public static final String SYSTEM_FRAMEWORK_BOOT = "boot.oat";
	public static final String SDK_LEVEL_PROP = "ro.build.version.sdk";

	public static final String DEX_EXT = ".dex";
	public static final String DEX2_EXT = "-classes2.dex";
	public static final String CLASSES = "classes.dex";
	public static final String CLASSES_2 = "classes2.dex";

	public static final String WORKER1 = "worker1";
	public static final String WORKER2 = "worker2";
	public static final String WORKER3 = "worker3";
	public static final String WORKER4 = "worker4";
	public static final String TMP = System.getProperty("java.io.tmpdir") + File.separator + "L U B D";

	public static final String LANGUAGE_PROP = "file.launguage";
	public static final String APP_NAME = "app.name";
	public static final String APP_WELCOME = "app.welcome";
	public static final String APP_LANG_BOX_LAB = "app.langBoxLab";
	public static final String APP_NEXT_BTN = "app.buttonNext";
	public static final String APP_WELCOME_MESSAGE = "app.welcome.message";
	public static final String BROWSE_FEILD = "browse.Field";
	public static final String LOG_WARNING = "log.warning";
	public static final String LOG_ERROR = "log.error";
	public static final String LOG_INFO = "log.info";
	public static final String LOG_SUCCESS = "log.success";
	public static final String LOG_FAIL = "log.fail";
	public static final String LOG_NO_BUILD_PROP = "log.no.build.prop";
	public static final String CANT_READ_SDK_LEVEL = "log.could.not.read.sdk_level";
	public static final String LOG_SYSTEM_APP_FOUND = "log.app.found";
	public static final String LOG_SYSTEM_APP_NOT_FOUND = "log.app.not.found";
	public static final String LOG_NOT_A_SYSTEM_FOLDER = "log.not.a.system.folder";

	public static final String ENGLISH = "English";
	public static final String FRENCH = "Français";
	public static final String ARABIC = "العربية";

	public static final String CFG_CUR_LANG = "cfg.current.language";
	public static final String CFG_HOST_OS = "cfg.host.os";

	public static final String WINDOWS = "windows";
	public static final String LINUX = "linux";
	public static final String MAC = "osx";

	public static final String OAT2DEX_JAR = PathUtils.getExcutionPath()+File.separator+"bins/oat2dex/oat2dex.jar";
	public static final String BACKSMALI_JAR = PathUtils.getExcutionPath()+File.separator+"bins/smali_backsmali/baksmali.jar";
	public static final String SMALI_JAR = PathUtils.getExcutionPath()+File.separator+"bins/smali_backsmali/smali.jar";
	public static final String ZIPALIGN_BIN = PathUtils.getExcutionPath()+File.separator+"bins/native/zipAlign";
	public static final String SIGN_APK = PathUtils.getExcutionPath()+File.separator+"bins/sign/signapk.jar";
	public static final String TEST_KEY_PK8 = PathUtils.getExcutionPath()+File.separator+"bins/sign/testkey.pk8";
	public static final String TEST_KEY_X509 = PathUtils.getExcutionPath()+File.separator+"bins/sign/testkey.x509.pem";

	// temporary folders
	public static File worker1Folder = new File(TMP + File.separator + WORKER1);
	public static File worker2Folder = new File(TMP + File.separator + WORKER2);
	public static File worker3Folder = new File(TMP + File.separator + WORKER3);
	public static File worker4Folder = new File(TMP + File.separator + WORKER4);
	public static File bootTmp = new File(TMP + File.separator + File.separator + "boot" + File.separator + "boot.oat");
	public static File bootTmpDex = new File(TMP + File.separator + File.separator + "boot" + File.separator + "dex");

	public static final String[] ARCH = { "arm", "arm64", "mips", "mips64", "x86", "x86_64" };

	public static void initTempFolders() {
		new File(TMP + File.separator + WORKER1).mkdirs();
		new File(TMP + File.separator + WORKER3).mkdirs();
		new File(TMP + File.separator + WORKER2).mkdirs();
		new File(TMP + File.separator + WORKER4).mkdirs();
		new File(TMP + File.separator + "boot").mkdirs();
	}
}
