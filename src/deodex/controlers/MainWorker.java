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
import deodex.tools.Logger;

public class MainWorker implements Runnable, ThreadWatcher {

	private int workingThreadCount = 4;
	private int Totalprogress = 0;

	private ArrayList<File> worker1List;
	private Thread ApkWorker1;
	private int progressWorker1;
	private int TotalProgressWorker1;

	private ArrayList<File> worker2List;
	private Thread ApkWorker2;
	private int progressWorker2;
	private int TotalProgressWorker2;

	private ArrayList<File> worker3List;
	private Thread framWorker1;
	private int progressFramWorker1;
	private int TotalProgressFramWorker1;

	private ArrayList<File> worker4List;
	private Thread framWorker2;
	private int progressFramWorker2;
	private int TotalProgressFramWorker2;
	private LoggerPan logPan;

	File folder;
	BootWorker boot;
	JarWorker jar;
	ApkWorker apk1;
	ApkWorker apk2;
	boolean isinitialized = false;

	public MainWorker(File folder, LoggerPan logPane) {
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
		progressWorker1 = apk1.getProgressBar().getMinimum();
		TotalProgressWorker1 = apk1.getProgressBar().getMaximum();
		apk2 = new ApkWorker(worker2List, logPan, S.worker2Folder, SessionCfg.isSign(), SessionCfg.isZipalign());
		progressWorker2 = apk2.getProgressBar().getMinimum();
		TotalProgressWorker2 = apk2.getProgressBar().getMaximum();
		ApkWorker1 = new Thread(apk1);
		ApkWorker2 = new Thread(apk2);

		/// framework
		File[] list = framework.listFiles();
		worker3List = new ArrayList<File>();
		for (File f : list) {
			if (f.getName().endsWith(S.ODEX_EXT) || f.getName().endsWith(S.COMP_ODEX_EXT)) {
				worker3List.add(f);
			}
		}
		jar = new JarWorker(worker3List, logPan, S.worker3Folder);
		this.framWorker1 = new Thread(jar);

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
		this.framWorker2 = new Thread(boot);
		apk1.addThreadWatcher(this);
		apk2.addThreadWatcher(this);
		boot.addThreadWatcher(this);
		jar.addThreadWatcher(this);
	}

	private void initLegacy() {

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ApkWorker1.start();
		ApkWorker2.start();
		framWorker1.start();
		framWorker2.start();

	}

	public synchronized void stepProgress() {
		Totalprogress++;
		// TODO progress Bar set this new Value
	}

	@Override
	public void done(Runnable r) {
		// TODO Auto-generated method stub
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
					// TODO remove this
					Logger.logToStdIO("ALL JOBS THERMINATED ");
				}

			}).start();

		}
	}
}
