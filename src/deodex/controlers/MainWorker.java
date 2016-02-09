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

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import deodex.R;
import deodex.S;
import deodex.SessionCfg;
import deodex.tools.Deodexer;
import deodex.tools.FilesUtils;
import deodex.tools.Logger;

public class MainWorker implements Runnable, ThreadWatcher, Watchable {

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

	ApkWorkerLegacy apk1l;
	ApkWorkerLegacy apk2l;
	JarWorkerLegacy jar1l;
	JarWorkerLegacy jar2l;

	boolean isinitialized = false;
	int maxThreading = 2;
	public JPanel mainPannel = new JPanel();
	JProgressBar progressBar;
	ArrayList<Runnable> tasks = new ArrayList<Runnable>();

	public MainWorker(File folder, LoggerPan logPane, int maxThreads) {
		workingThreadCount = maxThreads;
		this.logPan = logPane;
		this.folder = folder;
		if (SessionCfg.getSdk() > 20) {
			init();
		} else {
			initLegacy();
		}

	}

	@Override
	public void addThreadWatcher(ThreadWatcher watcher) {
		// TODO Auto-generated method stub
		threadWatcher = watcher;
	}

	@Override
	public void done(Runnable r) {
		if (tasks.size() > 0) {
			new Thread(tasks.get(0)).start();
			tasks.remove(0);
		}
		workingThreadCount--;
		// if (r.equals(apk1)) {
		// workingThreadCount--;
		// } else if (r.equals(apk2)) {
		// workingThreadCount--;
		//
		// } else if (r.equals(boot)) {
		// workingThreadCount--;
		//
		// } else if (r.equals(jar)) {
		// workingThreadCount--;
		// }
		if (workingThreadCount == 0) {
			logPan.saveToFile();
			FilesUtils.deleteRecursively(new File(SessionCfg.getSystemFolder().getAbsolutePath() + File.separator
					+ S.SYSTEM_FRAMEWORK + File.separator + SessionCfg.getArch()));
			FilesUtils.deleteRecursively(S.bootTmp.getParentFile().getParentFile());
			// TODO remove this
			Logger.logToStdIO("ALL JOBS THERMINATED ");
			// logPan.addLog(R.getString(S.LOG_INFO)+R.getString("mainWorker.alljobsDone"));
			// logPan.addLog(R.getString(S.LOG_INFO)+R.getString("mainworker.finallog"));
			progressBar.setValue(progressBar.getMaximum());
			progressBar.setString(R.getString("progress.done"));
			updateWatcher();
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

	private void initLegacy() {
		File framwork = new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK);
		File app = new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_APP);
		File privApp = new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_PRIV_APP);

		this.worker1List = new ArrayList<File>();
		this.worker2List = new ArrayList<File>();
		this.worker3List = new ArrayList<File>();
		this.worker4List = new ArrayList<File>();

		File[] appList = app.listFiles();
		File[] privList = null;
		if (privApp.exists())
			privList = privApp.listFiles();

		File framworkList[] = framwork.listFiles();

		for (File f : framworkList) {
			if (f.getName().endsWith(".odex")) {
				worker3List.add(f);
				FilesUtils.copyFileRecurcively(f, new File(S.bootTmpDex + File.separator + f.getName()));
			}
		}
		// FIXME: check this before
		isinitialized = true;
		for (File f : appList) {
			if (f.getName().endsWith(".odex")) {
				worker1List.add(f);
			}
		}
		if (privList != null) {
			for (File f : privList) {
				if (f.getName().endsWith(".odex")) {
					worker1List.add(f);
				}
			}
		}
		// framworklists
		if (worker3List.size() > 0) {
			int half = worker3List.size() / 2;
			for (int i = 0; i <= half; i++) {
				worker4List.add(worker3List.get(0));
				worker3List.remove(0);
			}
		}
		// apps
		if (worker1List.size() > 0) {
			int half = worker1List.size() / 2;
			for (int i = 0; i <= half; i++) {
				worker2List.add(worker1List.get(0));
				worker1List.remove(0);
			}
		}
		// initialize workers
		apk1l = new ApkWorkerLegacy(worker1List, logPan, S.worker1Folder, SessionCfg.sign, SessionCfg.zipalign);
		apk2l = new ApkWorkerLegacy(worker2List, logPan, S.worker2Folder, SessionCfg.sign, SessionCfg.zipalign);
		jar1l = new JarWorkerLegacy(worker3List, logPan, S.worker3Folder);
		jar2l = new JarWorkerLegacy(worker4List, logPan, S.worker4Folder);
		apk1l.addThreadWatcher(this);
		apk2l.addThreadWatcher(this);
		jar1l.addThreadWatcher(this);
		jar2l.addThreadWatcher(this);

