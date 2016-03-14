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
import java.util.ArrayList;

import deodex.S;

/**
 * 
 * @author lord-ralf-adolf
 *
 */
public class UnsquashUtils {

	/**
	 * 
	 * @param squashFile
	 *            squashfs file to get the command for it
	 * @param dest
	 *            the output destination of the extraction command
	 * @return a Sting Array with the command to extract the given file to the
	 *         given destination
	 */
	private static String[] getUnsquashCommand(File squashFile, File dest) {
		String cmd[] = { S.getUnsquashBinary(), "-no-xattrs", "-f", "-n", "-d", dest.getAbsolutePath(),
				squashFile.getAbsolutePath() };

		return cmd;
	}

	/**
	 * 
	 * @return return if the squashfs tool is available
	 */
	public static boolean haveUnsquash() {
		String cmd[] = { S.getUnsquashBinary(), "-h" };

		int exitValue = CmdUtils.runCommand(cmd);

		return exitValue == 0 || exitValue == 1;
	}

	/**
	 * unsquash the .sqsh file under the given folder
	 * 
	 * @param systemFolder
	 *            the system folder where we will search for the squash files
	 * @return true only if the extraction was successful
	 */
	public static boolean unsquash(File systemFolder) {
		File appSquash = new File(systemFolder.getAbsolutePath() + File.separator + "odex.app.sqsh");
		File privAppSquash = new File(systemFolder.getAbsolutePath() + File.separator + "odex.priv-app.sqsh");
		File destFile = S.getUnsquash();
		// make sure the destFile is not there
		FilesUtils.deleteRecursively(destFile);
		// get the commands
		String[] cmd1 = getUnsquashCommand(appSquash, destFile);
		String[] cmd2 = getUnsquashCommand(privAppSquash, destFile);
		if (appSquash.exists()) {
			// unsquash app
			boolean sucess = (CmdUtils.runCommand(cmd1) == 0);
			if (sucess) {
				ArrayList<File> files = FilesUtils.listAllFiles(destFile);
				if (!destFile.exists() || files == null || files.size() == 0) {
					return false;
				} else {
					boolean copied = FilesUtils.copyFileRecurcively(destFile,
							new File(systemFolder.getAbsolutePath() + File.separator + "app"));
					FilesUtils.deleteRecursively(destFile);
					if (!copied)
						return false;
				}
			} else {
				Logger.writLog("[UnsquashUtils][E]failed to unsquash " + appSquash.getAbsolutePath());
				return false;
			}

		}

		if (privAppSquash.exists()) {
			// unsquash priv-app
			boolean sucess = (CmdUtils.runCommand(cmd2) == 0);
			if (sucess) {
				ArrayList<File> files = FilesUtils.listAllFiles(destFile);
				if (!destFile.exists() || files == null || files.size() == 0) {
					return false;
				} else {
					boolean copied = FilesUtils.copyFileRecurcively(destFile,
							new File(systemFolder.getAbsolutePath() + File.separator + "priv-app"));
					FilesUtils.deleteRecursively(destFile);
					if (!copied)
						return false;
				}
			} else {
				Logger.writLog("[UnsquashUtils][E]failed to unsquash " + privAppSquash.getAbsolutePath());
				return false;
			}
		}

		return true;
	}
}
