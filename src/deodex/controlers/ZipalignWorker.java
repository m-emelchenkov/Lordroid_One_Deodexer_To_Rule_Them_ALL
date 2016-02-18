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

import deodex.tools.Deodexer;
import deodex.tools.Zip;

public class ZipalignWorker implements Runnable, Watchable {
	ArrayList<ThreadWatcher> watchers = new ArrayList<ThreadWatcher>();
	JProgressBar bar;
	ArrayList<File> apks;
	LoggerPan log;
	private boolean doZipalign = true;
	private boolean doSign = false;

	public ZipalignWorker(ArrayList<File> apks, JProgressBar bar, LoggerPan log) {
		this.bar = bar;
		this.apks = apks;
		this.log = log;
		this.bar.setMinimum(0);



		this.bar.setStringPainted(true);
	}

	@Override
	public void addThreadWatcher(ThreadWatcher watcher) {
		// TODO Auto-generated method stub

		watchers.add(watcher);
	}

	private String percent() {
		// max ====> 100%
		// value ====> ?
		// ? = (value*100)/max
		return "" + ((bar.getValue() * 100) / bar.getMaximum()) + "%";
	}

	private boolean zipalignApk(File apk) {
		File zipApk = new File(apk.getAbsolutePath() + "_zipaligned.apk");
		boolean success = false;
		try {
			success = Zip.zipAlignAPk(apk, zipApk);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		boolean delete = apk.delete();
		boolean rename = zipApk.renameTo(apk);

		return success && delete && rename;

	}

	private boolean signApk(File apk) {
		File signedApk = new File(apk.getAbsolutePath() + "_signed.apk");
		boolean signed = false;
		try {
			signed = Deodexer.signApk(apk, signedApk);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean delete = apk.delete();
		boolean rename = signedApk.renameTo(apk);

		return signed && delete && rename;

	}

	@Override
	public void run() {
		this.bar.setMaximum((doSign && doZipalign) ? apks.size() * 2 : apks.size());
		for (ThreadWatcher w : watchers) {
			w.updateProgress();
		}
		// TODO Auto-generated method stub
		for (File apk : apks) {

			if (this.doSign) {
				boolean success = signApk(apk);

				if (success) {
					log.addLog("[INFO][" + apk.getName() + "]" + "[SIGNED]");
				} else {
					log.addLog("[INFO][" + apk.getName() + "]" + "[FAILED TO SIGN]");
				}
				bar.setValue(bar.getValue() + 1);
				bar.setString("Zipaligning apks " + this.percent());
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (this.doZipalign) {

				boolean success = zipalignApk(apk);
				if (success) {
					log.addLog("[INFO][" + apk.getName() + "]" + "[ZIPALIGNED]");
				} else {
					log.addLog("[INFO][" + apk.getName() + "]" + "[FAILED TO ZIPALIGN]");
				}
				bar.setValue(bar.getValue() + 1);
				bar.setString("Zipaligning apks " + this.percent());
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		bar.setValue(bar.getMaximum());
		bar.setString("Done");
		for (ThreadWatcher w : watchers) {
			w.done(this);
		}

	}

	/**
	 * @return the doZipalign
	 */
	public boolean isDoZipalign() {
		return doZipalign;
	}

	/**
	 * @param doZipalign
	 *            the doZipalign to set
	 */
	public void setDoZipalign(boolean doZipalign) {
		this.doZipalign = doZipalign;
	}

	/**
	 * @return the doSign
	 */
	public boolean isDoSign() {
		return doSign;
	}

	/**
	 * @param doSign
	 *            the doSign to set
	 */
	public void setDoSign(boolean doSign) {
		this.doSign = doSign;
	}

}
