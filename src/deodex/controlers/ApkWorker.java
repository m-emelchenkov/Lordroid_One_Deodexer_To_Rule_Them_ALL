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
import deodex.obj.ApkObj;
import deodex.tools.Deodexer;
import deodex.tools.FilesUtils;
import deodex.tools.Logger;
import deodex.tools.Zip;
import deodex.tools.ZipTools;

public class ApkWorker implements Runnable {

	ArrayList<File> apkList;
	LoggerPan logPan;
	File tmpFolder;
	boolean doSign;
	boolean doZipalign;
	public JProgressBar progressBar;
	ThreadWatcher threadWatcher;
	private boolean signStatus = false;
	private boolean zipAlignStatus = false;

	/**
	 * constructor the argument is a list of the APk objectes to be deodexed
	 * (lolipop and above)
	 * 
	 * @param apkFoldersList
	 */
	public ApkWorker(ArrayList<File> apkFoldersList, LoggerPan logPan, File tmpFolder, boolean doSign,
			boolean doZipalign) {
		apkList = apkFoldersList;
		this.logPan = logPan;
		this.tmpFolder = tmpFolder;
		this.doSign = doSign;
		this.doZipalign = doZipalign;

		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		if (apkList != null)
			progressBar.setMaximum(apkList.size() <= 0 ? 2 : apkList.size() * 8);
		else
			progressBar.setMaximum(1);
		progressBar.setStringPainted(true);
		this.tmpFolder.mkdirs();
	}

	/**
	 * @param threadWatcher
	 *            the threadWatcher to set
	 */
	public void addThreadWatcher(ThreadWatcher threadWatcher) {
		this.threadWatcher = threadWatcher;
	}

	private boolean deodexApk(File apkFolder) {
		ApkObj apk = new ApkObj(apkFolder);
		Logger.writLog("Processing "+apk.getOrigApk().getName() +" ...");
		// phase 01 copying to temp forlder
		Logger.writLog(apk.getOrigApk().getName()+"Copying needed Files to working folder ...");
		boolean copyStatus = apk.copyNeededFilesToTempFolder(tmpFolder);
		if (!copyStatus) { // returns
			logPan.addLog(R.getString(S.LOG_WARNING) + " [" + apk.getOrigApk().getName() + "]"
					+ R.getString("log.copy.to.tmp.failed"));
			Logger.writLog(apk.getOrigApk().getName()+" Failed to copy needed files ");
			return false;
		}
		Logger.writLog(apk.getOrigApk().getName()+" copy files to temp folder successfull ! ");
		progressBar.setValue(progressBar.getValue() + 1);
		progressBar.setString(R.getString("progress.apks") + " (" + this.getPercent() + "%)");
		threadWatcher.updateProgress();

		// phase 02 extract xz if evailable
		boolean extraxtStatus = false;
		try {
			extraxtStatus = ZipTools.extractOdex(apk.getTempCompOdex());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!extraxtStatus) {
			logPan.addLog(R.getString(S.LOG_WARNING) + " [" + apk.getOrigApk().getName() + "]"
					+ R.getString("log.extract.to.tmp.failed"));
			FilesUtils.deleteRecursively(apk.getTempApk().getParentFile());
			return false;
		}
		progressBar.setValue(progressBar.getValue() + 1);
		progressBar.setString(R.getString("progress.apks") + " (" + this.getPercent() + "%)");
		threadWatcher.updateProgress();

		// phase 03 deodexing (most of the processing time is spend here)
		boolean dexStatus = Deodexer.deodexApk(apk.getTempOdex(), apk.getTempDex());
		if (!dexStatus) {
			Logger.logToStdIO(apk.getOrigApk().getName() + " Failed with method1 trying method 2");
			dexStatus = Deodexer.deodexApkFailSafe(apk.getTempOdex(), apk.getTempDex());
			if (!dexStatus) {
				logPan.addLog(R.getString(S.LOG_WARNING) + " [" + apk.getOrigApk().getName() + "]"
						+ R.getString("log.deodex.failed"));
				FilesUtils.deleteRecursively(apk.getTempApk().getParentFile());
				return false;
			}
		}
		progressBar.setValue(progressBar.getValue() + 1);
		progressBar.setString(R.getString("progress.apks") + " (" + this.getPercent() + "%)");
		threadWatcher.updateProgress();

		// phase 04 renamming (FIXME: why copy instead of rename? is it relly
		// safer ?)
		boolean rename = apk.getTempDex().renameTo(apk.getTempClasses1());// FilesUtils.copyFile(apk.getTempDex(), apk.getTempClasses1());
		if (apk.getTempDex2().exists()) {
			rename = rename &&  apk.getTempDex2().renameTo(apk.getTempClasses2());  //FilesUtils.copyFile(apk.getTempDex2(), apk.getTempClasses2());
		}
		if (!rename) {
			logPan.addLog(R.getString(S.LOG_WARNING) + " [" + apk.getOrigApk().getName() + "]"
					+ R.getString("log.classes.failed"));
			FilesUtils.deleteRecursively(apk.getTempApk().getParentFile());
			return false;

		}
		progressBar.setValue(progressBar.getValue() + 1);
		progressBar.setString(R.getString("progress.apks") + " (" + this.getPercent() + "%)");
		threadWatcher.updateProgress();

		// phase 5
		ArrayList<File> classesFiles = new ArrayList<File>();
		classesFiles.add(apk.getTempClasses1());
		if (apk.getTempClasses2().exists())
			classesFiles.add(apk.getTempClasses2());
		boolean addClassesToApkStatus = false;
		try {
			addClassesToApkStatus = Zip.addFilesToExistingZip(apk.getTempApk(), classesFiles);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!addClassesToApkStatus) {
			logPan.addLog(R.getString(S.LOG_WARNING) + " [" + apk.getOrigApk().getName() + "]"
					+ R.getString("log.add.classes.failed"));
			// FilesUtils.deleteRecursively(apk.getTempApk().getParentFile());
			return false;
		}
		progressBar.setValue(progressBar.getValue() + 1);
		progressBar.setString(R.getString("progress.apks") + " (" + this.getPercent() + "%)");
		threadWatcher.updateProgress();

		// phase 6
		if (this.doSign) {
			// TODO sign !
			try {
				this.signStatus = Deodexer.signApk(apk.getTempApk(), apk.getTempApkSigned());
				if(!this.signStatus)
					apk.getTempApk().renameTo(apk.getTempApkSigned());
			} catch (IOException | InterruptedException e) {
				apk.getTempApk().renameTo(apk.getTempApkSigned());
			}
		} else {
			//FilesUtils.copyFile(apk.getTempApk(), apk.getTempApkSigned());
			apk.getTempApk().renameTo(apk.getTempApkSigned());

		}
		progressBar.setValue(progressBar.getValue() + 1);
		progressBar.setString(R.getString("progress.apks") + " (" + this.getPercent() + "%)");
		threadWatcher.updateProgress();

		// phase 7
		if (this.doZipalign) {
			try {
				this.zipAlignStatus = Zip.zipAlignAPk(apk.getTempApkSigned(), apk.getTempApkZipalign());
				if(!this.zipAlignStatus)
					apk.getTempApkSigned().renameTo(apk.getTempApkZipalign());
			} catch (IOException | InterruptedException e) {
				apk.getTempApkSigned().renameTo(apk.getTempApkZipalign());
			}
		} else {
			apk.getTempApkSigned().renameTo(apk.getTempApkZipalign());
		}
		progressBar.setValue(progressBar.getValue() + 1);
		progressBar.setString(R.getString("progress.apks") + " (" + this.getPercent() + "%)");
		threadWatcher.updateProgress();

		// phase 08
		// the process is successful now copy and clean !
		boolean putBackStatus = FilesUtils.copyFile(apk.getTempApkZipalign(), apk.getOrigApk());
		if (!putBackStatus) {
			Logger.logToStdIO("Failed to copy back " + apk.getPureName());
			return false;
		}
		// delete the arch folder clearlly we dont need it any more

		FilesUtils.deleteFiles(FilesUtils.searchrecursively(apk.getFolder(), S.ODEX_EXT));
		FilesUtils.deleteFiles(FilesUtils.searchrecursively(apk.getFolder(), S.COMP_ODEX_EXT));
		FilesUtils.deleteUmptyFoldersInFolder(apk.getFolder());

		FilesUtils.deleteRecursively(apk.getTempApkZipalign().getParentFile());

		return true;
	}

