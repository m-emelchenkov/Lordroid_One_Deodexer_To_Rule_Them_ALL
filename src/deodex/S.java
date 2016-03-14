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
import java.io.IOException;

import deodex.tools.FilesUtils;
import deodex.tools.Logger;
import deodex.tools.PathUtils;

public class S {
	public static final long SAFE_HEAP_SIZE = 754974720L;
	public static final String DEFAULT_HEAP_SIZE = "512m (default)";
	public static final String[] HEAP_SIZES = {"128m","256m","512m (default)","1024m","2048m"};
	public static final String[] HEAP_SIZES_ARG = {"-Xmx128m","-Xmx256m","-Xmx512m","-Xmx1024m","-Xmx2048m"};
	public static final int SEVENZIP_METHOD = 2;
	public static final int J4ZIP_METHOD = 1;
	public static final int AAPT_METHOD = 0;

	
	public static final String APK_EXT = ".apk";
	public static final String ODEX_EXT = ".odex";
	public static final String COMP_ODEX_EXT = ".odex.xz";
	public static final String SYSTEM_APP = "app";
	public static final String SYSTEM_PRIV_APP = "priv-app";
	public static final String SYSTEM_BUILD_PROP = "build.prop";
	public static final String SYSTEM_FRAMEWORK = "framework";
	public static final String SYSTEM_FRAMEWORK_BOOT = "boot.oat";
	public static final String SYSTEM_FRAMEWORK_BOOT_ART = "boot.art";
	public static final String SDK_LEVEL_PROP = "ro.build.version.sdk";

	public static final String DEX_EXT = ".dex";
	public static final String DEX2_EXT = "-classes2.dex";
	public static final String DEX3_EXT = "-classes3.dex";
	public static final String CLASSES = "classes.dex";
	public static final String CLASSES_2 = "classes2.dex";
	public static final String CLASSES_3 = "classes3.dex";

	public static final String WORKER1 = "worker1";
	public static final String WORKER2 = "worker2";
	public static final String WORKER3 = "worker3";
	public static final String WORKER4 = "worker4";
	private static final String TMP = System.getProperty("java.io.tmpdir") + File.separator + "L U B D";

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

	private static final File ADB_BIN = new File(
			PathUtils.getExcutionPath() + File.separator + "bins/native/adb/" + Cfg.getOs() + "/adb"); 
	private static final File AAPT_BIN = new File(
			PathUtils.getExcutionPath()+"/"+ "bins/native/adb/" + Cfg.getOs() + "/aapt");
																										
	private static final String OAT2DEX_JAR = PathUtils.getExcutionPath() + File.separator + "bins/oat2dex/oat2dex.jar";
	private static final String BACKSMALI_JAR = PathUtils.getExcutionPath() + File.separator
			+ "bins/smali_backsmali/baksmali.jar";
	
	private static final String UNSQUASH_WIN = new File(
			PathUtils.getExcutionPath() + File.separator + "bins/native/squashfs/unsquashfs").getAbsolutePath();
	private static final String UNSQUASH_linux = "unsquashfs";
	private static final String UNSQUASH_mac = "unsquashfs";
	
	private static final String SMALI_JAR = PathUtils.getExcutionPath() + File.separator
			+ "bins/smali_backsmali/smali.jar";
	private static final String ZIPALIGN_BIN = PathUtils.getExcutionPath() + File.separator + "bins/native/zipAlign/"+Cfg.getOs();
	public static final String SIGN_APK = PathUtils.getExcutionPath() + File.separator + "bins/sign/signapk.jar";
	public static final String TEST_KEY_PK8 = PathUtils.getExcutionPath() + File.separator + "bins/sign/testkey.pk8";
	public static final String TEST_KEY_X509 = PathUtils.getExcutionPath() + File.separator
			+ "bins/sign/testkey.x509.pem";
	public static final File DUMMY_JAR = new File(PathUtils.getExcutionPath() + File.separator + "blanks/blank.jar");
	public static final File DUMMY_ZIP = new File(
			PathUtils.getExcutionPath() + File.separator + "blanks/blank_rom.zip");
	public static final File ZIP_OUTPUT = new File(PathUtils.getExcutionPath() + File.separator + "flashable_zips_out");
	public static final File EXTRACTED_SYSTEMS = new File(
			PathUtils.getExcutionPath() + File.separator + "extracted_system_folders");
	public static final File TOOLS_JAR = new File(PathUtils.getExcutionPath()+"/tools/tools.jar");
	// temporary folders
	private static File worker1Folder ;
	private static File worker2Folder ;
	private static File worker3Folder ;
	private static File worker4Folder ;
	private static File bootTmp ;
	private static File bootTmpDex ;
	private static File unsquash ;
	
	public static final String TMP_SFX = "LOBDTRTA";
	
	public static final String[] ARCH = { "arm64", "arm", "mips64", "mips", "x86_64", "x86" };

