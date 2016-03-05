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
import javax.swing.JProgressBar;

import deodex.Cfg;
import deodex.R;
import deodex.S;
import deodex.SessionCfg;
import deodex.tools.ArrayUtils;
import deodex.tools.CmdUtils;
import deodex.tools.Deodexer;
import deodex.tools.FilesUtils;
import deodex.tools.Logger;
import deodex.tools.UnsquashUtils;

public class MainWorker implements Runnable, ThreadWatcher, Watchable {

	private int workingThreadCount = 4;

	private ArrayList<File> worker1List = new ArrayList<File>();

	private ArrayList<File> worker2List = new ArrayList<File>();

	private ArrayList<File> worker3List = new ArrayList<File>();

	private ArrayList<File> worker4List = new ArrayList<File>();
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
		maxThreading = maxThreads;
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

		if (workingThreadCount == 0) {
			logPan.saveToFile();

			FilesUtils.deleteFiles(FilesUtils.searchrecursively(
					new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK), S.SYSTEM_FRAMEWORK_BOOT));
			FilesUtils.deleteFiles(FilesUtils.searchrecursively(
					new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK),
					S.SYSTEM_FRAMEWORK_BOOT_ART));

			FilesUtils.deleteUmptyFoldersInFolder(
					new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK));

			FilesUtils.deleteRecursively(S.bootTmp.getParentFile().getParentFile());
			// TODO remove this
			Logger.writLog("[MainWorker][I]"+"ALL JOBS THERMINATED ");
			// logPan.addLog(R.getString(S.LOG_INFO)+R.getString("mainWorker.alljobsDone"));
			// logPan.addLog(R.getString(S.LOG_INFO)+R.getString("mainworker.finallog"));
			progressBar.setValue(progressBar.getMaximum());
			progressBar.setString(R.getString("progress.done"));
			updateWatcher();
		}
	}

	private ArrayList<File> getapkOdexFiles() {
		ArrayList<File> global = new ArrayList<File>();
		ArrayList<File> list1 = null;
		ArrayList<File> list2 = null;
		// system/app odex
		if (new File(this.folder.getAbsolutePath() + File.separator + S.SYSTEM_APP).exists()) {
			list1 = FilesUtils.searchrecursively(
					new File(this.folder.getAbsolutePath() + File.separator + S.SYSTEM_APP), S.ODEX_EXT);
			list2 = FilesUtils.searchrecursively(
					new File(this.folder.getAbsolutePath() + File.separator + S.SYSTEM_APP), S.COMP_ODEX_EXT);
		}

		if (list1 != null && list1.size() > 0) {
			list1 = ArrayUtils.deletedupricates(list1);
			for (File f : list1) {
				global.add(f);
			}
		}
		if (list2 != null && list2.size() > 0) {
			list2 = ArrayUtils.deletedupricates(list2);
			for (File f : list2)
				global.add(f);
		}

		// system/priv-app odex
		if (new File(this.folder.getAbsolutePath() + File.separator + S.SYSTEM_PRIV_APP).exists()) {
			list1 = FilesUtils.searchrecursively(
					new File(this.folder.getAbsolutePath() + File.separator + S.SYSTEM_PRIV_APP), S.ODEX_EXT);
			list2 = FilesUtils.searchrecursively(
					new File(this.folder.getAbsolutePath() + File.separator + S.SYSTEM_PRIV_APP), S.COMP_ODEX_EXT);

		}

		if (list1 != null && list1.size() > 0) {
			list1 = ArrayUtils.deletedupricates(list1);
			for (File f : list1) {
				global.add(f);
			}
		}

		if (list2 != null && list2.size() > 0) {
			list2 = ArrayUtils.deletedupricates(list2);
			for (File f : list2)
				global.add(f);
		}
		return ArrayUtils.deletedupricates(global);
	}

	private int getPercent() {
		// max ===> 100
		// value ===> ?
		// ? = value*100/max;
		return (this.progressBar.getValue() * 100) / this.progressBar.getMaximum();

	}

	private void init() {

		// XXX: you may wanna rethink this the boot.oat can be somewhere else in
		// the future
		// may be searching for it recursively in all framework folder is better
		// ?
		// yes more code but it will be more compatible
		try {
			isinitialized = FilesUtils.copyFile(SessionCfg.getBootOatFile(), S.bootTmp);
			if(!isinitialized)
				this.logPan.addLog(R.getString(S.LOG_ERROR)+"couldn't copy boot.oat to working folder aborting ...");
		} catch (Exception e) {
			Logger.writLog("[MainWorker][EX]"+e.getStackTrace());
		}
		isinitialized = isinitialized && Deodexer.oat2dexBoot(S.bootTmp);
		if (!isinitialized) {
			this.logPan.addLog(R.getString(S.LOG_ERROR)+"couldn't deodex boot.oat aborting ...");
			return;
		}
		// lets unsquash this bitch !
		if(SessionCfg.isSquash){
			boolean unsquash =UnsquashUtils.unsquash(folder);
			if(!unsquash)
				isinitialized = false;
			else{
				new File(folder.getAbsolutePath()+File.separator+"odex.app.sqsh").delete();
				new File(folder.getAbsolutePath()+File.separator+"odex.priv-app.sqsh").delete();
			}
		}
		
		File bootFiles = new File(S.bootTmpDex.getAbsolutePath());

		// TODO init apklist here
		worker1List = this.getapkOdexFiles();

		int half = worker1List.size() / 2;
		worker2List = new ArrayList<File>();
		for (int i = worker1List.size() - 1; i >= half; i = worker1List.size() - 1) {
			worker2List.add(worker1List.get(i));
			worker1List.remove(i);
		}

		/// framework
		this.worker3List = FilesUtils.searchrecursively(
				new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK), S.ODEX_EXT);
		if (this.worker3List != null && this.worker3List.size() > 0)
			this.worker3List = ArrayUtils.deletedupricates(worker3List);

		ArrayList<File> tmpList = FilesUtils.searchrecursively(
				new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK), S.COMP_ODEX_EXT);
		if (tmpList != null && tmpList.size() > 0) {
			tmpList = ArrayUtils.deletedupricates(tmpList);
			for (File f : tmpList) {
				this.worker3List.add(f);
			}
		}

		// some roms have apks under framwork like LG roms
		ArrayList<File> temapkinfram = new ArrayList<File>();
		for (File f : this.worker3List) {
			ArrayList<File> apksInFram = ArrayUtils
					.deletedupricates(
							FilesUtils.searchExactFileNames(
									new File(folder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK),
									(f.getName().endsWith(".odex")
											? f.getName().substring(0, f.getName().lastIndexOf("."))
											: f.getName().substring(0, f.getName().lastIndexOf(".odex.xz"))) + ".apk"));
			Logger.writLog("[MainWorker][I]"+"Searching for "
					+ (f.getName().endsWith(".odex") ? f.getName().substring(0, f.getName().lastIndexOf("."))
							: f.getName().substring(0, f.getName().lastIndexOf(".odex.xz")))
					+ ".apk");
			if (!apksInFram.isEmpty()) {
				temapkinfram.add(f);
				Logger.writLog("[MainWorker][I]"+"fount moving it to apk worker's list ");
			} else {
				Logger.writLog("[MainWorker][I]"+"not found skip ...");
			}
		}
		for (File f : temapkinfram) {
			this.worker1List.add(f);
			this.worker3List.remove(f);
		}

		apk1 = new ApkWorker(worker1List, logPan, S.worker1Folder, SessionCfg.isSign(), SessionCfg.isZipalign());
		apk2 = new ApkWorker(worker2List, logPan, S.worker2Folder, SessionCfg.isSign(), SessionCfg.isZipalign());
		jar = new JarWorker(worker3List, logPan, S.worker3Folder);

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
		boot = new BootWorker(worker4List, S.worker4Folder, this.logPan);

		Logger.writLog("[MainWorker][I]"+"APK list 1");
		for (File f : this.worker1List) {
			Logger.writLog("[MainWorker][I]"+f.getAbsolutePath());
		}
		Logger.writLog("[MainWorker][I]"+"APK list 2");
		for (File f : this.worker2List) {
			Logger.writLog("[MainWorker][I]"+f.getAbsolutePath());
		}
		Logger.writLog("[MainWorker][I]"+"Jar list 3");
		for (File f : this.worker3List) {
			Logger.writLog("[MainWorker][I]"+f.getAbsolutePath());
		}
		Logger.writLog("[MainWorker][I]"+"boot list 4 (boot)");
		for (File f : this.worker4List) {
			Logger.writLog("[MainWorker][I]"+f.getAbsolutePath());
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
		S.bootTmpDex.mkdirs();
		for (File f : framworkList) {
			if (f.getName().endsWith(".odex")) {
				worker3List.add(f);
				FilesUtils.copyFile(f, new File(S.bootTmpDex.getAbsolutePath() + File.separator + f.getName()));
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

		mainPannel.setSize(798, 224);
		mainPannel.setLayout(null);
		mainPannel.setBackground(new Color(206, 194, 229));
		apk1.getProgressBar().setBounds(10, 5, 780, 40);
		apk1.getProgressBar().setFont(R.COURIER_NORMAL);
		apk1.getProgressBar().setBackground(Color.white);
		apk1.getProgressBar().setForeground(new Color(0, 183, 92));

		apk2.getProgressBar().setBounds(10, 49, 780, 40);
		apk2.getProgressBar().setFont(R.COURIER_NORMAL);
		apk2.getProgressBar().setBackground(Color.white);
		apk2.getProgressBar().setForeground(new Color(0, 183, 92));

		jar.getProgressBar().setBounds(10, 93, 780, 40);
		jar.getProgressBar().setFont(R.COURIER_NORMAL);
		jar.getProgressBar().setBackground(Color.white);
		jar.getProgressBar().setForeground(new Color(0, 183, 92));

		boot.progressBar.setBounds(10, 137, 780, 40);
		boot.progressBar.setFont(R.COURIER_NORMAL);
		boot.progressBar.setBackground(Color.white);
		boot.progressBar.setForeground(new Color(0, 183, 92));

		progressBar.setBounds(10, 181, 780, 40);
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

		mainPannel.setSize(798, 224);
		mainPannel.setLayout(null);
		mainPannel.setBackground(new Color(206, 194, 229));
		apk1l.getProgressBar().setBounds(10, 5, 780, 40);
		apk1l.getProgressBar().setFont(R.COURIER_NORMAL);
		apk1l.getProgressBar().setBackground(Color.white);
		apk1l.getProgressBar().setForeground(new Color(0, 183, 92));

		apk2l.getProgressBar().setBounds(10, 49, 780, 40);
		apk2l.getProgressBar().setFont(R.COURIER_NORMAL);
		apk2l.getProgressBar().setBackground(Color.white);
		apk2l.getProgressBar().setForeground(new Color(0, 183, 92));

		jar1l.getProgressBar().setBounds(10, 93, 780, 40);
		jar1l.getProgressBar().setFont(R.COURIER_NORMAL);
		jar1l.getProgressBar().setBackground(Color.white);
		jar1l.getProgressBar().setForeground(new Color(0, 183, 92));

		jar2l.progressBar.setBounds(10, 137, 780, 40);
		jar2l.progressBar.setFont(R.COURIER_NORMAL);
		jar2l.progressBar.setBackground(Color.white);
		jar2l.progressBar.setForeground(new Color(0, 183, 92));

		progressBar.setBounds(10, 181, 780, 40);
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

	private void logToolsversions(){
		String[] oat2dex = {"java","-jar",new File(S.OAT2DEX_JAR).getAbsolutePath(),"-v"};
		String[] smali = {"java","-jar",new File(S.SMALI_JAR).getAbsolutePath(),"-v"};
		String[] backsmali = {"java","-jar",new File(S.BACKSMALI_JAR).getAbsolutePath(),"-v"};
		String[] zupalign = {new File(S.ZIPALIGN_BIN + File.separator + Cfg.getOs()).getAbsolutePath(),"-v"};

		CmdUtils.runCommand(oat2dex);
		CmdUtils.runCommand(smali);
		CmdUtils.runCommand(backsmali);
		CmdUtils.runCommand(zupalign);
		
	}
	@Override
	public void run() {
		logToolsversions();		
		if(this.isinitialized)
			this.threadWatcher.updateProgress();
		else
			this.threadWatcher.sendFailed(this);
		for (int i = 0; i < this.maxThreading; i++) {
			if (tasks.size() > 0) {
				new Thread(tasks.get(0)).start();
				tasks.remove(0);
			}
		}
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

	@Override
	public void sendFailed(Runnable r) {
		// TODO Auto-generated method stub
		
	}
}
