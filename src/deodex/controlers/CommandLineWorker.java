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
import java.util.ArrayList;

import deodex.S;
import deodex.SessionCfg;
import deodex.tools.FailTracker;
import deodex.tools.FilesUtils;
import deodex.tools.Zip;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class CommandLineWorker implements ThreadWatcher {
	boolean createZip = false;
	File systemFolder = SessionCfg.getSystemFolder();
	ArrayList<File> fileToAdd = new ArrayList<File>();

	/**
	 * 
	 * @param createZip
	 *            : true if you want this watcher to create a flashable zip when
	 *            all tasks are done
	 */
	public CommandLineWorker(boolean createZip) {
		this.createZip = createZip;
	}

	/**
	 * this will create the flashable zip
	 */
	private void createFlashableZip() {
		initFilesList();
		S.ZIP_OUTPUT.mkdirs();
		File zipFile = new File(S.ZIP_OUTPUT + File.separator + this.systemFolder + ".zip");
		FilesUtils.copyFile(S.DUMMY_ZIP, zipFile);
		System.out.println("zip file to be created : " + zipFile.getAbsolutePath());
		for (File f : this.fileToAdd) {

			try {
				Zip.AddFileToFolderInZip(systemFolder, f, new ZipFile(zipFile));
			} catch (ZipException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void done(Runnable r) {
		if (createZip) {
			System.out.println("Creating zip File ");
			createFlashableZip();
		}
		System.out.println("ALL Threads Terminated !");
		if(FailTracker.failCount > 0){
			System.out.println("List of files fail to deodex :");
		for (String str : FailTracker.failedFiles)
			System.out.println(str);
		} else {
			System.out.println("All apks and jars were deodexed !");
		}
	}

	/**
	 * list all the files in the system folder that will be added to the
	 * flashable zip
	 */
	private void initFilesList() { // FIXME: Duplicated in FlachableZipCreater
									// the GUI version move it somewhere else
		fileToAdd.addAll(FilesUtils
				.searchrecursively(systemFolder, ".apk"));

		fileToAdd.addAll(FilesUtils.searchrecursively(
				systemFolder, ".jar"));

	}

	@Override
	public void sendFailed(Runnable r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateProgress() {
		// TODO Auto-generated method stub

	}

}
