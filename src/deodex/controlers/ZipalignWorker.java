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

import com.alee.laf.progressbar.WebProgressBar;

import deodex.R;
import deodex.S;
import deodex.tools.Deodexer;
import deodex.tools.Zip;

/**
 * 
 * @author lord-ralf-adolf
 *
 */
public class ZipalignWorker implements Runnable, Watchable {
	ArrayList<ThreadWatcher> watchers = new ArrayList<ThreadWatcher>();
	WebProgressBar bar;
	ArrayList<File> apks;
	LoggerPan log;
	private boolean doZipalign = true;
	private boolean doSign = false;

	/**
	 * 
	 * @param apks
	 *            list of apks to be zipaligned
	 * @param bar
	 *            the progressBar in which we will display the progress
	 * @param log
	 *            the LoggerPan where all the logs will be sent
	 */
	public ZipalignWorker(ArrayList<File> apks, WebProgressBar bar, LoggerPan log) {
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

	/**
	 * @return the doSign
	 */
	public boolean isDoSign() {
		return doSign;
	}

	/**
	 * @return the doZipalign
	 */
	public boolean isDoZipalign() {
		return doZipalign;
	}

	/**
	 * 
	 * @return the percentage of the current progress
	 */
	private String percent() {
		return "" + ((bar.getValue() * 100) / bar.getMaximum()) + "%";
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
					log.addLog(R.getString(S.LOG_INFO)+"[" + apk.getName() + "]" + "[SUCCESS][SIGNED]");
				} else {
					log.addLog(R.getString(S.LOG_ERROR)+"[" + apk.getName() + "]" + "[FAILED TO SIGN]");
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
					log.addLog(R.getString(S.LOG_INFO)+"[" + apk.getName() + "]" + "[SUCCESS][ZIPALIGNED]");
				} else {
					log.addLog(R.getString(S.LOG_ERROR)+"[" + apk.getName() + "]" + "[FAILED TO ZIPALIGN]");
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
	 * @param doSign
	 *            the doSign to set
	 */
	public void setDoSign(boolean doSign) {
		this.doSign = doSign;
	}

	/**
	 * @param doZipalign
	 *            the doZipalign to set
	 */
	public void setDoZipalign(boolean doZipalign) {
		this.doZipalign = doZipalign;
	}

	/**
	 * 
	 * @param apk
	 *            the apkFile to be signed
	 * @return true only if the apk was signed
	 */
	private boolean signApk(File apk) {
		File signedApk = new File(apk.getAbsolutePath() + "_signed.apk");
		boolean signed = false;

		signed = Deodexer.signApk(apk, signedApk);
		boolean delete = apk.delete();
		boolean rename = signedApk.renameTo(apk);

		return signed && delete && rename;
	}

	/**
	 * 
	 * @param apk
	 *            the apkFile to be zipaligned
	 * @return true only if the apk was zipaligned
	 */
	private boolean zipalignApk(File apk) {
		File zipApk = new File(apk.getAbsolutePath() + "_zipaligned.apk");
		boolean success = false;

		success = Zip.zipAlignAPk(apk, zipApk);

		boolean delete = apk.delete();
		boolean rename = zipApk.renameTo(apk);

		return success && delete && rename;

	}

}
