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

import java.io.File;
import java.util.ArrayList;

import deodex.tools.CmdUtils;
import deodex.tools.Logger;
import deodex.tools.Os;
import deodex.tools.PathUtils;
import deodex.tools.PropReader;
import deodex.tools.StringUtils;

/**
 * @author lord-ralf-adolf
 *
 */
public class Cfg {
	public static final String CFG_PATH = PathUtils.getExcutionPath() + File.separator + "cfg/app_config.rsx";
	private static final String LANG_FOLDER = PathUtils.getExcutionPath() + File.separator + "lang";
	private static final String SHOW_DEODEX_ALERT = "show.deodex.alert";
	private static final String SHOW_EXIT_ALERT = "show.exit.alert";
	private static final String SHOW_THREAD_ALERT = "show.thread.alert";
	private static final String MAX_JOBS_PROP = "max.job.prop";
	private static final String FONT_NAME_PROP = "app.font";
	private static final String HEAP_SIZE_PROP = "max.heap.size";
	private static final String COMP_METHOD_PROP = "compression.method";
	
	
	private static String currentLang;
	private static String currentFont;

	private static int showDeodexAlert = 1;
	private static int showExitAlert = 1;
	private static int showThreadAlert = 1;
	private static int maxJobs = Cfg.getIdealMaxThread();
	private static String maxHeadSize = S.DEFAULT_HEAP_SIZE;
	private static int compresionMethod = S.AAPT_METHOD;
	private static ArrayList<File> langFiles = new ArrayList<File>();
	private static ArrayList<String> availableLang = new ArrayList<String>();
	
	
	
	
	/**
	 * returns weither or not show the dialog to the user
	 * 
	 * @return showDeodexAlert
	 */
	public static boolean doShowDeodexAlert() {
		return showDeodexAlert == 1;
	}

	/**
	 * returns weither or not show the dialog to the user
	 * 
	 * @return showExitAlert
	 */
	public static boolean doShowExitAlert() {
		return showExitAlert == 1;
	}

	/**
	 * 
	 * @return doShowThreadAlert 
	 */
	public static boolean doShowThreadAlert() {
		return showThreadAlert == 1;
	}

	/**
	 * 
	 * @return availableLangs the available language files 
	 */
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

	/**
	 * @return currentLang the String of the name of the current Language
	 */
	public static String getCurrentLang() {
		return currentLang;
	}

	/**
	 * 
	 * @return langFile the lang file to use for strings 
	 */
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
	 * 
	 * @return maxJobs the max jobs to use
	 */
	public static int getMaxJobs() {
		return Cfg.maxJobs;
	}

	/**
	 * 
	 * @return OsName os name 
	 */
	public static String getOs() {
		if (Os.isLinux()) {
			return S.LINUX;
		} else if (Os.isWindows()) {
			return S.WINDOWS;
		} else if (Os.isMac()) {
			return S.MAC;
		}
		return null;
	}

	/**
	 * 
	 * @return isfirstLaunch
	 * 						  returns the logical value of this question : is the application launching for the first time ?
	 */
	public static boolean isFirstLaunch() {
		return !new File(CFG_PATH).exists();
	}

