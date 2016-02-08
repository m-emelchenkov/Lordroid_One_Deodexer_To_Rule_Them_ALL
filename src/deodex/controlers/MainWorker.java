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

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import deodex.R;
import deodex.S;
import deodex.SessionCfg;
import deodex.tools.Deodexer;
import deodex.tools.FilesUtils;
import deodex.tools.Logger;
import deodex.ui.Window;

public class MainWorker implements Runnable, ThreadWatcher ,Watchable{

	private int workingThreadCount = 4;

	private ArrayList<File> worker1List;

	private ArrayList<File> worker2List;

	private ArrayList<File> worker3List;

	private ArrayList<File> worker4List;
	private LoggerPan logPan;

	ThreadWatcher threadWatcher;
	File folder;
	BootWorker boot;
	JarWorker jar;
	ApkWorker apk1;
	ApkWorker apk2;
	boolean isinitialized = false;
	int maxThreading = 2;
	public JPanel mainPannel = new JPanel ();
	JProgressBar progressBar;
	ArrayList<Runnable> tasks = new ArrayList<Runnable>();
	
	public MainWorker(File folder, LoggerPan logPane,int maxThreads) {
		workingThreadCount = maxThreads;
		this.logPan = logPane;
		this.folder = folder;
		if (SessionCfg.getSdk() > 20) {
			init();
		} else {
			initLegacy();
		}
		
	}

	private void init() {
		isinitialized = FilesUtils.copyFile(SessionCfg.getBootOatFile(), S.bootTmp);
		isinitialized = isinitialized && Deodexer.oat2dexBoot(S.bootTmp);
		if (!isinitialized) {
			return;
		}

		File appFolder = new File(folder.getAbsolutePath() + File.separator + "app");
		File privAppFolder = new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_PRIV_APP);
		File framework = new File(
				folder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK + File.separator + SessionCfg.getArch());
		File bootFiles = new File(S.bootTmpDex.getAbsolutePath());

		// apkfiles
		File[] apps = appFolder.listFiles();
		this.worker1List = new ArrayList<File>();
		for (File f : apps) {
			if (f.isDirectory()) {
				if (new File(f.getAbsolutePath() + File.separator + SessionCfg.getArch()).exists()) {
					worker1List.add(f);
				}
			}
		}
		if (privAppFolder.exists()) {
			File[] privApps = privAppFolder.listFiles();
			for (File f : privApps) {
				if (f.isDirectory()) {
					if (new File(f.getAbsolutePath() + File.separator + SessionCfg.getArch()).exists()) {
						worker1List.add(f);
					}
				}
			}
		}
		int half = worker1List.size() / 2;

		worker2List = new ArrayList<File>();
		for (int i = worker1List.size() - 1; i > half; i = worker1List.size() - 1) {
			worker2List.add(worker1List.get(i));
			worker1List.remove(i);
		}

		apk1 = new ApkWorker(worker1List, logPan, S.worker1Folder, SessionCfg.isSign(), SessionCfg.isZipalign());
		apk2 = new ApkWorker(worker2List, logPan, S.worker2Folder, SessionCfg.isSign(), SessionCfg.isZipalign());

		/// framework
		File[] list = framework.listFiles();
		worker3List = new ArrayList<File>();
		for (File f : list) {
			if (f.getName().endsWith(S.ODEX_EXT) || f.getName().endsWith(S.COMP_ODEX_EXT)) {
				worker3List.add(f);
			}
		}
		jar = new JarWorker(worker3List, logPan, S.worker3Folder);

