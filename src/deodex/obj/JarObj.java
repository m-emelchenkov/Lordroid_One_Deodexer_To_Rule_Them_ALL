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
import deodex.SessionCfg;
import deodex.tools.FilesUtils;
import deodex.tools.Logger;

public class JarObj {

	File origJar;
	File odexFile;
	File tmpFolder;
	File tmpodex;
	File tmpdex;
	File tmpdex2;
	File tmpClasses;
	File tmpClasses2;
	File tmpJar;
	String absoluteName;

	public JarObj(File odexFile) {
		this.odexFile = odexFile;

		if (odexFile.getName().endsWith(".odex")) {
			absoluteName = odexFile.getName().substring(0, odexFile.getName().lastIndexOf("."));
		} else if (odexFile.getName().endsWith(".odex.xz")) {
			absoluteName = odexFile.getName().substring(0, -9);
		}

		this.origJar = new File(SessionCfg.getSystemFolder().getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK
				+ File.separator + absoluteName + ".jar");
		// this .tmpFolder = new File(S.)
		Logger.logToStdIO(origJar.getAbsolutePath());
	}

	public boolean copyNeedFiles(File tmpFolder) {
		this.tmpFolder = new File(tmpFolder.getAbsolutePath() + File.separator + this.absoluteName);
		tmpFolder.mkdirs();
		if (!tmpFolder.exists())
			return false;
		tmpodex = new File(this.tmpFolder.getAbsolutePath() + File.separator + odexFile.getName());
		tmpJar = new File(this.tmpFolder.getAbsolutePath() + File.separator + this.origJar.getName());
		tmpdex = new File(this.tmpFolder.getAbsolutePath() + File.separator + this.absoluteName + S.DEX_EXT);
		tmpdex2 = new File(this.tmpFolder.getAbsolutePath() + File.separator + this.absoluteName + S.DEX2_EXT);
		tmpClasses = new File(this.tmpFolder.getAbsolutePath() + File.separator + S.CLASSES);
		tmpClasses2 = new File(this.tmpFolder.getAbsolutePath() + File.separator + S.CLASSES_2);

		boolean copyStatus = FilesUtils.copyFile(odexFile, tmpodex);
		boolean copyStatus2 = FilesUtils.copyFile(origJar, tmpJar);

		return copyStatus && copyStatus2;

	}

	/**
	 * @return the absoluteName
	 */
	public String getAbsoluteName() {
		return absoluteName;
	}

	/**
	 * @return the odexFile
	 */
	public File getOdexFile() {
		return odexFile;
	}

	/**
	 * @return the origJar
	 */
	public File getOrigJar() {
		return origJar;
	}

	/**
	 * @return the tmpClasses
	 */
	public File getTmpClasses() {
		return tmpClasses;
	}

	/**
	 * @return the tmpClasses2
	 */
	public File getTmpClasses2() {
		return tmpClasses2;
	}

	/**
	 * @return the tmpdex
	 */
	public File getTmpdex() {
		return tmpdex;
	}

	/**
	 * @return the tmpdex2
	 */
	public File getTmpdex2() {
		return tmpdex2;
	}

	/**
	 * @return the tmpFolder
	 */
	public File getTmpFolder() {
		return tmpFolder;
	}

	/**
	 * @return the tmpJar
	 */
	public File getTmpJar() {
		return tmpJar;
	}

	/**
	 * @return the tmpodex
	 */
	public File getTmpodex() {
		return tmpodex;
	}

	/**
	 * @param absoluteName
	 *            the absoluteName to set
	 */
	public void setAbsoluteName(String absoluteName) {
		this.absoluteName = absoluteName;
	}

	/**
	 * @param odexFile
	 *            the odexFile to set
	 */
	public void setOdexFile(File odexFile) {
		this.odexFile = odexFile;
	}

	/**
	 * @param origJar
	 *            the origJar to set
	 */
	public void setOrigJar(File origJar) {
		this.origJar = origJar;
	}

	/**
	 * @param tmpClasses
	 *            the tmpClasses to set
	 */
	public void setTmpClasses(File tmpClasses) {
		this.tmpClasses = tmpClasses;
	}

	/**
	 * @param tmpClasses2
	 *            the tmpClasses2 to set
	 */
	public void setTmpClasses2(File tmpClasses2) {
		this.tmpClasses2 = tmpClasses2;
	}

	/**
	 * @param tmpdex
	 *            the tmpdex to set
	 */
	public void setTmpdex(File tmpdex) {
		this.tmpdex = tmpdex;
	}

	/**
	 * @param tmpdex2
	 *            the tmpdex2 to set
	 */
	public void setTmpdex2(File tmpdex2) {
		this.tmpdex2 = tmpdex2;
	}

	/**
	 * @param tmpFolder
	 *            the tmpFolder to set
	 */
	public void setTmpFolder(File tmpFolder) {
		this.tmpFolder = tmpFolder;
	}

	/**
	 * @param tmpJar
	 *            the tmpJar to set
	 */
	public void setTmpJar(File tmpJar) {
		this.tmpJar = tmpJar;
	}

	/**
	 * @param tmpodex
	 *            the tmpodex to set
	 */
	public void setTmpodex(File tmpodex) {
		this.tmpodex = tmpodex;
	}

}
