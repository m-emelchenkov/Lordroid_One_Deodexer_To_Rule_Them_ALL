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
import java.util.ArrayList;

import deodex.tools.Os;
import deodex.tools.PropReader;
import deodex.tools.StringUtils;

/**
 * @author lord-ralf-adolf
 *
 */
public class Cfg {
	private static final String CFG_PATH = "cfg/app_config.rsx";
	private static final String LANG_FOLDER = "lang";
	
	
	private static String currentLang;
	
	private static ArrayList<File> langFiles = new ArrayList<File>();
	private static ArrayList<String> availableLang = new ArrayList<String>();

	public static boolean isFirstLaunch() {
		return !new File(CFG_PATH).exists();
	}

	/**
	 * 
	 * @return available languages as String if there is none retrrns null
	 */

	public static void writeCfgFile(){
		PropReader.writeProp(S.CFG_CUR_LANG, currentLang, new File(CFG_PATH));
		
	}
	
	public static void readCfg(){
		currentLang = PropReader.getProp(S.CFG_CUR_LANG, new File(CFG_PATH));
	}
	public static ArrayList<String> getAvailableLaunguages() {
		File langFolder = new File(LANG_FOLDER);
		File lang[] = langFolder.listFiles();

		langFiles = new ArrayList<File>();
		for (File f : lang) {
			String ext;
			if (f.isFile()) {
				String name = f.getName();
				ext = StringUtils.getLastchars(name, 4);
				if (ext.equals("prop"))
					langFiles.add(f);
			}

		}
		availableLang = new ArrayList<String>();

		for (File f : langFiles) {
			availableLang.add(PropReader.getProp(S.LANGUAGE_PROP, f));
		}

		return availableLang;

	}

	public static File getLangFile() {
		File langFolder = new File(LANG_FOLDER);
		File lang[] = langFolder.listFiles();
		langFiles = new ArrayList<File>();
		for (File f : lang) {
			String ext;
			if (f.isFile()) {
				String name = f.getName();
				ext = StringUtils.getLastchars(name, 4);
				if (ext.equals("prop"))
					langFiles.add(f);
			}

		}
		availableLang = new ArrayList<String>();

		for (File f : langFiles) {
			availableLang.add(PropReader.getProp(S.LANGUAGE_PROP, f));
		}

		File tmp = null;
		if (currentLang == null)
			return new File("lang/en.prop");
		for (int i = 0; i < availableLang.size(); i++) {
			if (availableLang.get(i).equals(currentLang)) {
				tmp = langFiles.get(i);
				break;
			}
		}
		return tmp;
	}

	/**
	 * @return the currentLang
	 */
	public static String getCurrentLang() {
		return currentLang;
	}

	/**
	 * @param currentLang
	 *            the currentLang to set
	 */
	public static void setCurrentLang(String currentLang) {
		Cfg.currentLang = currentLang;
	}

	public static String getOs(){
		if(Os.isLinux()) {
			return S.LINUX;
		} else if (Os.isWindows()){
			return S.WINDOWS;
		} else if (Os.isMac()){
			return S.MAC;
		}
		return null;
	}
	
}
