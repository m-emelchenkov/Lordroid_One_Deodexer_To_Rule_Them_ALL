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

public class SessionCfg {

	private static String arch;
	private static int sdk;
	private static File systemFolder;
	private static File bootOatFile;
	public static boolean zipalign = false;
	public static boolean sign = false;
	public static int sessionFrom = 1;
	public static boolean isSquash = false;
	/**
	 * 
	 * @return
	 */
	public static String getArch() {
		return arch;
	}

	/**
	 * 
	 * @return bootOatFile location on the chosen folder
	 */
	public static File getBootOatFile() {
		return bootOatFile;
	}

	/**
	 * 
	 * @return
	 */
	public static int getSdk() {
		return sdk;
	}

	/**
	 * 
	 * @return
	 */
	public static File getSystemFolder() {
		return systemFolder;
	}

	/**
	 * @return the sign
	 */
	public static boolean isSign() {
		return sign;
	}

	/**
	 * @return the zipalign
	 */
	public static boolean isZipalign() {
		return zipalign;
	}

	/**
	 * 
	 * @param arch
	 */
	public static void setArch(String arch) {
		SessionCfg.arch = arch;
	}

	/**
	 * 
	 * @param bootOatFile
	 */
	public static void setBootOatFile(File bootOatFile) {
		SessionCfg.bootOatFile = bootOatFile;
	}

	/**
	 * 
	 * @param sdk
	 */
	public static void setSdk(int sdk) {
		SessionCfg.sdk = sdk;
	}

	/**
	 * @param sign
	 *            the sign to set
	 */
	public static void setSign(boolean signp) {
		sign = signp;
	}

	/**
	 * 
	 * @param systemFolder
	 */
	public static void setSystemFolder(File systemFolder) {
		SessionCfg.systemFolder = systemFolder;
		// setBootOatFile(new File(systemFolder.getAbsolutePath() +
		// File.separator + S.SYSTEM_FRAMEWORK + File.separator
		// + arch + File.separator + S.SYSTEM_FRAMEWORK_BOOT));
	}

	/**
	 * @param zipalign
	 *            the zipalign to set
	 */
	public static void setZipalign(boolean zipalignp) {
		zipalign = zipalignp;
	}

}
