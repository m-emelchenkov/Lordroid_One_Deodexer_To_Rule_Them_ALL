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
import deodex.tools.Zip;
import deodex.tools.ZipTools;

public class ApkWorker implements Runnable {

	/**
	 * constructor the argument is a list of the APk objectes to be deodexed
	 * (lolipop and above)
	 * 
	 * @param apkFoldersList
	 */
	ArrayList<File> apkList;
	LoggerPan logPan;
	File tmpFolder;
	boolean doSign;
	boolean doZipalign;
	public JProgressBar progressBar;
	ThreadWatcher threadWatcher;

	public ApkWorker(ArrayList<File> apkFoldersList, LoggerPan logPan, File tmpFolder, boolean doSign,
			boolean doZipalign) {
		apkList = apkFoldersList;
		this.logPan = logPan;
		this.tmpFolder = tmpFolder;
		this.doSign = doSign;
		this.doZipalign = doZipalign;

		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(apkList.size());
		progressBar.setStringPainted(true);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		int i = 0;
		for (File apk : apkList) {

			boolean sucess = deodexApk(apk);
			if (!sucess) {
				logPan.addLog("[ " + apk.getName() + ".apk  ]" + R.getString(S.LOG_FAIL));
			} else {
				logPan.addLog("[ " + apk.getName() + ".apk  ]" + R.getString(S.LOG_SUCCESS));
			}
			progressBar.setValue(i++);
			System.out.println("Progress = " + progressBar.getValue() + " / " + progressBar.getMaximum());
		}

		logPan.addLog("All jobs trminaled !");
		System.out.println("All jobs done for this thread !");
		//FilesUtils.deleteRecursively(tmpFolder);
		if(!this.threadWatcher.equals(null))
		this.threadWatcher.done(this);
	}

	/**
	 * @return the threadWatcher
	 */
	public ThreadWatcher getThreadWatcher() {
		return threadWatcher;
	}

	/**
	 * @param threadWatcher the threadWatcher to set
	 */
	public void addThreadWatcher(ThreadWatcher threadWatcher) {
		this.threadWatcher = threadWatcher;
	}

	/**
	 * @return the progressBar
	 */
	public JProgressBar getProgressBar() {
		return progressBar;
	}

	/**
	 * @param progressBar
	 *            the progressBar to set
	 */
	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	private boolean deodexApk(File apkFolder) {
		ApkObj apk = new ApkObj(apkFolder);
		// boolean deodexed = true;
		//
		// if(!deodexed){
		// logPan.addLog(R.getString(S.LOG_INFO) + " [" +
		// apk.getOrigApk().getName() + "]"
		// + R.getString("log.copy.to.tmp.failed"));
		// return true;
		// }

		boolean copyStatus = apk.copyNeededFilesToTempFolder(tmpFolder);
		if (!copyStatus) {
			logPan.addLog(R.getString(S.LOG_WARNING) + " [" + apk.getOrigApk().getName() + "]"
					+ R.getString("log.copy.to.tmp.failed"));
			return false;
		} else {
			boolean extraxtStatus = false;
			try {
				extraxtStatus = ZipTools.extractOdex(apk.getTempOdex());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!extraxtStatus) {
				logPan.addLog(R.getString(S.LOG_WARNING) + " [" + apk.getOrigApk().getName() + "]"
						+ R.getString("log.extract.to.tmp.failed"));
				FilesUtils.deleteRecursively(apk.getTempApk().getParentFile());
				return false;
			} else {
				boolean dexStatus = Deodexer.deodexApk(apk.getTempOdex(), apk.getTempDex());
				if (!dexStatus) {
					logPan.addLog(R.getString(S.LOG_WARNING) + " [" + apk.getOrigApk().getName() + "]"
							+ R.getString("log.deodex.failed"));
					FilesUtils.deleteRecursively(apk.getTempApk().getParentFile());
					return false;
				} else {
					boolean rename = FilesUtils.copyFile(apk.getTempDex(), apk.getTempClasses1());
					if (apk.getTempDex2().exists()) {
						rename = rename && FilesUtils.copyFile(apk.getTempDex2(), apk.getTempClasses2());
					}
					if (!rename) {
						logPan.addLog(R.getString(S.LOG_WARNING) + " [" + apk.getOrigApk().getName() + "]"
								+ R.getString("log.classes.failed"));
						FilesUtils.deleteRecursively(apk.getTempApk().getParentFile());
						return false;

					} else {
						ArrayList<File> classesFiles = new ArrayList<File>();
						classesFiles.add(apk.getTempClasses1());
						if (apk.getTempClasses2().exists())
							classesFiles.add(apk.getTempClasses1());
						boolean addClassesToApkStatus =false;
						try {
							addClassesToApkStatus = Zip.addFilesToExistingZip(apk.getTempApk(), classesFiles);
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (!addClassesToApkStatus) {
							logPan.addLog(R.getString(S.LOG_WARNING) + " [" + apk.getOrigApk().getName() + "]"
									+ R.getString("log.add.classes.failed"));
							//FilesUtils.deleteRecursively(apk.getTempApk().getParentFile());
							return false;
						} else {
							if (this.doSign) {
								// TODO sign !
							} else {
								FilesUtils.copyFile(apk.getTempApk(), apk.getTempApkSigned());

							}
							if (this.doZipalign) {
								// TODO zipalign this app
							} else {
								FilesUtils.copyFile(apk.getTempApkSigned(), apk.getTempApkZipalign());
							}
							//FilesUtils.copyFile(apk.getTempApkZipalign() , apk.getOrigApk());
						}
					}
				}
			}
		}
	//	 the process is successful no copy and clean !
		 FilesUtils.copyFile(apk.getTempApkZipalign(), apk.getOrigApk());
		
		 // delete the arch folder clearlly we dont need it any more
		 File[] files = apk.getArchFolder().listFiles();
		 // make sure there is no libs in that folder before deleting it
		// (bootloops are no tolerated ! )
		 for( File f : files){
		 if(!f.getName().endsWith(".so"))
		 FilesUtils.deleteRecursively(f);
		 }
		 files = apk.getArchFolder().listFiles();
		 if(files.length < 0){
		 apk.getArchFolder().delete();
		 }
		 FilesUtils.deleteRecursively(apk.getTempApkZipalign().getParentFile());
		

		return true;
	}
}
