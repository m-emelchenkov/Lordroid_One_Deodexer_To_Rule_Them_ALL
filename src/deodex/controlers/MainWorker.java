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

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.alee.laf.progressbar.WebProgressBar;

import deodex.R;
import deodex.S;
import deodex.SessionCfg;
import deodex.tools.ArrayUtils;
import deodex.tools.CmdUtils;
import deodex.tools.Deodexer;
import deodex.tools.FailTracker;
import deodex.tools.FilesUtils;
import deodex.tools.Logger;
import deodex.tools.UnsquashUtils;

public class MainWorker implements Runnable, ThreadWatcher, Watchable {
	public final String[] exts = { S.ODEX_EXT, S.COMP_ODEX_EXT, S.COMP_GZ_ODEX_EXT };

	private int workingThreadCount = 4;

	private ArrayList<File> worker1List = new ArrayList<File>();

	private ArrayList<File> worker2List = new ArrayList<File>();

	private ArrayList<File> worker3List = new ArrayList<File>();

	private ArrayList<File> worker4List = new ArrayList<File>();
	private LoggerPan logPan;

	final ThreadWatcher threadWatcher;
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
	WebProgressBar progressBar;
	ArrayList<Runnable> tasks = new ArrayList<Runnable>();

	/**
	 * 
	 * @param folder
	 *            : the system folder of the rom to be deodexed
	 * @param logPane
	 *            : the LoggerPan where all the logs will be sent
	 * @param maxThreads
	 *            : an int with the maximum Threads to use default (2)
	 */
	public MainWorker(File folder, LoggerPan logPane, int maxThreads, ThreadWatcher watcher) {
		this.threadWatcher = watcher;
		maxThreading = maxThreads;
		this.logPan = logPane;
		this.folder = folder;
		FailTracker.putToZero();
		if (SessionCfg.getSdk() > 20) {
			init();
		} else {
			initLegacy();
		}

	}

	@Override
	public void addThreadWatcher(ThreadWatcher watcher) {
		// TODO Auto-generated method stub
		// threadWatcher = watcher;
	}

