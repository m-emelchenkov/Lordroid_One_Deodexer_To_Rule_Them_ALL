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
import java.io.Serializable;

import deodex.S;
import deodex.SessionCfg;
import deodex.tools.FilesUtils;

public class ApkObj implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private File origApk;
	private File archFolder;
	private File odexFile;
	private File tmpWorkingFolder;
	private boolean isdeodexNeeded = false; 
	private File folder ;
	
	private File tempApk ;
	private File tempApkZipalign ;
	private File tempApkSigned;
	private File tempOdex ;
	private File tempDex ;
	private File tempDex2;
	private File tempClasses1;
	private File tempClasses2;
	
	
	/**
	 * 
	 * @param folder
	 * @param arch
	 */
	public ApkObj(File folder ) {
		this.folder = folder;
		// apk location
		origApk = new File(folder.getAbsolutePath()+File.separator+folder.getName()+S.APK_EXT);
		
		// arch folder we delete this one if the apk was deodexed successfully
		archFolder = new File(folder.getAbsolutePath()+File.separator+SessionCfg.getArch());
		
		// odex file ,now in 5.X and above it can be compressed or it can be not compressed
		if(archFolder.exists() && archFolder.isDirectory()){
		File[] archFiles = archFolder.listFiles();
		for (File f : archFiles){
			if (f.getName().endsWith(S.ODEX_EXT)){
				odexFile =f;
				break;
			} else if (f.getName().endsWith(S.COMP_ODEX_EXT)){
				odexFile =f;
				break;
			}
		}
		if(odexFile != null)
			isdeodexNeeded = true;
		}
	
	}
	
	/**
	 * 
	 * @param tmpFolder
	 * @return true only if the copy succes
	 */
	public boolean copyNeededFilesToTempFolder(File tmpFolder){
		//if()
		tmpWorkingFolder = new File(tmpFolder.getAbsolutePath()+File.separator+folder.getName());
		tmpWorkingFolder.mkdirs();

		tempApk = new File(tmpWorkingFolder.getAbsolutePath()+File.separator+folder.getName()+S.APK_EXT);
		tempApkZipalign = new File(tmpWorkingFolder.getAbsolutePath()+File.separator+folder.getName()+"_zipaligned"+S.APK_EXT);
		tempApkSigned = new File(tmpWorkingFolder.getAbsolutePath()+File.separator+folder.getName()+"_signed"+S.APK_EXT);
		tempOdex = new File(tmpWorkingFolder.getAbsolutePath()+File.separator+this.odexFile.getName());
		tempDex = new File(tmpWorkingFolder.getAbsolutePath()+File.separator+folder.getName()+S.DEX_EXT);
		tempDex2 = new File(tmpWorkingFolder.getAbsolutePath()+File.separator+folder.getName()+S.DEX2_EXT);
		setTempClasses1(new File(tmpWorkingFolder.getAbsolutePath()+File.separator+S.CLASSES));
		setTempClasses2(new File(tmpWorkingFolder.getAbsolutePath()+File.separator+S.CLASSES_2));
		FilesUtils.copyFile(this.origApk, tempApk) ;
		FilesUtils.copyFile(odexFile, tempOdex);
		return tempApk.exists() && tempOdex.exists();
	}

	
	
	public File getFolder() {
		return folder;
	}

	public File getTempApk() {
		return tempApk;
	}
	
	public File getTempApkZipalign() {
		return tempApkZipalign;
	}
	
	/**
	 * 
	 * @return
	 */
	public File getTempApkSigned() {
		return tempApkSigned;
	}
	/**
	 * 
	 * @return
	 */
	public File getTempOdex() {
		return tempOdex;
	}
	/**
	 * 
	 * @return
	 */
	public File getTempDex() {
		return tempDex;
	}
	/**
	 * 
	 * @return
	 */
	public File getTempDex2() {
		return tempDex2;
	}
	/**
	 * 
	 * @param odexFile
	 */
	public void setOdexFile(File odexFile) {
		this.odexFile = odexFile;
	}
	/**
	 * 
	 * @return
	 */
	public File getOrigApk() {
		return origApk;
	}
	/**
	 * 
	 * @return
	 */
	public File getArchFolder() {
		return archFolder;
	}
	/**
	 * 
	 * @return
	 */
	public File getOdexFile() {
		return odexFile;
	}
	/**
	 * 
	 * @return
	 */
	public File getTmpWorkingFolder() {
		return tmpWorkingFolder;
	}
	/**
	 * 
	 * @return
	 */
	public boolean isIsdeodexNeeded() {
		return isdeodexNeeded;
	}

	/**
	 * @return the tempClasses1
	 */
	public File getTempClasses1() {
		return tempClasses1;
	}

	/**
	 * @param tempClasses1 the tempClasses1 to set
	 */
	public void setTempClasses1(File tempClasses1) {
		this.tempClasses1 = tempClasses1;
	}

	/**
	 * @return the tempClasses2
	 */
	public File getTempClasses2() {
		return tempClasses2;
	}

	/**
	 * @param tempClasses2 the tempClasses2 to set
	 */
	public void setTempClasses2(File tempClasses2) {
		this.tempClasses2 = tempClasses2;
	}
}
