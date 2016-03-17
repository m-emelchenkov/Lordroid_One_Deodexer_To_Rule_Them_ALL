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
import deodex.tools.Logger;

public class ApkObj implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private File origApk;
	// private File archFolder;
	private File odexFile;
	private File tmpWorkingFolder;
	private File folder;

	private File tempApk;
	private File tempApkZipalign;
	private File tempApkSigned;
	private File tempCompOdex;

	private File tempOdex;

	private File tempDex;

	private File tempDex2;
	private File tempDex3;
	private File tempClasses1;
	private File tempClasses2;
	private File tempClasses3;
	private String pureName;

	/**
	 * 
	 * @param folder
	 * @param arch
	 */
	public ApkObj(File odexFile) {
		this.odexFile = odexFile;
		if (odexFile.getName().endsWith(S.COMP_ODEX_EXT)) {
			setPureName(odexFile.getName().substring(0, odexFile.getName().lastIndexOf(S.COMP_ODEX_EXT)));
		} else if (odexFile.getName().endsWith(S.COMP_GZ_ODEX_EXT)){
			setPureName(odexFile.getName().substring(0, odexFile.getName().lastIndexOf(S.COMP_GZ_ODEX_EXT)));	
		} else {
			setPureName(odexFile.getName().substring(0, odexFile.getName().lastIndexOf(S.ODEX_EXT)));
		}
		File f = odexFile;
		while (true) {
			f = f.getParentFile();
			if (f.isDirectory() && f.getName().equals(this.pureName)) {
				this.folder = f;
				break;
			}
		}
		this.origApk = new File(folder.getAbsolutePath() + File.separator + this.pureName + S.APK_EXT);

	}

	/**
	 * move origApk to tempApk and origOdex to tempOdex (note : move and not copy no IO involved) 
	 * @param tmpFolder
	 * @return true only if the copy succes
	 */
	public boolean copyNeededFilesToTempFolder(File tmpFolder) {
		// if()
		tmpWorkingFolder = new File(tmpFolder.getAbsolutePath() + File.separator + folder.getName());
		tmpWorkingFolder.mkdirs();

		tempApk = new File(tmpWorkingFolder.getAbsolutePath() + File.separator + folder.getName() + S.APK_EXT);
		tempApkZipalign = new File(
				tmpWorkingFolder.getAbsolutePath() + File.separator + folder.getName() + "_zipaligned" + S.APK_EXT);
		tempApkSigned = new File(
				tmpWorkingFolder.getAbsolutePath() + File.separator + folder.getName() + "_signed" + S.APK_EXT);
		tempCompOdex = new File(tmpWorkingFolder.getAbsolutePath() + File.separator + this.odexFile.getName());
		tempOdex = new File(tmpWorkingFolder.getAbsolutePath() + File.separator + this.pureName + S.ODEX_EXT);
		tempDex = new File(tmpWorkingFolder.getAbsolutePath() + File.separator + folder.getName() + S.DEX_EXT);
		tempDex2 = new File(tmpWorkingFolder.getAbsolutePath() + File.separator + folder.getName() + S.DEX2_EXT);
		tempDex3 = new File(tmpWorkingFolder.getAbsolutePath() + File.separator + folder.getName() + S.DEX3_EXT);

		setTempClasses1(new File(tmpWorkingFolder.getAbsolutePath() + File.separator + S.CLASSES));
		setTempClasses2(new File(tmpWorkingFolder.getAbsolutePath() + File.separator + S.CLASSES_2));
		setTempClasses3(new File(tmpWorkingFolder.getAbsolutePath() + File.separator + S.CLASSES_3));

		Logger.writLog("[ApkObj]" + this.origApk.getName() + "copying " + this.origApk.getAbsolutePath() + " to "
				+ tempApk.getAbsolutePath());
		// FilesUtils.copyFile(this.origApk, tempApk); lets use rename instead
		this.origApk.renameTo(tempApk);
		Logger.writLog("[ApkObj]" + "copying " + odexFile.getAbsolutePath() + " to " + tempCompOdex.getAbsolutePath());
		// FilesUtils.copyFile(odexFile, tempCompOdex);
		odexFile.renameTo(tempCompOdex);
		return tempApk.exists() && tempCompOdex.exists();
	}

	public File getFolder() {
		return folder;
	}

	/**
	 * 
	 * @return the OdexFile location
	 */
	public File getOdexFile() {
		return odexFile;
	}

	/**
	 * 
	 * @return the original apkLocation
	 */
	public File getOrigApk() {
		return origApk;
	}

	/**
	 * @return the pureName (without the extention)
	 */
	public String getPureName() {
		return pureName;
	}

	/**
	 * 
	 * @return the temp apk location in the working folder
	 */
	public File getTempApk() {
		return tempApk;
	}

	/**
	 * 
	 * @return the temp signed apk location in the working folder
	 */
	public File getTempApkSigned() {
		return tempApkSigned;
	}

	/**
	 * 
	 * @return the temp z aipaligned apk location in the working folder
	 */
	public File getTempApkZipalign() {
		return tempApkZipalign;
	}

	/**
	 * @return the tempClasses1 in the working folder
	 */
	public File getTempClasses1() {
		return tempClasses1;
	}

	/**
	 * @return the tempClasses2 in the working folder
	 */
	public File getTempClasses2() {
		return tempClasses2;
	}

	/**
	 * @return the tempClasses3 in the working folder
	 */
	public File getTempClasses3() {
		return tempClasses3;
	}

	/**
	 * @return the tempCompOdex in the working folder
	 */
	public File getTempCompOdex() {
		return tempCompOdex;
	}

	/**
	 * 
	 * @return the temps dex file created by oat2dex location
	 */
	public File getTempDex() {
		return tempDex;
	}

	/**
	 * some apks and jars have multiple classes.dex this is the location of the
	 * appName-classes2.dex
	 * 
	 * @return he temps dex file 2 created by oat2dex location
	 */
	public File getTempDex2() {
		return tempDex2;
	}

	/**
	 * some apks and jars have multiple classes.dex this is the location of the
	 * appName-classes3.dex
	 * 
	 * @return the tempDex3 he temps dex file 3 created by oat2dex location
	 */
	public File getTempDex3() {
		return tempDex3;
	}

	/**
	 * 
	 * @return the odex file location on the working folder
	 */
	public File getTempOdex() {
		return tempOdex;
	}

	/**
	 * 
	 * @return the location of the working folder of this apk
	 */
	public File getTmpWorkingFolder() {
		return tmpWorkingFolder;
	}

	public void reverseMove() {
		tempApk.renameTo(origApk);
		tempCompOdex.renameTo(odexFile);
	}

	/**
	 * 
	 * @param odexFile
	 *            to set the OdexFile
	 */
	public void setOdexFile(File odexFile) {
		this.odexFile = odexFile;
	}

	/**
	 * @param pureName
	 *            the pureName to set
	 */
	public void setPureName(String pureName) {
		this.pureName = pureName;
	}

	/**
	 * @param tempClasses1
	 *            the tempClasses1 to set
	 */
	public void setTempClasses1(File tempClasses1) {
		this.tempClasses1 = tempClasses1;
	}

	/**
	 * @param tempClasses2
	 *            the tempClasses2 to set
	 */
	public void setTempClasses2(File tempClasses2) {
		this.tempClasses2 = tempClasses2;
	}

	/**
	 * @param tempClasses3
	 *            the tempClasses3 to set
	 */
	public void setTempClasses3(File tempClasses3) {
		this.tempClasses3 = tempClasses3;
	}

	/**
	 * @param tempCompOdex
	 *            the tempCompOdex to set
	 */
	public void setTempCompOdex(File tempCompOdex) {
		this.tempCompOdex = tempCompOdex;
	}

	/**
	 * @param tempDex3
	 *            the tempDex3 to set
	 */
	public void setTempDex3(File tempDex3) {
		this.tempDex3 = tempDex3;
	}
}
