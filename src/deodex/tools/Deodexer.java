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
package deodex.tools;

import java.io.File;
import java.io.IOException;

import deodex.S;
import deodex.SessionCfg;

public class Deodexer {

	public static boolean deodexApk(File odexFile, File dexFile) {
		String cmd[] = { "java", "-jar", new File(S.OAT2DEX_JAR).getAbsolutePath(), odexFile.getAbsolutePath(),
				S.bootTmpDex.getAbsolutePath() };
		// for logging 
		String command = "";
		for (String s : cmd){
			command = command +" "+ s;
		}
		Logger.writLog("Running command : "+command+"");
		
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return dexFile.exists();
	}

	public static boolean deodexApkFailSafe(File odexFile, File dexFile) {
		File smaliFolder = new File(dexFile.getParentFile().getAbsolutePath() + File.separator
				+ dexFile.getName().substring(0, dexFile.getName().lastIndexOf(".")));
		smaliFolder.getParentFile().mkdirs();
		// baksmali command
		String[] cmd = { "java", "-jar", new File(S.BACKSMALI_JAR).getAbsolutePath(), "-x", "-c", "boot.oat", "-d",
				S.bootTmp.getParentFile().getAbsolutePath(), odexFile.getAbsolutePath(), "-o",
				smaliFolder.getAbsolutePath() };

		
		// smalicommand
		String[] cmd2 = { "java", "-jar", new File(S.SMALI_JAR).getAbsolutePath(), "-a", "" + SessionCfg.getSdk(), "-o",
				dexFile.getAbsolutePath(), smaliFolder.getAbsolutePath() };
		

		Process p;

		try {
			// logging> 
			String command = "";
			for (String s : cmd){
				command = command +" "+ s;
			}
			Logger.writLog("Running command : "+command+"");
			// </ligging
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!smaliFolder.exists()) {
			Logger.logToStdIO("Failed at baksmali " + odexFile.getName());
			return false;
		}
		try {
			// logging >
			String command2 = "";
			for (String s : cmd2){
				command2 = command2+" " + s;
			}
			Logger.writLog("Running command : "+command2+"");
			// </logging 
			p = Runtime.getRuntime().exec(cmd2);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return dexFile.exists();
	}

	public static boolean deoDexApkLegacy(File odexFile, File classesFile) {
		classesFile.getParentFile().mkdirs();
		File tempSmali = new File(odexFile.getParentFile().getAbsolutePath() + File.separator
				+ odexFile.getName().substring(0, odexFile.getName().lastIndexOf(".odex")));
		tempSmali.getParentFile().mkdirs();
		String[] cmd = { "java", "-jar", new File(S.BACKSMALI_JAR).getAbsolutePath(), "-a", "" + SessionCfg.getSdk(),
				"-d", S.bootTmpDex.getAbsolutePath(), "-x", odexFile.getAbsolutePath(), "-o",
				tempSmali.getAbsolutePath() };
		String[] cmd2 = { "java", "-jar", new File(S.SMALI_JAR).getAbsolutePath(), "-a", "" + SessionCfg.getSdk(), "-o",
				classesFile.getAbsolutePath(), tempSmali.getAbsolutePath() };
		// TODO search further info (can apks here have 2 classes.dex ? if so
		// what should we do here ?) XXX: there is none that I know about !
		Process p;

		// logging> 
		String command = "";
		for (String s : cmd){
			command = command +" "+ s;
		}
		Logger.writLog("Running command : "+command+"");
		// </ligging

		try {
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			if (!tempSmali.exists()) {
				return false;
			}
			// logging >
			String command2 = "";
			for (String s : cmd2){
				command2 = command2 +" "+ s;
			}
			Logger.writLog("Running command : "+command2+"");
			// </logging
			
			p = Runtime.getRuntime().exec(cmd2);
			p.waitFor();
			if (!classesFile.exists()) {
				return false;
			}

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FilesUtils.deleteRecursively(tempSmali);
		return classesFile.exists();
	}

	public static boolean oat2dexBoot(File bootOat) {
		String[] cmd = { "java", "-jar", new File(S.OAT2DEX_JAR).getAbsolutePath(), "boot", bootOat.getAbsolutePath() };
		// logging> 
		String command = "";
		for (String s : cmd){
			command = command+" " + s;
		}
		Logger.writLog("Running command : "+command+"");
		// </ligging
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return S.bootTmpDex.exists();
	}

	public static boolean signApk(File in, File out) throws IOException, InterruptedException {
		String[] cmd = { "java", "-jar", new File(S.SIGN_APK).getAbsolutePath(),
				new File(S.TEST_KEY_X509).getAbsolutePath(), new File(S.TEST_KEY_PK8).getAbsolutePath(),
				in.getAbsolutePath(), out.getAbsolutePath() };
		// logging> 
		String command = "";
		for (String s : cmd){
			command = command+" " + s;
		}
		Logger.writLog("Running command : "+command+"");
		// </ligging
		Process p;
		p = Runtime.getRuntime().exec(cmd);
		p.waitFor();

		return out.exists();
	}

}
