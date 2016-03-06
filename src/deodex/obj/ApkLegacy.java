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
package deodex.obj;

import java.io.File;
import java.io.Serializable;

import deodex.S;
import deodex.tools.FilesUtils;

public class ApkLegacy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5797950843030193904L;

	/**
	 * the original apk location in the system folder
	 */
	public File origApk;
	/**
	 * the temp apk location on the working folder
	 */
	public File tempApk;
	/**
	 * the smaliFolder location it will be created when calling smali tool on
	 * this apk
	 */
	public File smaliFolder;
	/**
	 * the classes.dex file location
	 */
	public File classes;
	/**
	 * the odex file location on the working folder
	 */
	public File tempOdex;
	/**
	 * the original OdexFile location in the system folder
	 */
	public File origOdex;
	/**
	 * the location of the zipaligned apk on the working folder
	 */
	public File tempZipaligned;
	/**
	 * the location of the signed apk on the working folder
	 */
	public File tempSigned;

	/**
	 * this object will create a serie of usefull File object to ease track all
	 * the files
	 * 
	 * @param odexFile
	 */
	public ApkLegacy(File odexFile) {
		this.origOdex = odexFile;
		this.origApk = new File(
				origOdex.getAbsolutePath().subSequence(0, origOdex.getAbsolutePath().lastIndexOf(".")) + S.APK_EXT);

	}

	/**
	 * typically this will copy odex and apk files to the working directory
	 * 
	 * @param tempFolder1
	 *            : the temp folder where to copy the files
	 * @return true only if the copy was sucessful
	 */
	public boolean copyNeededFiles(File tempFolder1) {
		String pureName = this.origApk.getName().substring(0, this.origApk.getName().lastIndexOf("."));
		File tempFolder = new File(tempFolder1.getAbsolutePath() + File.separator
				+ this.origApk.getName().substring(0, origApk.getName().lastIndexOf(".")));
		this.tempApk = new File(tempFolder.getAbsolutePath() + File.separator + origApk.getName());
		this.tempOdex = new File(tempFolder.getAbsolutePath() + File.separator + origOdex.getName());
		smaliFolder = new File(tempFolder.getAbsolutePath() + File.separator
				+ origApk.getName().substring(0, origApk.getName().lastIndexOf(".")));
		classes = new File(tempFolder.getAbsolutePath() + File.separator + S.CLASSES);
		this.tempZipaligned = new File(tempFolder.getAbsolutePath() + File.separator + pureName + "_zipaligned.apk");
		this.tempSigned = new File(tempFolder.getAbsolutePath() + File.separator + pureName + "_signed.apk");
		FilesUtils.copyFileRecurcively(origApk, tempApk);
		FilesUtils.copyFileRecurcively(origOdex, tempOdex);

		return tempApk.exists() && tempOdex.exists();
	}

}
