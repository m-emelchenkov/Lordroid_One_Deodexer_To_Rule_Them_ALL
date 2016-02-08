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
		String cmd[] = { "java", "-jar", S.OAT2DEX_JAR, odexFile.getAbsolutePath(), S.bootTmpDex.getAbsolutePath() };
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dexFile.exists();
	}

	public static boolean deoDexApkLegacy(File odexFile, File classesFile) {
		String[] cmd = { "java", "-jar", S.BACKSMALI_JAR, "-a", "" + SessionCfg.getSdk(), "-d",
				S.bootTmp.getAbsolutePath(), "-x", odexFile.getAbsolutePath(),
				new File(odexFile.getParentFile().getAbsolutePath() + File.separator + "smali").getAbsolutePath() };

		String[] cmd2 = { "java", "-jar", S.SMALI_JAR, "-a", "" + SessionCfg.getSdk(),
				new File(odexFile.getParentFile().getAbsolutePath() + File.separator + "smali").getAbsolutePath(),
				classesFile.getAbsolutePath() };

		// TODO search further info (can apks here have 2 classes.dex ? if so
		// what should we do here ?)
		Process p;

		try {
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			if (!new File(odexFile.getParentFile().getAbsolutePath() + File.separator + "smali").exists()) {
				return false;
			}
			p = Runtime.getRuntime().exec(cmd2);
			p.waitFor();
			if (!classesFile.exists()) {
				return false;
			}

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return classesFile.exists();
	}

	public static boolean oat2dexBoot(File bootOat) {
		String[] cmd = { "java", "-jar", S.OAT2DEX_JAR, "boot", bootOat.getAbsolutePath() };
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
		Process p;
		p = Runtime.getRuntime().exec(cmd);
		p.waitFor();

		return out.exists();
	}

}
