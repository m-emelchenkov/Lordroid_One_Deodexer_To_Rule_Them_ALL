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
import deodex.tools.Deodexer;
import deodex.tools.FilesUtils;
import deodex.ui.LoggerPan;

public class MainWorker implements Runnable{
	
	private int Totalprogress = 0;
	
	private ArrayList<File> worker1List;
	private Thread ApkWorker1;
	private int progressWorker1 ;
	private int TotalProgressWorker1 ;
	
	private ArrayList<File> worker2List;
	private Thread ApkWorker2;
	private int progressWorker2 ;
	private int TotalProgressWorker2 ;

	private ArrayList<File> worker3List;
	private Thread framWorker1;
	private int progressFramWorker1 ;
	private int TotalProgressFramWorker1 ;

	private ArrayList<File> worker4List;
	private Thread framWorker2;
	private int progressFramWorker2 ;
	private int TotalProgressFramWorker2 ;
	private LoggerPan logPan;
	
	File folder;
	
	boolean isinitialized = false; 
	public MainWorker(File folder ,LoggerPan logPane){
		this.logPan = logPane;
		this.folder = folder;
		if(SessionCfg.getSdk() > 20){
			init();
		} else {
			initLegacy();
		}
		
	}
	
	
	private void init(){
		isinitialized = FilesUtils.copyFile(SessionCfg.getBootOatFile(), S.bootTmp);
		isinitialized = isinitialized && Deodexer.oat2dexBoot(S.bootTmp);
		if(!isinitialized){
			return;
		}
		
		File appFolder = new File(folder.getAbsolutePath()+File.separator+"app");
		File privAppFolder = new File(folder.getAbsolutePath()+File.separator+S.SYSTEM_PRIV_APP);
		File framework = new File(folder.getAbsolutePath()+File.separator+S.SYSTEM_FRAMEWORK);
		File bootFiles = new File(S.bootTmpDex.getAbsolutePath());
		
		
		// apkfiles
		File[] apps = appFolder.listFiles();
		this.worker1List = new ArrayList<File>();
		for (File f : apps){
			if(f.isDirectory()){
				if (new File(f.getAbsolutePath()+File.separator+SessionCfg.getArch()).exists()){
					worker1List.add(f);
				}
			}
		}
		if (privAppFolder.exists()){
			File[] privApps = privAppFolder.listFiles();
			for (File f : privApps){
				if(f.isDirectory()){
					if (new File(f.getAbsolutePath()+File.separator+SessionCfg.getArch()).exists()){
						worker1List.add(f);
					}
				}
			}
		}
		int half = worker1List.size()/2;
		for (File f : worker1List){
			System.out.println(f.getName());
		}
		
		worker2List = new ArrayList<File>();
		for (int i = worker1List.size()-1 ; i >  half ; i = worker1List.size()-1){
			worker2List.add(worker1List.get(i));
			worker1List.remove(i);
		}
		
		for (File f : worker1List){
			System.out.println("[LIST 01]"+f.getName());
		}
		for (File f : worker2List){
			System.out.println("[LIST 02]"+f.getName());
		}
		ApkWorker apk1 = new ApkWorker(worker1List, logPan, S.worker1Folder,  SessionCfg.isSign(),SessionCfg.isZipalign());
		progressWorker1 = apk1.getProgressBar().getMinimum();
		TotalProgressWorker1 = apk1.getProgressBar().getMaximum();
		ApkWorker apk2 = new ApkWorker(worker2List, logPan, S.worker2Folder,  SessionCfg.isSign(),SessionCfg.isZipalign());
		progressWorker2 = apk2.getProgressBar().getMinimum();
		TotalProgressWorker2 = apk2.getProgressBar().getMaximum();
		ApkWorker1 = new Thread (apk1);
		ApkWorker2 = new Thread (apk2);
		
		
		
		
	}
	
	
	
	
	private void initLegacy(){
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ApkWorker1.start();
		ApkWorker2.start();
		
		
	}

	
	public synchronized void stepProgress(){
		Totalprogress ++;
		// TODO progress Bar set this new Value
	}
}
