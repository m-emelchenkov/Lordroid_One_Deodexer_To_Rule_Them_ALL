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
package deodex.controlers;

import java.io.File;
import java.util.ArrayList;

import deodex.S;
import deodex.SessionCfg;
import deodex.tools.FilesUtils;

public class BootWorker implements Runnable{
	ArrayList <File>bootFiles;
	File tmpFolder;
	
	public  BootWorker(ArrayList<File> bootList ,File tmpFolder){
		bootFiles = bootList;
		this.tmpFolder = tmpFolder;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	private boolean deoDexBootFile(File file){
		File tmpClasses = new File (tmpFolder.getAbsolutePath()+File.separator+S.CLASSES);
		File tmpClasses2 = new File (tmpFolder.getAbsolutePath()+File.separator+S.CLASSES_2);
		String absoluteName = file.getName().substring(0, file.getName().lastIndexOf("."));

		File tmpJar = new File(tmpFolder.getAbsolutePath()+File.separator+absoluteName+".jar");
		File origJar = new File(SessionCfg.getSystemFolder().getAbsolutePath()+File.separator+S.SYSTEM_FRAMEWORK+absoluteName+".jar");
		
		
		boolean copyStatus = false;
		copyStatus = FilesUtils.copyFile(origJar, tmpJar);
		if(!copyStatus){
			//TODO add LOGGING for this
			return false;
		}
		
		
		
		return false;
		
	}
	
}