		// bootFile
		File[] boots = bootFiles.listFiles();
		worker4List = new ArrayList<File>();
		for (File f : boots) {
			if (!f.getName().equals("framework-classes2.dex")) {
				if (f.getName().endsWith(".dex")) {
					worker4List.add(f);
				}
			}
		}
		boot = new BootWorker(worker4List, S.worker4Folder, this.logPan);
		apk1.addThreadWatcher(this);
		apk2.addThreadWatcher(this);
		boot.addThreadWatcher(this);
		jar.addThreadWatcher(this);
		tasks.add(apk1);
		tasks.add(apk2);
		tasks.add(jar);
		tasks.add(boot);
		this.initPannel();
	}

	public void initPannel(){
		progressBar = new JProgressBar();
		progressBar.setFont(R.COURIER_NORMAL);
		progressBar.setStringPainted(true);
		
		mainPannel.setSize(798, Window.W_HEIGHT-300-100);
		mainPannel.setLayout(null);
		mainPannel.setBackground(new Color(206, 194, 229));
		apk1.getProgressBar().setBounds(10, 5, 780, 22);
		apk1.getProgressBar().setFont(R.COURIER_NORMAL);
		
		apk2.getProgressBar().setBounds(10, 29, 780, 22);
		apk2.getProgressBar().setFont(R.COURIER_NORMAL);
		
		jar.getProgressBar().setBounds(10, 53, 780, 22);
		jar.getProgressBar().setFont(R.COURIER_NORMAL);
		
		boot.progressBar.setBounds(10, 77, 780, 22);
		boot.progressBar.setFont(R.COURIER_NORMAL);
		
		progressBar.setBounds(10, 101, 780, 22);
		progressBar.setMinimum(0);
		progressBar.setMaximum(apk1.getProgressBar().getMaximum()+apk2.getProgressBar().getMaximum()+jar.getProgressBar().getMaximum()
				+boot.progressBar.getMaximum());

		
		JLabel apk1Lab = new JLabel("task 01");
		apk1Lab.setBounds(5, 5, 90, 22);
		apk1Lab.setFont(R.COURIER_NORMAL);
		
		JLabel apk2Lab = new JLabel("task 02");
		apk2Lab.setBounds(5, 29, 90, 22);
		apk2Lab.setFont(R.COURIER_NORMAL);
		
		JLabel jarLab = new JLabel("task 03");
		jarLab.setBounds(5, 53, 90, 22);
		jarLab.setFont(R.COURIER_NORMAL);
		
		JLabel bootLab = new JLabel("task 04");
		bootLab.setBounds(5, 77, 90, 22);
		bootLab.setFont(R.COURIER_NORMAL);
		mainPannel.add(apk1.getProgressBar());
		mainPannel.add(apk2.getProgressBar());
		mainPannel.add(jar.getProgressBar());
		mainPannel.add(boot.progressBar);
		//mainPannel.add(apk1Lab );
		//mainPannel.add( apk2Lab);
		//mainPannel.add( jarLab);
		//mainPannel.add(bootLab );
		mainPannel.add(progressBar);
	}
	
	
	private void initLegacy() {

	}

	@Override
	public void run() {
		this.threadWatcher.updateProgress();

		// TODO Auto-generated method stub
		for (int i = 0 ; i < this.maxThreading ; i++){
			new Thread(tasks.get(0)).start();
			tasks.remove(0);
		}
	}

	public synchronized void stepProgress() {
		
		// TODO progress Bar set this new Value we will see this later
	}

	@Override
	public void done(Runnable r) {
		// TODO Auto-generated method stub
		if(tasks.size() > 0){
			new Thread(tasks.get(0)).start();
			tasks.remove(0);
		}
		
		if (r.equals(apk1)) {
			workingThreadCount--;
		} else if (r.equals(apk2)) {
			workingThreadCount--;

		} else if (r.equals(boot)) {
			workingThreadCount--;

		} else if (r.equals(jar)) {
			workingThreadCount--;
		}
		if (workingThreadCount == 0) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					FilesUtils.deleteRecursively(new File(SessionCfg.getSystemFolder().getAbsolutePath()
							+ File.separator + S.SYSTEM_FRAMEWORK + File.separator + SessionCfg.getArch()));
					FilesUtils.deleteRecursively(S.bootTmp.getParentFile().getParentFile());
					// TODO remove this
					Logger.logToStdIO("ALL JOBS THERMINATED ");
					logPan.addLog(R.getString(S.LOG_INFO)+R.getString("mainWorker.alljobsDone"));
					logPan.addLog(R.getString(S.LOG_INFO)+R.getString("mainworker.finallog"));
					logPan.saveToFile();
					progressBar.setValue(progressBar.getMaximum());
					progressBar.setString(R.getString("progress.done"));
					updateWatcher();
				}

			}).start();

		}
	}

	private void updateWatcher(){
		threadWatcher.done(this);
	}
	private synchronized void setProgress(){
		progressBar.setValue(apk1.getProgressBar().getValue()+apk2.getProgressBar().getValue()+boot.progressBar.getValue()+
				jar.progressBar.getValue());
		progressBar.setString(R.getString("overal.progress")+"("+progressBar.getValue()+"/"+progressBar.getMaximum()+")");
	}
	@Override
	public void updateProgress() {
		setProgress();
		
	}

	@Override
	public void addThreadWatcher(ThreadWatcher watcher) {
		// TODO Auto-generated method stub
		threadWatcher = watcher;
	}
}
