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

import deodex.Cfg;
import deodex.S;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class Zip {


	/**
	 * add files from a system folder to a zip used to create flashable zip only
	 * app priv-app and framwork will be added
	 * 
	 * @param systemFolder
	 *            the system folder to be added
	 * @param zipFile
	 *            the zip file to be created
	 */
	public static void AddFilesToFolderInZip(File systemFolder, ZipFile zipFile) {

		ArrayList<File> list0 = FilesUtils
				.listAllFiles(new File(systemFolder.getAbsolutePath() + File.separator + S.SYSTEM_APP));
		ArrayList<File> list1 = FilesUtils
				.listAllFiles(new File(systemFolder.getAbsolutePath() + File.separator + S.SYSTEM_PRIV_APP));
		ArrayList<File> list2 = FilesUtils
				.listAllFiles(new File(systemFolder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK));
		ArrayList<File> list = new ArrayList<File>();
		for (File f : list0)
			list.add(f);
		for (File f : list1)
			list.add(f);
		for (File f : list2)
			list.add(f);

		for (File f : list)
			Zip.AddFileToFolderInZip(systemFolder, f, zipFile);
	}

	/**
	 * 
	 * @param pathToIgnore
	 *            the path that will be ignored when putting in the zip
	 * @param fileToAdd
	 *            a File to add to the given zip
	 * @param zipFile
	 *            a zip file
	 */
	public static void AddFileToFolderInZip(File pathToIgnore, File fileToAdd, ZipFile zipFile) {
		try {

			ArrayList<File> filesToAdd = new ArrayList<File>();
			filesToAdd.add(fileToAdd);

			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_STORE); // set
																		// compression
																		// method
																		// to
																		// deflate
																		// compression

			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

			String rootInZip = "system"
					+ fileToAdd.getParentFile().getAbsolutePath().substring(pathToIgnore.getAbsolutePath().length());
			Logger.writLog("[Zip][I]putting " + fileToAdd.getAbsolutePath() + " in "
					+ zipFile.getFile().getAbsolutePath() + " >> " + rootInZip + File.separator + fileToAdd.getName());
			parameters.setRootFolderInZip(rootInZip);

			// Now add files to the zip file
			zipFile.addFiles(filesToAdd, parameters);
		} catch (ZipException e) {
			e.printStackTrace();
			Logger.writLog("[ZIP][EX]" + e.getStackTrace());
		}

	}

	/**
	 * zipalign a given apk
	 * 
	 * @param in
	 *            the input apk
	 * @param out
	 *            the output apk
	 * @return true only if the apk was zipaligned
	 */
	public static boolean zipAlignAPk(File in, File out) {
		if (out.exists()) {
			return true;
		}
		String[] cmd = { new File(S.ZIPALIGN_BIN + File.separator + Cfg.getOs()).getAbsolutePath(), "4",
				in.getAbsolutePath(), out.getAbsolutePath() };
		CmdUtils.runCommand(cmd);

		return out.exists();
	}

	/**
	 * this method uses aapt to add classes files to the given apk/jar 
	 * the junk path are ignored (classes file will be added to the root of the apk/jar)
	 * @param tempApk the apk to which classes will be added
	 * @param classesFiles the list of classes files to add (not null nor size 0 safe )
	 * @return added true only is files were added successfully
	 */
	public static boolean addFilesToExistingZip(File tempApk, ArrayList<File> classesFiles) {
		// TODO Auto-generated method stub
		ArrayList <String> cmds = new ArrayList<String>();
		cmds.add(S.AAPT_BIN.getAbsolutePath());
		cmds.add("a");
		cmds.add("-k");
		cmds.add(tempApk.getAbsolutePath());
		for (File f : classesFiles)
			cmds.add(f.getAbsolutePath());
		
		String[] cmd = new String[cmds.size()];
		for(int i = 0 ; i < cmds.size() ; i++)
			cmd[i] = cmds.get(i);
		boolean sucess = (CmdUtils.runCommand(cmd) == 0);
		if(!sucess){
			sucess = true;
			for (File f : classesFiles){
				try {
					sucess = sucess && ZipTools.isFileinZip(f.getName(), new ZipFile(tempApk));
				} catch (ZipException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return sucess;
	}
}