	/**
	 * 
	 * @param tempFolder the tempFOlder to use for rom deodexing 
	 */
	public static void setTempDir(File tempFolder){
		worker1Folder = new File(tempFolder.getAbsolutePath()+ File.separator +TMP_SFX + File.separator + WORKER1);
		worker2Folder = new File(tempFolder.getAbsolutePath()+ File.separator +TMP_SFX + File.separator + WORKER2);
		worker3Folder = new File(tempFolder.getAbsolutePath()+ File.separator +TMP_SFX + File.separator + WORKER3);
		worker4Folder = new File(tempFolder.getAbsolutePath()+ File.separator +TMP_SFX + File.separator + WORKER4);
		bootTmp = new File(tempFolder.getAbsolutePath()+ File.separator +TMP_SFX + File.separator +  "boot" + File.separator + "boot.oat");
		bootTmpDex = new File(tempFolder.getAbsolutePath()+ File.separator +TMP_SFX + File.separator + "boot" + File.separator + "dex");
		unsquash = new File(tempFolder.getAbsolutePath()+ File.separator +TMP_SFX + File.separator + "unsquash");
		File temp = worker1Folder.getParentFile();
		if(temp.exists()){
			FilesUtils.deleteRecursively(temp);
		}
		worker1Folder.mkdirs();
		worker2Folder.mkdirs();
		worker3Folder.mkdirs();
		worker4Folder.mkdirs();
		bootTmp.getParentFile().mkdirs();

	}
	
	public static String getPathExemple() {
		String path = "/something/otherthing/mysystemFolder";
		if (Cfg.getOs().equals(S.WINDOWS)) {
			path = "c:" + File.separator + "my_system_folder";
		} else if (Cfg.getOs().equals(S.LINUX)) {
			path = "/home/user/my_systemFolder";
		} else if (Cfg.getOs().equals(S.MAC)) {
			path = "/home/user/my_systemFolder";
		}
		return path;
	}

	public static String getRomExtractionFolderName() {
		File temp = new File("Rom_Extracted__lordroid");
		try {
			temp = File.createTempFile("ROM_Extracted_", "_lordroid");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp.getName();
	}

	public static void initTempFolders() {
		Logger.writLog("[S] creating temp folders ...");
		boolean w1 = new File(TMP + File.separator + WORKER1).mkdirs();
		boolean w2 = new File(TMP + File.separator + WORKER3).mkdirs();
		boolean w3 = new File(TMP + File.separator + WORKER2).mkdirs();
		boolean w4 = new File(TMP + File.separator + WORKER4).mkdirs();
		boolean b = new File(TMP + File.separator + "boot").mkdirs();
		Logger.writLog("[S] worker1 created ? " + w1 + " worker2 created ? " + w2 + " worker3 created ? " + w3
				+ " worker4 created ? " + w4 + " boot created ? " + b);
	}

	/**
	 * @return the worker1Folder
	 */
	public static File getWorker1Folder() {
		return worker1Folder;
	}

	/**
	 * @return the worker2Folder
	 */
	public static File getWorker2Folder() {
		return worker2Folder;
	}

	/**
	 * @return the worker3Folder
	 */
	public static File getWorker3Folder() {
		return worker3Folder;
	}

	/**
	 * @return the worker4Folder
	 */
	public static File getWorker4Folder() {
		return worker4Folder;
	}

	/**
	 * @return the bootTmp
	 */
	public static File getBootTmp() {
		return bootTmp;
	}

	/**
	 * @return the bootTmpDex
	 */
	public static File getBootTmpDex() {
		return bootTmpDex;
	}

	public static void setTempDir(String path) {
		S.setTempDir(new File(path));
	}
	
	/**
	 * @return the unsquash tmp folder
	 */
	public static File getUnsquash() {
		return unsquash;
	}

	
	/**
	 * 
	 * @return adbbinary the adb binary file to excute
	 */
	public static String getAdbBin(){
		return S.ADB_BIN.getAbsolutePath();
	}

	/**
	 * 
	 * @return oat2dex.jar the oat2dex File to be excuted
	 */
	public static String getAot2Dex(){
		return new File(S.OAT2DEX_JAR).getAbsolutePath();		
	}
	
	/**
	 * 
	 * @return smali the smali jar to be excuted 
	 */
	public static String getSmali(){
		return new File(S.SMALI_JAR).getAbsolutePath();
	}
	
	/**
	 * 
	 * @return baksmaliFile the baksmali jar to excute
	 */
	public static String getBaksmali(){
		return new File(S.BACKSMALI_JAR).getAbsolutePath();
	}
	

	/**
	 * 
	 * @return ziplignBin the zipalign binary to excute
	 */
	public static String getZipalign(){
		return new File(S.ZIPALIGN_BIN).getAbsolutePath();
	}

	/**
	 * 
	 * @return aaptBin to be used 
	 */
	public static String getAapt(){
		return S.AAPT_BIN.getAbsolutePath();
	}
	
	public static String getUnsquashBinary(){
		String unsquashBin = null;
		String os = Cfg.getOs();
		if(os.equals(S.WINDOWS)){
			unsquashBin = S.UNSQUASH_WIN;
		} else if (os.equals(S.LINUX)){
			unsquashBin = S.UNSQUASH_linux;
		} else if (os.equals(S.MAC)){
			unsquashBin = S.UNSQUASH_mac;
		}
		return unsquashBin;
	}
}
