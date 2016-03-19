/*
 *  Lordroid One Deodexer To Rule Them All
 * 
 *  Copyright 2016 Rachid Boudjelida <rachidboudjelida@gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package deodex.controlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.alee.laf.progressbar.WebProgressBar;

import deodex.R;
import deodex.S;
import deodex.SessionCfg;
import deodex.obj.JarObj;
import deodex.tools.Deodexer;
import deodex.tools.FilesUtils;
import deodex.tools.Logger;
import deodex.tools.Zip;
import deodex.tools.ZipTools;

public class JarWorker implements Runnable, Watchable {

	ArrayList<File> odexFiles = new ArrayList<File>();

	LoggerPan logPan;
	File tmpFolder;
	WebProgressBar progressBar;
	private ThreadWatcher threadWatcher;

	/**
	 * 
	 * @param odexList
	 *            the odex list of files to be deodexed
	 * @param logPan
	 *            : the LoggerPan where all the logs will be
	 * @param tmpFolder
	 *            : the workingfolder (scratch folder) a temps locations
	 */
	public JarWorker(ArrayList<File> odexList, LoggerPan logPan, File tmpFolder) {
		this.odexFiles = odexList;
		this.logPan = logPan;
		this.tmpFolder = tmpFolder;
		this.progressBar = new WebProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(odexList.size() * 6);
		progressBar.setStringPainted(true);
		tmpFolder.mkdirs();
	}

	@Override
	public void addThreadWatcher(ThreadWatcher watcher) {
		// TODO Auto-generated method stub
		this.threadWatcher = watcher;

	}

	/**
	 * 
	 * @param odex
	 *            odex file to be deodexed
	 * @return true only if all operations went successful and the odex file was
	 *         deodexed
	 */
	private boolean deodexJar(JarObj jar) {
		// phase 1
		Logger.appendLog("[JarWorker][I] about to copy needed files for " + jar.getAbsoluteName());
		boolean copyStatus = jar.copyNeedFiles(tmpFolder);
		if (!copyStatus) {
			Logger.appendLog("[JarWorker][E] copy of needed files for " + jar.getAbsoluteName() + "failed ");
			this.logPan.addLog(
					R.getString(S.LOG_ERROR) + "[" + jar.getOrigJar() + "]" + R.getString("log.copy.to.tmp.failed"));
			return false;
		}
		Logger.appendLog("[JarWorker][I] copy of needed files for " + jar.getAbsoluteName() + " success ");
		this.progressBar.setValue(this.progressBar.getValue() + 1);
		progressBar.setString(R.getString("progress.jar") + " " + this.getPercent() + "%");
		threadWatcher.updateProgress();

		// phase 02
		boolean extractStatus = false;
		try {
			Logger.appendLog("[JarWorker][I][" + jar.getAbsoluteName() + "]trying to extract odex file ");
			extractStatus = ZipTools.extractOdex(jar.getTmpCompodex());
		} catch (IOException e) {
			this.logPan.addLog(
					R.getString(S.LOG_ERROR) + "[" + jar.getOrigJar() + "]" + R.getString("log.extract.to.tmp.failed"));
			e.printStackTrace();
			Logger.appendLog(
					"[JarWorker][E][" + jar.getAbsoluteName() + "]trying to extract odex file failed with exception");
			Logger.appendLog("[JarWorker][EX]" + e.getStackTrace());
			return false;

		}
		if (!extractStatus) {
			Logger.appendLog("[JarWorker][E][" + jar.getAbsoluteName() + "]trying to extract odex file failed");
			this.logPan.addLog(
					R.getString(S.LOG_ERROR) + "[" + jar.getOrigJar() + "]" + R.getString("log.extract.to.tmp.failed"));
			return false;
		}
		Logger.appendLog("[JarWorker][I][" + jar.getAbsoluteName() + "]trying to extract odex file success");
		this.progressBar.setValue(this.progressBar.getValue() + 1);
		progressBar.setString(R.getString("progress.jar") + " " + this.getPercent() + "%");
		threadWatcher.updateProgress();

		// phase 3
		boolean deodexStatus = false;
		Logger.appendLog("[JarWorker][I][" + jar.getAbsoluteName() + "] about to deodex odex file ");
		deodexStatus = Deodexer.deodexApk(jar.getTmpodex(), jar.getTmpdex());
		if (!deodexStatus) {
			Logger.appendLog("[JarWorker][W][" + jar.getAbsoluteName()
					+ "] about to deodex odex file failed with oat2dex trying smali/backsmali");
			deodexStatus = Deodexer.deodexApkFailSafe(jar.getTmpodex(), jar.getTmpdex());
			if (!deodexStatus) {
				Logger.appendLog("[JarWorker][E][" + jar.getAbsoluteName()
						+ "] about to deodex odex file failed with oat2dex and smali/backsmali fatal");
				this.logPan.addLog(
						R.getString(S.LOG_ERROR) + "[" + jar.getOrigJar() + "]" + R.getString("log.deodex.failed"));
				return false;
			}
		}
		Logger.appendLog("[JarWorker][I][" + jar.getAbsoluteName() + "] about to deodex odex file success");
		this.progressBar.setValue(this.progressBar.getValue() + 1);
		progressBar.setString(R.getString("progress.jar") + " " + this.getPercent() + "%");
		threadWatcher.updateProgress();

		// phase 4
		boolean rename = false;
		rename = jar.getTmpdex().renameTo(jar.getTmpClasses());
		if (jar.getTmpdex2().exists()) {
			Logger.appendLog("[JarWorker][D][" + jar.getAbsoluteName() + "] Found " + jar.getTmpClasses2()
					+ " ! renaming it to " + jar.getTmpClasses2());
			rename = rename && jar.getTmpdex2().renameTo(jar.getTmpClasses2());
		}
		if (jar.getTmpdex3().exists()) {
			Logger.appendLog("[JarWorker][D][" + jar.getAbsoluteName() + "] Found " + jar.getTmpClasses3()
					+ " ! renaming it to " + jar.getTmpClasses3());
			rename = rename && jar.getTmpdex3().renameTo(jar.getTmpClasses3());
		}

		// if(rename) return true;
		if (!rename) {
			Logger.appendLog("[JarWorker][E][" + jar.getAbsoluteName()
					+ "] Found multiple classes and we couldn't rename them !");
			this.logPan.addLog(
					R.getString(S.LOG_ERROR) + "[" + jar.getOrigJar() + "]" + R.getString("log.classes.failed"));
			return false;
		}
		this.progressBar.setValue(this.progressBar.getValue() + 1);
		progressBar.setString(R.getString("progress.jar") + " " + this.getPercent() + "%");
		threadWatcher.updateProgress();

		// phase 5
		Logger.appendLog("[JarWorker][I][" + jar.getAbsoluteName() + "] abotut to put classes file(s) back in jar file");

		ArrayList<File> list = new ArrayList<File>();
		list.add(jar.getTmpClasses());
		if (jar.getTmpClasses2().exists()) {
			list.add(jar.getTmpClasses2());
		}
		if (jar.getTmpClasses3().exists()) {
			list.add(jar.getTmpClasses3());
		}
		String classesFiles = "";
		for (int i = 0; i < list.size(); i++) {
			if (i != list.size() - 1) {
				classesFiles = classesFiles + "" + list.get(i).getAbsolutePath() + " :: ";
			} else {
				classesFiles = classesFiles + "" + list.get(i).getAbsolutePath();
			}
		}
		Logger.appendLog("[JarWorker][I][" + jar.getAbsoluteName() + "] files to be added : " + classesFiles);
		boolean addstatus = false;
		addstatus = Zip.addFilesToExistingZip(jar.getTmpJar(), list);
		if (!addstatus) {
			Logger.appendLog(
					"[JarWorker][E][" + jar.getAbsoluteName() + "] failed to put classes file(s) back in jar file");
			this.logPan.addLog(
					R.getString(S.LOG_ERROR) + "[" + jar.getOrigJar() + "]" + R.getString("log.add.classes.failed"));
			return false;
		}
		this.progressBar.setValue(this.progressBar.getValue() + 1);
		progressBar.setString(R.getString("progress.jar") + " " + this.getPercent() + "%");
		threadWatcher.updateProgress();

		// phase 6
		boolean putBack = false;
		Logger.appendLog("[JarWorker][I][" + jar.getAbsoluteName() + "] about to copy file back to it's original folder");
		putBack = jar.getTmpJar().renameTo(jar.getOrigJar()); // FilesUtils.copyFile(jar.getTmpJar(),
																// jar.getOrigJar());
		if (!putBack) {
			Logger.appendLog("[JarWorker][E][" + jar.getAbsoluteName()
					+ "] about to copy file back to it's original folder failed ");
			this.logPan.addLog(
					R.getString(S.LOG_ERROR) + "[" + jar.getOrigJar() + "]" + R.getString("log.putback.apk.failed"));
			return false;
		}
		Logger.appendLog("[JarWorker][I] cleaning ...");
		FilesUtils.deleteRecursively(jar.getTmpFolder());
		FilesUtils.deleteRecursively(jar.getOdexFile());
		FilesUtils.deleteFiles(FilesUtils.searchExactFileNames(
				new File(SessionCfg.getSystemFolder() + File.separator + S.SYSTEM_FRAMEWORK),
				jar.getOdexFile().getName()));
		return true;
	}

	/**
	 * 
	 * @return the percentage of the current progress
	 */
	private int getPercent() {
		return (this.progressBar.getValue() * 100) / this.progressBar.getMaximum();

	}

	/**
	 * @return the progressBar
	 */
	public WebProgressBar getProgressBar() {
		return progressBar;
	}

	@Override
	public void run() {
		if (this.odexFiles != null && this.odexFiles.size() > 0) {
			for (File jar : odexFiles) {
				JarObj jarObj = new JarObj(jar);
				Logger.appendLog("[JarWorker][I] processing " + new JarObj(jar).getAbsoluteName() + ".jar ...");
				boolean success = deodexJar(jarObj);
				if (success) {
					Logger.appendLog("[JarWorker][I] " + new JarObj(jar).getAbsoluteName() + ".jar [SUCCESS]");
					logPan.addLog(
							R.getString(S.LOG_INFO) + "[" + new JarObj(jar).getAbsoluteName() + ".jar]" + " [SUCCESS]");
				} else {
					jarObj.reverseMove();
					Logger.appendLog("[JarWorker][E] " + new JarObj(jar).getAbsoluteName() + ".jar [FAILED]");
					logPan.addLog(R.getString(S.LOG_WARNING) + "[" + new JarObj(jar).getAbsoluteName() + ".jar]"
							+ " [FAILED]");
				}
				this.progressBar.setValue(this.progressBar.getValue() + 1);
				progressBar.setString(R.getString("progress.jar") + " " + this.getPercent() + "%");
				threadWatcher.updateProgress();
			}
		}

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Logger.appendLog("[JarWorker][EX]" + e.getStackTrace());
		}
		Logger.appendLog("[JarWorker][I] ALL Jobs Done now cleaning ...");
		FilesUtils.deleteRecursively(tmpFolder);
		this.progressBar.setValue(this.progressBar.getMaximum());
		progressBar.setString(R.getString("progress.done"));
		progressBar.setEnabled(false);
		this.threadWatcher.updateProgress();
		this.threadWatcher.done(this);
	}
}
