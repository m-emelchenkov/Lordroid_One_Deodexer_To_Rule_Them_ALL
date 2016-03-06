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

import deodex.S;
import deodex.SessionCfg;

/**
 * 
 * @author lord-ralf-adolf
 *
 */
public class Deodexer {

	/**
	 * will run the oat2dex tool on the given odex file
	 * 
	 * @param odexFile
	 *            odex File to be deodexed
	 * @param dexFile
	 *            the output dex file name
	 * @return true only if the odex was deodexed
	 */
	public static boolean deodexApk(File odexFile, File dexFile) {
		String cmd[] = { "java", "-jar", new File(S.OAT2DEX_JAR).getAbsolutePath(), odexFile.getAbsolutePath(),
				S.bootTmpDex.getAbsolutePath() };
		CmdUtils.runCommand(cmd);

		return dexFile.exists();
	}

	/**
	 * will run the smali baksmali tool on the given odex file
	 * 
	 * @param odexFile
	 *            odex file to be deodexed
	 * @param dexFile
	 *            output dex file
	 * @return true only if the odex was deodexed
	 */
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
			Logger.writLog("[Deodexer][E]Failed at baksmali " + odexFile.getName());
			return false;
		}
		CmdUtils.runCommand(cmd2);

		return dexFile.exists();
	}

	/**
	 * the legacy smali/backsmali deodexing command will be run on the given
	 * odex file
	 * 
	 * @param odexFile
	 *            odex file to be deodexed
	 * @param classesFile
	 *            the output dex file
	 * @return true only if the odex was deodexed
	 */
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

	/**
	 * will run the oat2dex on the given boot.oat file
	 * 
	 * @param bootOat
	 *            the boot.oat file to be de-optimized
	 * @return true only if the boot.oat was deoptimized
	 */
	public static boolean oat2dexBoot(File bootOat) {
		String[] cmd = { "boot", bootOat.getAbsolutePath() };
		try {
			Logger.writLog("[Deodexer][I] trying to de-optimize boot.oat using oat2dex as library ....");
			org.rh.smaliex.Main.main(cmd);
		} catch (Exception e) {
			Logger.writLog("[Deodexer][E] de-optimize boot.oat using oat2dex as library ...." + "[failed]");
			Logger.writLog("[Deodexer][I] trying to de-optimize boot.oat using oat2dex as binary ....");
			String[] cmd1 = { "java", "-jar", new File(S.OAT2DEX_JAR).getAbsolutePath(), "boot",
					bootOat.getAbsolutePath() };
			CmdUtils.runCommand(cmd1);

		}
		return S.bootTmpDex.exists();
	}

	/**
	 * 
	 * @param in
	 *            apk to be signed
	 * @param out
	 *            the output signed apk
	 * @return true only if the sign was successful
	 */
	public static boolean signApk(File in, File out) {
		String[] cmd = { "java", "-jar", new File(S.SIGN_APK).getAbsolutePath(),
				new File(S.TEST_KEY_X509).getAbsolutePath(), new File(S.TEST_KEY_PK8).getAbsolutePath(),
				in.getAbsolutePath(), out.getAbsolutePath() };

		CmdUtils.runCommand(cmd);

		return out.exists();
	}

}