		tasks = new ArrayList<Runnable>();
		tasks.add(apk1l);
		tasks.add(apk2l);
		tasks.add(jar1l);
		tasks.add(jar2l);
		this.initPannelLegacy();
	}

	public void initPannel() {
		progressBar = new JProgressBar();
		progressBar.setFont(R.COURIER_NORMAL);
		progressBar.setStringPainted(true);

		mainPannel.setSize(798, 222);
		mainPannel.setLayout(null);
		mainPannel.setBackground(new Color(206, 194, 229));
		apk1.getProgressBar().setBounds(10, 5, 780, 22);
		apk1.getProgressBar().setFont(R.COURIER_NORMAL);
		apk1.getProgressBar().setBackground(Color.white);
		apk1.getProgressBar().setForeground(new Color(0, 183, 92));

		apk2.getProgressBar().setBounds(10, 29, 780, 22);
		apk2.getProgressBar().setFont(R.COURIER_NORMAL);
		apk2.getProgressBar().setBackground(Color.white);
		apk2.getProgressBar().setForeground(new Color(0, 183, 92));

		jar.getProgressBar().setBounds(10, 53, 780, 22);
		jar.getProgressBar().setFont(R.COURIER_NORMAL);
		jar.getProgressBar().setBackground(Color.white);
		jar.getProgressBar().setForeground(new Color(0, 183, 92));

		boot.progressBar.setBounds(10, 77, 780, 22);
		boot.progressBar.setFont(R.COURIER_NORMAL);
		boot.progressBar.setBackground(Color.white);
		boot.progressBar.setForeground(new Color(0, 183, 92));

		progressBar.setBounds(10, 101, 780, 22);
		progressBar.setMinimum(0);
		progressBar.setMaximum(apk1.getProgressBar().getMaximum() + apk2.getProgressBar().getMaximum()
				+ jar.getProgressBar().getMaximum() + boot.progressBar.getMaximum());
		progressBar.setForeground(new Color(175, 122, 197));
		progressBar.setBackground(Color.WHITE);

		mainPannel.add(apk1.getProgressBar());
		mainPannel.add(apk2.getProgressBar());
		mainPannel.add(jar.getProgressBar());
		mainPannel.add(boot.progressBar);

		mainPannel.add(progressBar);
	}

	public void initPannelLegacy() {
		progressBar = new JProgressBar();
		progressBar.setFont(R.COURIER_NORMAL);
		progressBar.setStringPainted(true);

		mainPannel.setSize(798, 222);
		mainPannel.setLayout(null);
		mainPannel.setBackground(new Color(206, 194, 229));
		apk1l.getProgressBar().setBounds(10, 5, 780, 22);
		apk1l.getProgressBar().setFont(R.COURIER_NORMAL);
		apk1l.getProgressBar().setBackground(Color.white);
		apk1l.getProgressBar().setForeground(new Color(0, 183, 92));

		apk2l.getProgressBar().setBounds(10, 29, 780, 22);
		apk2l.getProgressBar().setFont(R.COURIER_NORMAL);
		apk2l.getProgressBar().setBackground(Color.white);
		apk2l.getProgressBar().setForeground(new Color(0, 183, 92));

		jar1l.getProgressBar().setBounds(10, 53, 780, 22);
		jar1l.getProgressBar().setFont(R.COURIER_NORMAL);
		jar1l.getProgressBar().setBackground(Color.white);
		jar1l.getProgressBar().setForeground(new Color(0, 183, 92));

		jar2l.progressBar.setBounds(10, 77, 780, 22);
		jar2l.progressBar.setFont(R.COURIER_NORMAL);
		jar2l.progressBar.setBackground(Color.white);
		jar2l.progressBar.setForeground(new Color(0, 183, 92));

		progressBar.setBounds(10, 101, 780, 22);
		progressBar.setMinimum(0);
		progressBar.setMaximum(
				this.worker1List.size() + this.worker2List.size() + this.worker3List.size() + this.worker4List.size());
		progressBar.setForeground(new Color(175, 122, 197));
		progressBar.setBackground(Color.WHITE);

		mainPannel.add(apk1l.getProgressBar());
		mainPannel.add(apk2l.getProgressBar());
		mainPannel.add(jar1l.getProgressBar());
		mainPannel.add(jar2l.progressBar);

		mainPannel.add(progressBar);
	}

	@Override
	public void run() {
		this.threadWatcher.updateProgress();

		for (int i = 0; i < this.maxThreading && tasks.size() > 0; i++) {
			new Thread(tasks.get(0)).start();
			tasks.remove(0);
		}
	}

	private synchronized void setProgress() {
		if (SessionCfg.getSdk() > 20) {
			progressBar.setValue(apk1.getProgressBar().getValue() + apk2.getProgressBar().getValue()
					+ boot.progressBar.getValue() + jar.progressBar.getValue());
			progressBar.setString(R.getString("overal.progress") + "(" + progressBar.getValue() + "/"
					+ progressBar.getMaximum() + ")");
		} else {
			progressBar.setValue(apk1l.getProgressBar().getValue() + apk2l.getProgressBar().getValue()
					+ jar1l.getProgressBar().getValue() + jar2l.getProgressBar().getValue());
			progressBar.setString(R.getString("overal.progress") + "(" + progressBar.getValue() + "/"
					+ progressBar.getMaximum() + ")");
		}
	}

	@Override
	public void updateProgress() {
		setProgress();

	}

	private void updateWatcher() {
		threadWatcher.done(this);
	}
}
