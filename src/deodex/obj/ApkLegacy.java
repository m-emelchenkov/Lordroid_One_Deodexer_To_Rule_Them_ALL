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
import deodex.tools.FilesUtils;

public class ApkLegacy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5797950843030193904L;

	public File origApk;
	public File tempApk;
	public File smaliFolder;
	public File classes;
	public File tempOdex;
	public File origOdex;
	public File tempZipaligned;
	public File tempSigned;
	
	
	public ApkLegacy (File odexFile){
		this.origOdex = odexFile;
		this.origApk = new File(origOdex.getAbsolutePath().subSequence(0, origOdex.getAbsolutePath().lastIndexOf("."))+S.APK_EXT);
		
	}
	
	public boolean copyNeededFiles(File tempFolder1){
		String pureName = this.origApk.getName().substring(0, this.origApk.getName().lastIndexOf("."));
		File tempFolder = new File(tempFolder1.getAbsolutePath()+File.separator+this.origApk.getName().substring(0,origApk.getName().lastIndexOf(".")));
		this.tempApk = new File(tempFolder.getAbsolutePath()+File.separator+origApk.getName());
		this.tempOdex = new File(tempFolder.getAbsolutePath()+File.separator+origOdex.getName());
		smaliFolder = new File(tempFolder.getAbsolutePath()+File.separator+origApk.getName().substring(0, origApk.getName().lastIndexOf(".")));
		classes = new File(tempFolder.getAbsolutePath()+File.separator+S.CLASSES);
		this.tempZipaligned = new File(tempFolder.getAbsolutePath()+File.separator+pureName+"_zipaligned.apk");
		this.tempSigned = new File(tempFolder.getAbsolutePath()+File.separator+pureName+"_signed.apk");
		FilesUtils.copyFileRecurcively(origApk, tempApk);
		FilesUtils.copyFileRecurcively(origOdex, tempOdex);
		
		return tempApk.exists() && tempOdex.exists();		
	}
	
}
