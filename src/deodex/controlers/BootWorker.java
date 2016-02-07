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
import java.io.IOException;
import java.util.ArrayList;

import deodex.S;
import deodex.SessionCfg;
import deodex.tools.FilesUtils;
import deodex.tools.Zip;
import deodex.tools.ZipTools;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class BootWorker implements Runnable ,Watchable{
	ArrayList<File> bootFiles;
	File tmpFolder;
	ThreadWatcher threadWatcher;
	LoggerPan log;
	public BootWorker(ArrayList<File> bootList, File tmpFolder,LoggerPan log) {
		bootFiles = bootList;
		this.tmpFolder = tmpFolder;
		this.log = log;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (File file : bootFiles){
			boolean success = deoDexBootFile(file);
			if(success){
				log.addLog("[ "+file.getName()+" ]"+" [SUCCESS]");
			} else{
				log.addLog("[ "+file.getName()+" ]"+" [FAILED ]");

			}
		}
		FilesUtils.deleteRecursively(tmpFolder);
		this.threadWatcher.done(this);
	}

	private boolean deoDexBootFile(File file) {
		File tmpClasses = new File(tmpFolder.getAbsolutePath() + File.separator + S.CLASSES);
		File tmpClasses2 = new File(tmpFolder.getAbsolutePath() + File.separator + S.CLASSES_2);
		String absoluteName = file.getName().substring(0, file.getName().lastIndexOf("."));

		File tmpJar = new File(tmpFolder.getAbsolutePath() + File.separator + absoluteName + ".jar");
		File origJar = new File(SessionCfg.getSystemFolder().getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK + File.separator
				+ absoluteName + ".jar");

		boolean copyStatus = false;
		copyStatus = FilesUtils.copyFile(origJar, tmpJar);
		if (!copyStatus) {
			// TODO add LOGGING for this
			return false;
		} else{
			copyStatus = FilesUtils.copyFile(file, tmpClasses);
			if(absoluteName.equals("framework")){
				copyStatus = FilesUtils.copyFile(new File(
						file.getParentFile().getAbsolutePath()+File.separator+absoluteName+S.DEX2_EXT), tmpClasses2);
			}
			if(!copyStatus){
				return false;
			} else {
				boolean addStatus = false;
				ArrayList <File> list =new ArrayList<File>();
				list.add(tmpClasses);
				if(absoluteName.equals("framework")){
					list.add(tmpClasses2);
				}
				try {
					addStatus = Zip.addFilesToExistingZip(tmpJar, list);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if(!addStatus){
					return false;
				} else{
					copyStatus = FilesUtils.copyFile(tmpJar, origJar);
				}
			}
		}
		
		return copyStatus;

	}

	@Override
	public void addThreadWatcher(ThreadWatcher watcher) {
		// TODO Auto-generated method stub
		this.threadWatcher = watcher;
	}

}
