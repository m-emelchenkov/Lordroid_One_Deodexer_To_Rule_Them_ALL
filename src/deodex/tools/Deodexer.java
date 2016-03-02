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
package deodex.tools;

import java.io.File;
import java.io.IOException;

import deodex.S;
import deodex.SessionCfg;

public class Deodexer {

	public static boolean deodexApk(File odexFile, File dexFile) {
		String cmd[] = { "java", "-jar", new File(S.OAT2DEX_JAR).getAbsolutePath(), odexFile.getAbsolutePath(),
					S.bootTmpDex.getAbsolutePath() };
		CmdUtils.runCommand(cmd);
	
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

		CmdUtils.runCommand(cmd);

		if (!smaliFolder.exists()) {
			Logger.logToStdIO("Failed at baksmali " + odexFile.getName());
			return false;
		}
		CmdUtils.runCommand(cmd2);

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

		CmdUtils.runCommand(cmd);
		CmdUtils.runCommand(cmd2);

		FilesUtils.deleteRecursively(tempSmali);
		return classesFile.exists();
	}

	public static boolean oat2dexBoot(File bootOat) {
		// FIXME : don't forget this !
		String[] cmd = { /**"java", "-jar", new File(S.OAT2DEX_JAR).getAbsolutePath(), */"boot", bootOat.getAbsolutePath() };
//		try {
//		org.rh.smaliex.Main.main(cmd);
//		} catch (Exception e) {
//
//			String[] cmd1 = { "java", "-jar", new File(S.OAT2DEX_JAR).getAbsolutePath(), "boot", bootOat.getAbsolutePath() };
//			CmdUtils.runCommand(cmd1);
//
//		}
		String[] cmd1 = { "java", "-jar", new File(S.OAT2DEX_JAR).getAbsolutePath(), "boot", bootOat.getAbsolutePath() };
		CmdUtils.runCommand(cmd1);
		return S.bootTmpDex.exists();
	}

	public static boolean signApk(File in, File out) throws IOException, InterruptedException {
		String[] cmd = { "java", "-jar", new File(S.SIGN_APK).getAbsolutePath(),
				new File(S.TEST_KEY_X509).getAbsolutePath(), new File(S.TEST_KEY_PK8).getAbsolutePath(),
				in.getAbsolutePath(), out.getAbsolutePath() };

		CmdUtils.runCommand(cmd);

		return out.exists();
	}

}