	@Override
	public void done(Runnable r) {
		if (tasks.size() > 0) {
			new Thread(tasks.get(0)).start();
			tasks.remove(0);
		}
		workingThreadCount--;

		if (workingThreadCount == 0) {
			logPan.saveToFile();
			// don't delete if there us a fail the user needs boot.oat to deodex manually
			if (FailTracker.failCount == 0) {
				FilesUtils.deleteFiles(FilesUtils.searchrecursively(
						new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK),
						S.SYSTEM_FRAMEWORK_BOOT));
				FilesUtils.deleteFiles(FilesUtils.searchrecursively(
						new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK),
						S.SYSTEM_FRAMEWORK_BOOT_ART));

			}
			FilesUtils.deleteUmptyFoldersInFolder(
					new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK));

			FilesUtils.deleteRecursively(S.getBootTmp().getParentFile().getParentFile());
			S.setTempDir(System.getProperty("java.io.tmpdir"));
			// TODO remove this
			Logger.appendLog("[MainWorker][I]" + "ALL JOBS THERMINATED ");
			progressBar.setValue(progressBar.getMaximum());
			progressBar.setString(R.getString("progress.done"));
			progressBar.setEnabled(false);
			updateWatcher();
		}
	}

	/**
	 * will search recursively for odex files that have a matching apk file
	 * under app and priv app folders
	 * 
	 * @return the list of apk's odex files in the systemFolder
	 */
	private ArrayList<File> getapkOdexFiles() {
		File app = new File(this.folder.getAbsolutePath() + File.separator + S.SYSTEM_APP);
		File privApp = new File(this.folder.getAbsolutePath() + File.separator + S.SYSTEM_PRIV_APP);
		File plugin = new File(this.folder.getAbsolutePath() + File.separator + "plugin");
		File vendor = new File(this.folder.getAbsolutePath() + "/" + "vendor");
		File[] folders = { app, privApp, plugin, vendor };
		ArrayList<File> odexFiles = new ArrayList<File>();

		for (File dir : folders) {
			if (dir.exists() && dir.listFiles().length > 0)
				for (String ext : exts) {
					odexFiles.addAll(FilesUtils.searchrecursively(dir, ext));
				}
		}
		return ArrayUtils.deletedupricates(odexFiles);

	}

	/**
	 * 
	 * @return the percentage of the current progress
	 */
	private int getPercent() {
		return (this.progressBar.getValue() * 100) / this.progressBar.getMaximum();
	}

	/**
	 * this one will be called to initialize the worker for roms with sdk > 20
	 */
	private void init() {

		// lets unsquash this bitch !
		if (SessionCfg.isSquash) {
			boolean unsquash = UnsquashUtils.unsquash(folder);
			if (!unsquash) {
				this.logPan.addLog(R.getString(S.LOG_ERROR) + R.getString("0000141"));
				this.threadWatcher.sendFailed(this);
				isinitialized = false;
				return;
			} else {
				new File(folder.getAbsolutePath() + File.separator + "odex.app.sqsh").delete();
				new File(folder.getAbsolutePath() + File.separator + "odex.priv-app.sqsh").delete();
				new File(folder.getAbsolutePath() + File.separator + "odex.framework.sqsh").delete();
			}
		}

		// unsquash first !
		try {
			ArrayList<File> boot = FilesUtils.searchExactFileNames(
					new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK), "boot.oat");
			SessionCfg.setBootOatFile(boot.get(0));
			isinitialized = FilesUtils.copyFile(SessionCfg.getBootOatFile(), S.getBootTmp());
			if (!isinitialized) {
				this.threadWatcher.sendFailed(this);
				this.logPan.addLog(R.getString(S.LOG_ERROR) + R.getString("0000139"));
				return;
			}
		} catch (Exception e) {
			Logger.appendLog("[MainWorker][EX]" + e.getStackTrace());

		}

		isinitialized = isinitialized && Deodexer.oat2dexBoot(S.getBootTmp());
		if (!isinitialized) {
			this.logPan.addLog(R.getString(S.LOG_ERROR) + R.getString("0000140"));
			this.threadWatcher.sendFailed(this);
			return;
		}

		File bootFiles = new File(S.getBootTmpDex().getAbsolutePath());

		// TODO init apklist here
		worker1List = this.getapkOdexFiles();

		int half = worker1List.size() / 2;
		worker2List = new ArrayList<File>();
		for (int i = worker1List.size() - 1; i >= half; i = worker1List.size() - 1) {
			worker2List.add(worker1List.get(i));
			worker1List.remove(i);
		}

		/// framework
		File framework = new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK);
		this.worker3List = new ArrayList<File>();
		for (String ext : exts) {
			this.worker3List.addAll(FilesUtils.searchrecursively(framework, ext));
		}

		// some roms have apks under framwork like LG roms
		ArrayList<File> temapkinfram = new ArrayList<File>();
		for (File f : this.worker3List) {
			ArrayList<File> apksInFram = ArrayUtils.deletedupricates(FilesUtils.searchExactFileNames(
					new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK),
					f.getName().substring(0, f.getName().lastIndexOf(".")) + ".apk"));

			Logger.appendLog("[MainWorker][I]" + "Searching for ");
			if (!apksInFram.isEmpty() && FilesUtils
					.searchExactFileNames(new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK),
							f.getName().substring(0, f.getName().lastIndexOf(".")) + ".jar")
					.isEmpty()) {
				temapkinfram.add(f);
				Logger.appendLog("[MainWorker][I]" + "Found moving it to apk worker's list ");
				// this.worker3List.remove(f);
			} else {
				Logger.appendLog("[MainWorker][I]" + "Not found assuming odex file belongs to a .jar file ...");
			}
		}
		this.worker1List.addAll(temapkinfram);
		this.worker3List.removeAll(temapkinfram);

		apk1 = new ApkWorker(worker1List, logPan, S.getWorker1Folder(), SessionCfg.isSign(), SessionCfg.isZipalign());
		apk2 = new ApkWorker(worker2List, logPan, S.getWorker2Folder(), SessionCfg.isSign(), SessionCfg.isZipalign());
		jar = new JarWorker(worker3List, logPan, S.getWorker3Folder());

		// bootFile
		File[] boots = bootFiles.listFiles();
		worker4List = new ArrayList<File>();
		for (File f : boots) {
			if (!f.getName().endsWith(S.CLASSES_2) && !f.getName().endsWith(S.CLASSES_3)) {
				if (f.getName().endsWith(".dex")) {
					worker4List.add(f);
				}
			}
		}
		boot = new BootWorker(worker4List, S.getWorker4Folder(), this.logPan);

		Logger.appendLog("[MainWorker][I]" + "APK list 1");
		for (File f : this.worker1List) {
			Logger.appendLog("[MainWorker][I]" + f.getAbsolutePath());
		}
		Logger.appendLog("[MainWorker][I]" + "APK list 2");
		for (File f : this.worker2List) {
			Logger.appendLog("[MainWorker][I]" + f.getAbsolutePath());
		}
		Logger.appendLog("[MainWorker][I]" + "Jar list 3");
		for (File f : this.worker3List) {
			Logger.appendLog("[MainWorker][I]" + f.getAbsolutePath());
		}
		Logger.appendLog("[MainWorker][I]" + "boot list 4 (boot)");
		for (File f : this.worker4List) {
			Logger.appendLog("[MainWorker][I]" + f.getAbsolutePath());
		}

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

	/**
	 * this one will be called to initialize the worker for roms with sdk < 21
	 */
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
		S.getBootTmpDex().mkdirs();
		for (File f : framworkList) {
			if (f.getName().endsWith(".odex")) {
				worker3List.add(f);
				FilesUtils.copyFile(f, new File(S.getBootTmpDex().getAbsolutePath() + File.separator + f.getName()));
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
		worker1List = ArrayUtils.deletedupricates(worker1List);
		worker2List = ArrayUtils.deletedupricates(worker2List);
		worker3List = ArrayUtils.deletedupricates(worker3List);
		worker4List = ArrayUtils.deletedupricates(worker4List);
		// initialize workers
		apk1l = new ApkWorkerLegacy(worker1List, logPan, S.getWorker1Folder(), SessionCfg.sign, SessionCfg.zipalign);
		apk2l = new ApkWorkerLegacy(worker2List, logPan, S.getWorker2Folder(), SessionCfg.sign, SessionCfg.zipalign);
		jar1l = new JarWorkerLegacy(worker3List, logPan, S.getWorker3Folder());
		jar2l = new JarWorkerLegacy(worker4List, logPan, S.getWorker4Folder());
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

	/**
	 * the panel containing all the progress bares of this and all the child
	 * threads private because you don't need to call it outside of this this
	 * one is called from init() when rom's sdk is > 20
	 */
	private void initPannel() {
		progressBar = new WebProgressBar();
		progressBar.setFont(R.getCouriernormal());
		progressBar.setStringPainted(true);

		mainPannel.setSize(798, 224);
		mainPannel.setLayout(null);
		mainPannel.setBackground(R.PANELS_BACK_COLOR);
		apk1.getProgressBar().setBounds(10, 5, 780, 40);
		apk1.getProgressBar().setFont(R.getCouriernormal());
		apk1.getProgressBar().setBackground(Color.white);

		apk1.getProgressBar().setBgBottom(new Color(236, 240, 241));
		apk1.getProgressBar().setBgTop(new Color(189, 195, 199));
		apk1.getProgressBar().setProgressTopColor(new Color(46, 204, 113));
		apk1.getProgressBar().setProgressBottomColor(new Color(39, 174, 96));
		// apk1.getProgressBar().setProgressTopColor(););
		// apk1.getProgressBar().setForeground(Color.BLACK);

		apk2.getProgressBar().setBounds(10, 49, 780, 40);
		apk2.getProgressBar().setFont(R.getCouriernormal());
		apk2.getProgressBar().setBackground(Color.white);
		// apk2.getProgressBar().setForeground(Color.BLACK);
		apk2.getProgressBar().setBgBottom(new Color(236, 240, 241));
		apk2.getProgressBar().setBgTop(new Color(189, 195, 199));
		apk2.getProgressBar().setProgressTopColor(new Color(46, 204, 113));
		apk2.getProgressBar().setProgressBottomColor(new Color(39, 174, 96));
		jar.getProgressBar().setBounds(10, 93, 780, 40);
		jar.getProgressBar().setFont(R.getCouriernormal());
		jar.getProgressBar().setBackground(Color.white);
		// jar.getProgressBar().setForeground(new Color(0, 183, 92));
		jar.getProgressBar().setBgBottom(new Color(236, 240, 241));
		jar.getProgressBar().setBgTop(new Color(189, 195, 199));
		jar.getProgressBar().setProgressTopColor(new Color(46, 204, 113));
		jar.getProgressBar().setProgressBottomColor(new Color(39, 174, 96));

		boot.progressBar.setBounds(10, 137, 780, 40);
		boot.progressBar.setFont(R.getCouriernormal());
		boot.progressBar.setBackground(Color.white);
		// boot.progressBar.setForeground(new Color(0, 183, 92));
		boot.progressBar.setBgBottom(new Color(236, 240, 241));
		boot.progressBar.setBgTop(new Color(189, 195, 199));
		boot.progressBar.setProgressTopColor(new Color(46, 204, 113));
		boot.progressBar.setProgressBottomColor(new Color(39, 174, 96));

		progressBar.setBounds(10, 181, 780, 40);
		progressBar.setMinimum(0);
		progressBar.setMaximum(apk1.getProgressBar().getMaximum() + apk2.getProgressBar().getMaximum()
				+ jar.getProgressBar().getMaximum() + boot.progressBar.getMaximum());
		// progressBar.setForeground(new Color(175, 122, 197));
		progressBar.setBackground(Color.WHITE);

		progressBar.setBgBottom(new Color(236, 240, 241));
		progressBar.setBgTop(new Color(189, 195, 199));
		progressBar.setProgressTopColor(new Color(155, 89, 182));
		progressBar.setProgressBottomColor(new Color(142, 68, 173));

		mainPannel.add(apk1.getProgressBar());
		mainPannel.add(apk2.getProgressBar());
		mainPannel.add(jar.getProgressBar());
		mainPannel.add(boot.progressBar);

		mainPannel.add(progressBar);
	}

	/**
	 * the panel containing all the progress bares of this and all the child
	 * threads private because you don't need to call it outside of this this
	 * one is called from initLegacy() when rom's sdk is <= 20
	 */
	public void initPannelLegacy() {
		progressBar = new WebProgressBar();
		progressBar.setFont(R.getCouriernormal());
		progressBar.setStringPainted(true);

		mainPannel.setSize(798, 224);
		mainPannel.setLayout(null);
		mainPannel.setBackground(R.PANELS_BACK_COLOR);
		apk1l.getProgressBar().setBounds(10, 5, 780, 40);
		apk1l.getProgressBar().setFont(R.getCouriernormal());
		apk1l.getProgressBar().setBackground(Color.white);
		// apk1l.getProgressBar().setForeground(new Color(0, 183, 92));
		apk1l.getProgressBar().setBgBottom(new Color(236, 240, 241));
		apk1l.getProgressBar().setBgTop(new Color(189, 195, 199));
		apk1l.getProgressBar().setProgressTopColor(new Color(46, 204, 113));
		apk1l.getProgressBar().setProgressBottomColor(new Color(39, 174, 96));

		apk2l.getProgressBar().setBounds(10, 49, 780, 40);
		apk2l.getProgressBar().setFont(R.getCouriernormal());
		apk2l.getProgressBar().setBackground(Color.white);
		// apk2l.getProgressBar().setForeground(new Color(0, 183, 92));
		apk2l.getProgressBar().setBgBottom(new Color(236, 240, 241));
		apk2l.getProgressBar().setBgTop(new Color(189, 195, 199));
		apk2l.getProgressBar().setProgressTopColor(new Color(46, 204, 113));
		apk2l.getProgressBar().setProgressBottomColor(new Color(39, 174, 96));

		jar1l.getProgressBar().setBounds(10, 93, 780, 40);
		jar1l.getProgressBar().setFont(R.getCouriernormal());
		jar1l.getProgressBar().setBackground(Color.white);
		// jar1l.getProgressBar().setForeground(new Color(0, 183, 92));
		jar1l.getProgressBar().setBgBottom(new Color(236, 240, 241));
		jar1l.getProgressBar().setBgTop(new Color(189, 195, 199));
		jar1l.getProgressBar().setProgressTopColor(new Color(46, 204, 113));
		jar1l.getProgressBar().setProgressBottomColor(new Color(39, 174, 96));

		jar2l.progressBar.setBounds(10, 137, 780, 40);
		jar2l.progressBar.setFont(R.getCouriernormal());
		jar2l.progressBar.setBackground(Color.white);
		// jar2l.progressBar.setForeground(new Color(0, 183, 92));
		jar2l.getProgressBar().setBgBottom(new Color(236, 240, 241));
		jar2l.getProgressBar().setBgTop(new Color(189, 195, 199));
		jar2l.getProgressBar().setProgressTopColor(new Color(46, 204, 113));
		jar2l.getProgressBar().setProgressBottomColor(new Color(39, 174, 96));

		progressBar.setBounds(10, 181, 780, 40);
		progressBar.setMinimum(0);
		progressBar.setMaximum(
				this.worker1List.size() + this.worker2List.size() + this.worker3List.size() + this.worker4List.size());
		// progressBar.setForeground(new Color(175, 122, 197));
		progressBar.setBackground(Color.WHITE);
		progressBar.setBgBottom(new Color(236, 240, 241));
		progressBar.setBgTop(new Color(189, 195, 199));
		progressBar.setProgressTopColor(new Color(155, 89, 182));
		progressBar.setProgressBottomColor(new Color(142, 68, 173));
		mainPannel.add(apk1l.getProgressBar());
		mainPannel.add(apk2l.getProgressBar());
		mainPannel.add(jar1l.getProgressBar());
		mainPannel.add(jar2l.progressBar);

		mainPannel.add(progressBar);
	}

	/**
	 * will log all external tools versions to the main log file
	 */
	private void logToolsversions() {

		String[] oat2dex = { "java", "-jar", S.getAot2Dex(), "-v" };
		String[] smali = { "java", "-jar", S.getSmali(), "-v" };
		String[] backsmali = { "java", "-jar", S.getBaksmali(), "-v" };
		String[] zipalign = { new File(S.getZipalign()).getAbsolutePath(), "-v" };

		CmdUtils.runCommand(oat2dex);
		CmdUtils.runCommand(smali);
		CmdUtils.runCommand(backsmali);
		CmdUtils.runCommand(zipalign);

	}

	@Override
	public void run() {
		logToolsversions();
		if (this.isinitialized) {
			this.threadWatcher.updateProgress();
			for (int i = 0; i < this.maxThreading; i++) {
				if (tasks.size() > 0) {
					new Thread(tasks.get(0)).start();
					tasks.remove(0);
				}
			}
		} else {
			FilesUtils.deleteRecursively(S.getWorker1Folder().getParentFile());
			S.setTempDir(System.getProperty("java.io.tmpdir"));
			this.threadWatcher.sendFailed(this);
			return;
		}

	}

	@Override
	public void sendFailed(Runnable r) {
		// TODO Auto-generated method stub

	}

	private synchronized void setProgress() {
		if (SessionCfg.getSdk() > 20) {
			progressBar.setValue(apk1.getProgressBar().getValue() + apk2.getProgressBar().getValue()
					+ boot.progressBar.getValue() + jar.progressBar.getValue());
			progressBar.setString(R.getString("overal.progress") + "(" + this.getPercent() + "%)");
		} else {
			progressBar.setValue(apk1l.getProgressBar().getValue() + apk2l.getProgressBar().getValue()
					+ jar1l.getProgressBar().getValue() + jar2l.getProgressBar().getValue());
			progressBar.setString(R.getString("overal.progress") + "(" + this.getPercent() + "%)");
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