	/**
	 * @return the progressBar
	 */
	public JProgressBar getProgressBar() {
		return progressBar;
	}

	/**
	 * @return the threadWatcher
	 */
	public ThreadWatcher getThreadWatcher() {
		return threadWatcher;
	}

	private int getPercent() {
		// max ===> 100
		// value ===> ?
		// ? = value*100/max;
		return (this.progressBar.getValue() * 100) / this.progressBar.getMaximum();

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (apkList != null && apkList.size() > 0) {
			for (File apk : apkList) {

				boolean sucess = deodexApk(apk);
				if (!sucess) {
					logPan.addLog(R.getString(S.LOG_ERROR) + "[" + new ApkObj(apk).getOrigApk().getName() + "]"
							+ R.getString(S.LOG_FAIL));
				} else {
					logPan.addLog(
							R.getString(S.LOG_INFO) + "[" + new ApkObj(apk).getOrigApk().getName() + "]"
									+ R.getString(S.LOG_SUCCESS)
									+ (this.doSign ? (this.signStatus ? R.getString("log.resign.ok")
											: R.getString("log.resign.fail")) : "")
									+ (this.doZipalign ? (this.zipAlignStatus ? R.getString("log.zipalign.ok")
											: R.getString("log.zipalign.fail")) : ""));
				}
				progressBar.setValue(progressBar.getValue() + 1);
				progressBar.setString(R.getString("progress.apks") + " (" + this.getPercent() + ")");

				threadWatcher.updateProgress();
			}
		}

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finalMove();
		}
		finalMove();
	}

	private void finalMove() {
		FilesUtils.deleteRecursively(tmpFolder);
		progressBar.setValue(progressBar.getMaximum());
		progressBar.setString(R.getString("progress.done"));
		this.threadWatcher.updateProgress();
		this.threadWatcher.done(this);
	}

	/**
	 * @param progressBar
	 *            the progressBar to set
	 */
	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}
}
