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

import deodex.tools.Zip;

public class ZipalignWorker implements Runnable ,Watchable{
	ArrayList<ThreadWatcher> watchers = new ArrayList<ThreadWatcher>();
	JProgressBar bar ;
	ArrayList<File> apks;
	LoggerPan log;
	public ZipalignWorker(ArrayList<File> apks ,JProgressBar bar,LoggerPan log){
		this.bar = bar;
		this.apks = apks;
		this.log = log;
		this.bar.setMinimum(0);
		this.bar.setMaximum(apks.size());
		this.bar.setStringPainted(true);
	}
	
	@Override
	public void addThreadWatcher(ThreadWatcher watcher) {
		// TODO Auto-generated method stub
		
		watchers.add(watcher);
	}

	private String percent(){
		// max ====> 100%
		// value ====> ?
		// ? = (value*100)/max
		return ""+((bar.getValue()*100)/bar.getMaximum())+"%";
	}
	
	private boolean zipalignApk(File apk){
		File zipApk = new File(apk.getAbsolutePath()+"_zipaligned.apk");
		boolean success = false;
		try {
			success =Zip.zipAlignAPk(apk, zipApk);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		boolean deleted = zipApk.delete();
		if(!deleted){
			log.addLog("[WARNING]["+apk.getName()+"]"+"[Failed to delet temp file please delete it manually]");
		}
		
		return success;
		
	}
	
	@Override
	public void run() {
		for(ThreadWatcher w : watchers){
			w.updateProgress();
		}
		// TODO Auto-generated method stub
		for (File apk : apks){
			boolean success = zipalignApk(apk);
			if(success){
				log.addLog("[INFO]["+apk.getName()+"]"+"[SUCCESS]");
			} else {
				log.addLog("[INFO]["+apk.getName()+"]"+"[FAILED]");
			}
			bar.setValue(bar.getValue()+1);
			bar.setString("Zipaligning apks "+this.percent());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		bar.setValue(bar.getMaximum());
		bar.setString("Done");
		for(ThreadWatcher w : watchers){
			w.done(this);
		}

	}

}