	public static void readCfg() {
		
		// read current launguage 
		try {
		currentLang = PropReader.getProp(S.CFG_CUR_LANG, new File(CFG_PATH));
		if (currentLang == null)
			currentLang = S.ENGLISH;
		} catch (Exception e){
			e.printStackTrace();
			Cfg.setCurrentLang(S.ENGLISH);
		}
		
		/**
		 * is previous version those values was not there don't force the user
		 * to use a new configuration instead lets try to catch prop not Found
		 * exception
		 */
		
		// read showDeodexAlert
		try {
			showDeodexAlert = Integer.parseInt(PropReader.getProp(SHOW_DEODEX_ALERT, new File(CFG_PATH)));
		} catch (Exception e) {
			e.printStackTrace();
			Cfg.setShowDeodexAlert(true);
			Logger.writLog("[Cfg][EX]" + e.getStackTrace());
		}

		// read show ExitDialog ?
		try {
			showExitAlert = Integer.parseInt(PropReader.getProp(SHOW_EXIT_ALERT, new File(CFG_PATH)));
		} catch (Exception e) {
			e.printStackTrace();
			Cfg.setShowExitAlert(true);
			Logger.writLog("[Cfg][EX]" + e.getStackTrace());
		}
		
		// read showallertdialog ?
		try {
			showThreadAlert = Integer.parseInt(PropReader.getProp(SHOW_THREAD_ALERT, new File(CFG_PATH)));
		} catch (Exception e) {
			e.printStackTrace();
			Cfg.setShowThreadAlert(true);
			Logger.writLog("[Cfg][EX]" + e.getStackTrace());
		}
		
		// read max jobs
		try {
			Cfg.maxJobs = Integer.parseInt(PropReader.getProp(MAX_JOBS_PROP, new File(CFG_PATH)));
			Cfg.setMaxJobs(maxJobs);
		} catch (Exception e) {
			e.printStackTrace();
			Cfg.setMaxJobs(Cfg.getIdealMaxThread());
			Logger.writLog("[Cfg][EX]" + e.getStackTrace());
		}
		
		// read font 
		try {
			Cfg.currentFont = PropReader.getProp(Cfg.FONT_NAME_PROP, new File(Cfg.CFG_PATH));
			if(currentFont != null)
				R.setFont(currentFont);
			else
				R.setFont("Arial");
		} catch (Exception e){
			e.printStackTrace();
			Cfg.currentFont = "Arial";
			R.setFont("Arial");
		}
		
		// read heapSize prop
		 try {
			 Cfg.maxHeadSize = PropReader.getProp(Cfg.HEAP_SIZE_PROP, new File(Cfg.CFG_PATH));
			 if(Cfg.maxHeadSize == null){
				 Cfg.maxHeadSize = S.DEFAULT_HEAP_SIZE;
			 }
		 } catch (Exception e){
			 e.printStackTrace();
			 Cfg.maxHeadSize = S.DEFAULT_HEAP_SIZE;
		 }
		 
			// read compMethod
			try {
				Cfg.compresionMethod = Integer.parseInt(PropReader.getProp(Cfg.COMP_METHOD_PROP, new File(CFG_PATH)));
			} catch (Exception e) {
				e.printStackTrace();
				Cfg.compresionMethod = 0;
				Logger.writLog("[Cfg][EX]" + e.getStackTrace());
			}
	}

	/**
	 * @param currentLang
	 *            the currentLang to set
	 */
	public static void setCurrentLang(String currentLang) {
		Cfg.currentLang = currentLang;
		PropReader.writeProp(S.CFG_CUR_LANG, currentLang, new File(CFG_PATH));
	}

	public static void setMaxJobs(int i) {
		Cfg.maxJobs = i;
		PropReader.writeProp(MAX_JOBS_PROP, "" + i, new File(CFG_PATH));
	}

	/**
	 * set weither or not show a dialog to the user
	 * 
	 * @param show
	 */
	public static void setShowDeodexAlert(boolean show) {
		if (show) {
			showDeodexAlert = 1;
		} else {
			showDeodexAlert = 0;
		}
		PropReader.writeProp(SHOW_DEODEX_ALERT, "" + showDeodexAlert, new File(Cfg.CFG_PATH));
	}

	/**
	 * set weither or not show a dialog to the user
	 * 
	 * @param show
	 */
	public static void setShowExitAlert(boolean show) {
		if (show) {
			showExitAlert = 1;
		} else {
			showExitAlert = 0;
		}
		PropReader.writeProp(SHOW_EXIT_ALERT, "" + showExitAlert, new File(Cfg.CFG_PATH));

	}

