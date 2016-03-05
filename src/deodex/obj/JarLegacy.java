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

import deodex.S;
import deodex.tools.FilesUtils;
import deodex.tools.Logger;

public class JarLegacy {
	public File origJar;
	public File tempJar;
	public File smaliFolder;
	public File classes;
	public File tempOdex;
	public File origOdex;

	public JarLegacy(File odexFile) {
		this.origOdex = odexFile;
		this.origJar = new File(
				origOdex.getAbsolutePath().subSequence(0, origOdex.getAbsolutePath().lastIndexOf(".")) + ".jar");

	}

	public boolean copyNeededFiles(File tempFolder1) {
		String pure = this.origJar.getName().substring(0, this.origJar.getName().lastIndexOf("."));
		File tempFolder = new File(tempFolder1.getAbsolutePath() + File.separator + pure);
		this.tempJar = new File(tempFolder.getAbsolutePath() + File.separator + origJar.getName());
		this.tempOdex = new File(tempFolder.getAbsolutePath() + File.separator + origOdex.getName());
		smaliFolder = new File(tempFolder.getName() + File.separator
				+ origJar.getName().substring(0, origJar.getName().lastIndexOf(".")));
		classes = new File(tempFolder.getAbsolutePath() + File.separator + S.CLASSES);
		Logger.writLog("[JarLegacy][I] about to copy "+origJar.getAbsolutePath()+" to "+tempJar.getAbsolutePath());
		boolean sucess = FilesUtils.copyFileRecurcively(origJar, tempJar);
		Logger.writLog("[JarLegacy][I]  copy "+origJar.getAbsolutePath()+" to "+tempJar.getAbsolutePath() +(sucess ? " Success":" Failed"));

		Logger.writLog("[JarLegacy][I] about to copy "+origOdex.getAbsolutePath()+" to "+tempOdex.getAbsolutePath());
		sucess = FilesUtils.copyFileRecurcively(origOdex, tempOdex);
		Logger.writLog("[JarLegacy][I] copy "+origOdex.getAbsolutePath()+" to "+tempOdex.getAbsolutePath()+(sucess ? " Success":" Failed"));

		return tempJar.exists() && tempOdex.exists();
	}

	/**
	 * @return the classes
	 */
	public File getClasses() {
		return classes;
	}

	/**
	 * @return the origJar
	 */
	public File getOrigJar() {
		return origJar;
	}

	/**
	 * @return the origOdex
	 */
	public File getOrigOdex() {
		return origOdex;
	}

	/**
	 * @return the smaliFolder
	 */
	public File getSmaliFolder() {
		return smaliFolder;
	}

	/**
	 * @return the tempJar
	 */
	public File getTempJar() {
		return tempJar;
	}

	/**
	 * @return the tempOdex
	 */
	public File getTempOdex() {
		return tempOdex;
	}

}
