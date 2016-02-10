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
import deodex.obj.ApkLegacy;
import deodex.tools.Deodexer;
import deodex.tools.FilesUtils;
import deodex.tools.Logger;
import deodex.tools.Zip;

public class ApkWorkerLegacy implements Watchable, Runnable {

	public JProgressBar progressBar = new JProgressBar();
	ArrayList<File> apkList;
	LoggerPan logPan;
	boolean doSign;
	boolean doZipalign;
	ThreadWatcher threadWatcher;
	File tempFolder;

	public ApkWorkerLegacy(ArrayList<File> apkList, LoggerPan logPan, File tempFolder, boolean doSign,
			boolean doZipalign) {
		this.apkList = apkList;
		this.logPan = logPan;
		this.doSign = doSign;
		this.doZipalign = doZipalign;
		this.tempFolder = tempFolder;
		progressBar.setMinimum(0);
		progressBar.setMaximum(this.apkList.size() > 0 ? this.apkList.size() : 1);
		progressBar.setStringPainted(true);
	}

	@Override
	public void addThreadWatcher(ThreadWatcher watcher) {
		// TODO Auto-generated method stub
		this.threadWatcher = watcher;
	}

	private boolean deodexApk(ApkLegacy apk) {
		boolean copyStatus = false;
		copyStatus = apk.copyNeededFiles(tempFolder);

		if (!copyStatus) {
			// TODO add loggin for this
			//Logger.logToStdIO("[" + apk.origApk.getName() + "]  failedTocopy");
			return false;
		} else {
			// we deodex now !
			boolean deodexStatus = false;
			deodexStatus = Deodexer.deoDexApkLegacy(apk.tempOdex, apk.classes);
			//Logger.logToStdIO(apk.tempOdex.getAbsolutePath());
			//Logger.logToStdIO(apk.classes.getAbsolutePath());
			if (!deodexStatus) {
				//Logger.logToStdIO("[" + apk.origApk.getName() + "] failed to deodex aborting");
				//Logger.logToStdIO(apk.tempOdex.getAbsolutePath());
				//Logger.logToStdIO(apk.classes.getAbsolutePath());

				return false;
			} else {
				ArrayList<File> classes = new ArrayList<File>();
				classes.add(apk.classes);
				boolean putBack = false;
				try {
					putBack = Zip.addFilesToExistingZip(apk.tempApk, classes);
				} catch (IOException e) {

					e.printStackTrace();
				}
				if (!putBack) {
					Logger.logToStdIO(apk.origApk.getName() + " failed to push classes.dex back to apk ");
				} else {
					if (this.doSign) {
						try {
							Deodexer.signApk(apk.tempApk, apk.tempSigned);
						} catch (IOException | InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						FilesUtils.copyFileRecurcively(apk.tempApk, apk.tempSigned);
					}
					if (this.doZipalign) {
						try {
							Zip.zipAlignAPk(apk.tempSigned, apk.tempZipaligned);
						} catch (IOException | InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						FilesUtils.copyFile(apk.tempSigned, apk.tempZipaligned);
					}

				}

			}

		}

		// if we got here all things are right put stuffBack
		boolean pushBack = FilesUtils.copyFile(apk.tempZipaligned, apk.origApk);
		if (pushBack)
			FilesUtils.deleteRecursively(apk.origOdex);
		else
			return false;

		// clean temp dir we don't wanna
		FilesUtils.deleteRecursively(apk.tempApk);
		FilesUtils.deleteRecursively(apk.tempOdex);
		FilesUtils.deleteRecursively(apk.classes);
		FilesUtils.deleteRecursively(apk.tempSigned);
		FilesUtils.deleteRecursively(apk.tempZipaligned);

		return true;
	}

	/**
	 * @return the progressBar
	 */
	public JProgressBar getProgressBar() {
		return progressBar;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (apkList != null && apkList.size() > 0) {
			for (File f : this.apkList) {
				ApkLegacy apk = new ApkLegacy(f);
				boolean success = this.deodexApk(apk);
				if (success) {
					logPan.addLog("[" + apk.origApk.getName() + "]" + R.getString(S.LOG_SUCCESS));
				} else {
					logPan.addLog("[" + apk.origApk.getName() + "]" + R.getString(S.LOG_FAIL));
				}
				progressBar.setValue(progressBar.getValue() + 1);
				progressBar.setString(R.getString("progress.apks") + " (" + progressBar.getValue() + "/"
						+ progressBar.getMaximum() + ")");
				this.threadWatcher.updateProgress();
			}
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		progressBar.setValue(progressBar.getMaximum());
		progressBar.setString(R.getString("progress.done"));
		this.threadWatcher.updateProgress();
		threadWatcher.done(this);
	}

	/**
	 * @param progressBar
	 *            the progressBar to set
	 */
	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

}