	/**
	 * set weither or not show a dialog to the user
	 * 
	 * @param show
	 */
	public static void setShowThreadAlert(boolean show) {
		if (show) {
			showThreadAlert = 1;
		} else {
			showThreadAlert = 0;
		}
		PropReader.writeProp(SHOW_THREAD_ALERT, "" + showThreadAlert, new File(Cfg.CFG_PATH));
	}

	/**
	 * @return the currentFont
	 */
	public static String getCurrentFont() {
		return currentFont;
	}

	/**
	 * @param currentFont the currentFont to set
	 */
	public static void setCurrentFont(String currentFont) {
		Cfg.currentFont = currentFont;
		R.setFont(currentFont);
		PropReader.writeProp(Cfg.FONT_NAME_PROP, currentFont, new File(Cfg.CFG_PATH));
	}

	/**
	 * 
	 * writes the cfg file to disque 
	 */

	public static void writeCfgFile() {
		PropReader.writeProp(S.CFG_CUR_LANG, currentLang, new File(CFG_PATH));
		PropReader.writeProp(S.CFG_HOST_OS, Cfg.getOs(), new File(CFG_PATH));
		PropReader.writeProp(SHOW_DEODEX_ALERT, "" + showDeodexAlert, new File(Cfg.CFG_PATH));
		PropReader.writeProp(MAX_JOBS_PROP, ""+Cfg.getMaxJobs(), new File(Cfg.CFG_PATH));
		PropReader.writeProp(Cfg.FONT_NAME_PROP, currentFont, new File(Cfg.CFG_PATH));
		PropReader.writeProp(Cfg.HEAP_SIZE_PROP, Cfg.maxHeadSize, new File(Cfg.CFG_PATH));
		PropReader.writeProp(Cfg.COMP_METHOD_PROP, ""+Cfg.compresionMethod, new File(Cfg.CFG_PATH));
	}

	/**
	 * 
	 * @return defaultMaxThread max thread count 
	 */
	public static int getIdealMaxThread(){
		int max = 2;
		int cpu = 2;
		try {
			cpu = HostInfo.availableCpus();
		} catch (Exception e){
			cpu = 2;
		}
		
		if( cpu == 1){
			max = 1;
		} else if (cpu == 2){
			max = 2;
		} else if(cpu > 2){
			max = 4;
		}
		return max;
	}
	
	public static String[] getAvailableCompMrthods(){

		return null;
	}
	
	/**
	 * @return the maxHeadSize
	 */
	public static String getMaxHeadSize() {
		return maxHeadSize;
	}

	public static String getMaxHeadSizeArg() {
		String heapSizeArg = "";
		for (int i = 0 ; i < S.HEAP_SIZES.length ; i++ ){
			if(Cfg.maxHeadSize.equals(S.HEAP_SIZES[i])){
				return S.HEAP_SIZES_ARG[i];
			}
		}
		return heapSizeArg;
	}
	
	/**
	 * @return the maxHeadSize
	 */
	public static void setMaxHeadSize(String heapSize) {
		 Cfg.maxHeadSize = heapSize;
		 PropReader.writeProp(Cfg.HEAP_SIZE_PROP, Cfg.maxHeadSize, new File(Cfg.CFG_PATH));
	}
	
	/**
	 * @return the compresionMathod
	 */
	public static int getCompresionMathod() {
		return compresionMethod;
	}

	/**
	 * @param compresionMathod the compresionMathod to set
	 */
	public static void setCompresionMathod(int compresionMathod) {
		Cfg.compresionMethod = compresionMathod;
		PropReader.writeProp(Cfg.COMP_METHOD_PROP, ""+Cfg.compresionMethod, new File(Cfg.CFG_PATH));
	}

	public static boolean is7ZipAvailable(){
		String SevenZipBin = PathUtils.getSevenZBinPath();
		String cmd[] = {SevenZipBin,"-h"};
		
		return CmdUtils.runCommand(cmd) != 2;
	}
	
	public static boolean isAaptAvailable(){
		String[] cmd = {S.getAapt(),"v"};
		return (CmdUtils.runCommand(cmd)!= 2);
	}

}
