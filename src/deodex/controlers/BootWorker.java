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

import javax.swing.JProgressBar;

import deodex.R;
import deodex.S;
import deodex.SessionCfg;
import deodex.tools.FilesUtils;
import deodex.tools.Logger;
import deodex.tools.Zip;

public class BootWorker implements Runnable, Watchable {
	ArrayList<File> bootFiles;
	File tmpFolder;
	ThreadWatcher threadWatcher;
	LoggerPan log;
	JProgressBar progressBar;

	public BootWorker(ArrayList<File> bootList, File tmpFolder, LoggerPan log) {
		bootFiles = bootList;
		this.tmpFolder = tmpFolder;
		tmpFolder.mkdirs();
		this.log = log;
		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(bootList.size());
		progressBar.setStringPainted(true);
	}

	@Override
	public void addThreadWatcher(ThreadWatcher watcher) {
		// TODO Auto-generated method stub
		this.threadWatcher = watcher;
	}

	private boolean deoDexBootFile(File file) {
		File tmpClasses = new File(tmpFolder.getAbsolutePath() + File.separator + S.CLASSES);
		File tmpClasses2 = new File(tmpFolder.getAbsolutePath() + File.separator + S.CLASSES_2);
		File tmpClasses3 = new File(tmpFolder.getAbsolutePath() + File.separator + S.CLASSES_3);

		String absoluteName = file.getName().substring(0, file.getName().lastIndexOf("."));

		File tmpJar = new File(tmpFolder.getAbsolutePath() + File.separator + absoluteName + ".jar");
		File origJar = new File(SessionCfg.getSystemFolder().getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK
				+ File.separator + absoluteName + ".jar");
		if (!origJar.exists()) {
			Logger.writLog("[BootWorker][W] matching jar for "+file.getName() +" creating dummy ...");
			FilesUtils.copyFile(S.DUMMY_JAR, origJar);
		}

		boolean copyStatus = false;
		copyStatus = FilesUtils.copyFile(origJar, tmpJar);
		if (!copyStatus) {
			this.log.addLog(
					R.getString(S.LOG_ERROR) + "[" + absoluteName + ".jar]" + R.getString("log.copy.to.tmp.failed"));
			return false;
		}

		copyStatus = FilesUtils.copyFile(file, tmpClasses);
		if (new File(file.getParentFile().getAbsolutePath() + File.separator + absoluteName + S.DEX2_EXT).exists()) {
			copyStatus = FilesUtils.copyFile(
					new File(file.getParentFile().getAbsolutePath() + File.separator + absoluteName + S.DEX2_EXT),
					tmpClasses2);
		}

		if (new File(file.getParentFile().getAbsolutePath() + File.separator + absoluteName + S.DEX3_EXT).exists()) {
			copyStatus = copyStatus && FilesUtils.copyFile(
					new File(file.getParentFile().getAbsolutePath() + File.separator + absoluteName + S.DEX3_EXT),
					tmpClasses3);
		}

		if (!copyStatus) {
			this.log.addLog(
					R.getString(S.LOG_ERROR) + "[" + absoluteName + ".jar]" + R.getString("log.classes.failed"));
			return false;
		}

		boolean addStatus = false;
		ArrayList<File> list = new ArrayList<File>();
		list.add(tmpClasses);
		if (tmpClasses2.exists()) {
			list.add(tmpClasses2);
		}
		if (tmpClasses3.exists()) {
			list.add(tmpClasses3);
		}
		try {
			addStatus = Zip.addFilesToExistingZip(tmpJar, list);
		} catch (IOException e) {
			e.printStackTrace();
			Logger.writLog("[MainWorker][EX]"+e.getStackTrace());
		}

		if (!addStatus) {
			this.log.addLog(
					R.getString(S.LOG_ERROR) + "[" + absoluteName + ".jar]" + R.getString("log.add.classes.failed"));
			return false;
		}
		copyStatus = FilesUtils.copyFile(tmpJar, origJar);

		return copyStatus;

	}

	private String percent (){
		//  ?  >>> value
		// 100 >>>> max 
		return (this.progressBar.getValue() *100 / this.progressBar.getMaximum())+"%";
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (File file : bootFiles) {
			Logger.writLog("[BootWorker][I] processing " +file.getName().substring(0, file.getName().lastIndexOf("."))
						+ ".jar ");
			boolean success = deoDexBootFile(file);
			if (success) {
				log.addLog(R.getString(S.LOG_INFO) + "[" + file.getName().substring(0, file.getName().lastIndexOf("."))
						+ ".jar]" + " [SUCCESS]");
			} else {
				log.addLog(R.getString(S.LOG_WARNING) + "[" + file.getName().substring(0, file.getName().lastIndexOf("."))
						+ ".jar]" + " [FAILED ]");

			}
			progressBar.setValue(progressBar.getValue() + 1);
			progressBar.setString(R.getString("progress.bootFiles") + " "+this.percent());
			threadWatcher.updateProgress();
		}
		FilesUtils.deleteRecursively(tmpFolder);
		progressBar.setValue(progressBar.getMaximum());
		progressBar.setString(R.getString("progress.done"));
		this.threadWatcher.updateProgress();
		this.threadWatcher.done(this);
	}

}
