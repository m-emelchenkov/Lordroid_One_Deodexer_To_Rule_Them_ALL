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
package deodex.obj;

import java.io.File;

import deodex.S;
import deodex.tools.FilesUtils;

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

		FilesUtils.copyFileRecurcively(origJar, tempJar);
		FilesUtils.copyFileRecurcively(origOdex, tempOdex);

		return tempJar.exists() && tempOdex.exists();
	}

}
