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

import javax.swing.JProgressBar;

import deodex.R;
import deodex.S;
import deodex.obj.JarLegacy;
import deodex.tools.Deodexer;
import deodex.tools.FilesUtils;
import deodex.tools.Logger;
import deodex.tools.Zip;

public class JarWorkerLegacy implements Watchable ,Runnable{

	ArrayList<File> jarList;
	File tempFolder;
	LoggerPan logPan;
	JProgressBar progressBar = new JProgressBar();
	ThreadWatcher threadWatcher;
	
	public JarWorkerLegacy(ArrayList <File> jarList ,LoggerPan logPan ,File tempFolder ){
	this.jarList = jarList;
	this.logPan = logPan;
	this.tempFolder = tempFolder;
	
	progressBar.setMinimum(0);
	progressBar.setMaximum(jarList.size());
	progressBar.setStringPainted(true);
	
	}
	
	private boolean deodexJar(JarLegacy jar){
		boolean copyStatus = jar.copyNeededFiles(tempFolder);
		if(!copyStatus){
			Logger.logToStdIO("["+jar.origJar+"]"+"Failed to coppy neededFiles");
			return false;
		}
		// deodexing 
		boolean deodexStatus = Deodexer.deoDexApkLegacy(jar.tempOdex, jar.classes);
		if(!deodexStatus){
			Logger.logToStdIO("["+jar.origJar+"]"+"Failed to deodex ");
			return false;
		}
		// putback
		ArrayList<File> classes = new ArrayList<File>();
		classes.add(jar.classes);
		boolean putBack = false;
		try {
			putBack = Zip.addFilesToExistingZip(jar.tempJar, classes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!putBack){
			Logger.logToStdIO("["+jar.origJar+"]"+"Failed to push classes.dex ");
			return false;
		}
		
		// pushBack
		boolean pushBack = false;
		pushBack = FilesUtils.copyFile(jar.tempJar, jar.origJar);
		if(!pushBack){
			Logger.logToStdIO("["+jar.origJar+"]"+"Failed to push apk back ");
			return false;
		}
		
		FilesUtils.deleteRecursively(jar.tempJar);
		FilesUtils.deleteRecursively(jar.origOdex);
		FilesUtils.deleteRecursively(jar.classes);	
		
		return true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		boolean success = false;
		for (File f : this.jarList){
			JarLegacy jar = new JarLegacy (f);
			success = this.deodexJar(jar);
			if(success){
				logPan.addLog("["+jar.origJar.getName()+"]"+R.getString(S.LOG_SUCCESS));
			} else {
				logPan.addLog("["+jar.origJar.getName()+"]"+R.getString(S.LOG_FAIL));
			}
			progressBar.setValue(progressBar.getValue()+1);
			progressBar.setString(R.getString("progress.jar")+"("+progressBar.getValue()+"/"+progressBar.getMaximum()+")");
			threadWatcher.updateProgress();
		}
		FilesUtils.deleteRecursively(tempFolder);
		progressBar.setValue(progressBar.getMaximum());
		progressBar.setString(R.getString("progress.done"));
		threadWatcher.updateProgress();
		threadWatcher.done(this);
		
	}

	/**
	 * @return the progressBar
	 */
	public JProgressBar getProgressBar() {
		return progressBar;
	}

	/**
	 * @param progressBar the progressBar to set
	 */
	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	@Override
	public void addThreadWatcher(ThreadWatcher watcher) {
		threadWatcher = watcher;
	}
}
