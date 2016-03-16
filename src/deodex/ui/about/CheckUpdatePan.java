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
package deodex.ui.about;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.compress.archivers.ArchiveException;

import com.alee.laf.progressbar.WebProgressBar;

import deodex.R;
import deodex.S;
import deodex.controlers.LoggerPan;
import deodex.tools.AdbUtils;
import deodex.tools.DesktopUtils;
import deodex.tools.FilesUtils;
import deodex.tools.PathUtils;
import deodex.tools.PropReader;
import deodex.tools.TarGzUtils;
import deodex.ui.Alerts;

public class CheckUpdatePan extends JPanel implements ActionListener {
	public URL updateInfo;
	private URL downloadLink;
	private URL downloadPage;
	public boolean didUpdate = false;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JLabel updateText = new JLabel(R.getString("0000074"));
	JButton checkForUpdate = new JButton(R.getString("0000075"));
	JButton openDownloadpage = new JButton(R.getString("0000076"));
	JButton downloadNewVersion = new JButton(R.getString("0000077"));
	final JDialog container;
	WebProgressBar progress = new WebProgressBar();

	public CheckUpdatePan(JDialog d) {
		container = d;
		this.setBackground(R.PANELS_BACK_COLOR);
		this.setSize(700, 300);
		this.setLayout(null);
		checkForUpdate.addActionListener(this);
		openDownloadpage.addActionListener(this);
		downloadNewVersion.addActionListener(this);
		this.updateText.setText(R.getString("0000074"));
		initMainView();
	}

	private void initMainView() {
		this.removeAll();
		this.setLayout(null);

		this.updateText.setFont(R.SMALL_TITLE_FONT);
		this.updateText.setBounds(10, 10, 620, 60);
		this.updateText.setHorizontalAlignment(JLabel.CENTER);

		this.checkForUpdate.setFont(R.getSmallFont());
		this.checkForUpdate.setBounds(10, 150, 200, 40);

		this.openDownloadpage.setFont(R.getSmallFont());
		this.openDownloadpage.setBounds(220, 150, 200, 40);
		this.openDownloadpage.setEnabled(false);

		this.downloadNewVersion.setFont(R.getSmallFont());
		this.downloadNewVersion.setBounds(430, 150, 200, 40);
		this.downloadNewVersion.setEnabled(false);

		progress.setBounds(10, 80, 620, 40);
		// adding
		this.add(updateText);
		this.add(checkForUpdate);
		this.add(openDownloadpage);
		this.add(downloadNewVersion);
		// this.add(progress);
		// getThis().add(progress);
		// progress.setIndeterminate(true);

		this.revalidate();
		this.repaint();
	}

	public CheckUpdatePan getThis() {
		return this;
	}

	public void disableAllButtons() {
		this.checkForUpdate.setEnabled(false);
		this.downloadNewVersion.setEnabled(false);
		this.openDownloadpage.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(checkForUpdate)) {
			new UpdateFetcher().start();
			// getThis().remove(progress);

		} else if (e.getSource().equals(openDownloadpage)) {
			DesktopUtils.openWebpage(downloadPage);
		} else if (e.getSource().equals(this.downloadNewVersion)) {
			new DownloadnewVersion().start();
		}
	}

	class DownloadnewVersion implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			progress = new WebProgressBar();
			progress.setBounds(10, 80, 620, 40);

			getThis().add(progress);
			progress.setStringPainted(true);
			progress.setIndeterminate(false);
			getThis().disableAllButtons();
			File tar = new File(PathUtils.getExcutionPath() + File.separator + "/updates/latest.tar");
			try {
				URLConnection connection = downloadLink.openConnection();
				progress.setMinimum(0);
				long lengh = connection.getContentLengthLong();
				double kolengh = lengh;
				progress.setMaximum((int) kolengh);
				// progress.setMaximum(connection.getContentLength());
				InputStream in = connection.getInputStream();
				new File(PathUtils.getExcutionPath() + File.separator + "/updates").mkdirs();
				OutputStream out = new FileOutputStream(
						new File(PathUtils.getExcutionPath() + File.separator + "/updates/latest.tar.gz"));
				byte[] buffer = new byte[32768];
				int len;
				long startTime = System.currentTimeMillis();
				while ((len = in.read(buffer)) != -1) {
					updateText.setText(R.getString("0000078"));
					out.write(buffer, 0, len);
					progress.setValue(
							(int) new File(PathUtils.getExcutionPath() + File.separator + "/updates/latest.tar.gz")
									.length());
					progress.repaint();
					getThis().repaint();
					long nowTime = System.currentTimeMillis();
					updateText
							.setText(
									R.getString("0000079")
											+ (int) ((double) (progress.getValue()
													/ (double) ((double) (nowTime - startTime) / 1000)) / 1024)
									+ "kb/s");

				}
				in.close();
				out.close();
				progress.setValue(progress.getMaximum());
				getThis().initMainView();
				getThis().disableAllButtons();
				progress = new WebProgressBar();
				progress.setBounds(10, 80, 620, 40);

				getThis().add(progress);
				progress.setStringPainted(true);
				progress.setIndeterminate(true);
				progress.setString(R.getString("0000080"));
				TarGzUtils.unGzip(new File(PathUtils.getExcutionPath() + File.separator + "/updates/latest.tar.gz"),
						tar.getParentFile());
				progress.setString(R.getString("0000081"));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				getThis().initMainView();
				updateText.setText(R.getString("0000082"));
				getThis().didUpdate = false;
				return;
			}
			File us = new File(PathUtils.getExcutionPath());
			AdbUtils.killServer();
			List<File> list;
			try {
				list = TarGzUtils.unTar(tar, us);
				if (list != null && list.size() > 0) {
					for (File f : list) {
						progress.setString(R.getString("0000083") + f.getName() + " ...");
						Thread.sleep(10);
					}
					progress.setString(R.getString("0000084"));

				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				updateText.setText(R.getString("0000085"));
				getThis().initMainView();
				getThis().disableAllButtons();
				e1.printStackTrace();
				return;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				updateText.setText(R.getString("0000085"));
				getThis().initMainView();
				getThis().disableAllButtons();
				e1.printStackTrace();
				return;
			} catch (ArchiveException e1) {
				// TODO Auto-generated catch block
				updateText.setText(R.getString("0000085"));
				getThis().initMainView();
				getThis().disableAllButtons();
				e1.printStackTrace();
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				getThis().initMainView();
				getThis().disableAllButtons();
				updateText.setText(R.getString("0000085"));
				e.printStackTrace();
				return;
			}

			getThis().initMainView();
			getThis().disableAllButtons();
			updateText.setText(R.getString("0000086"));
			getThis().didUpdate = true;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getThis().container.dispose();
			Alerts.showClosingdialog(getThis().container);
		}

		public void start() {
			new Thread(this).start();
		}
	}

	class UpdateFetcher implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			File remoteVersionFile = new File(
					System.getProperty("java.io.tmpdir") + File.separator + "remoteVersion.txt");
			File localeVersionFile = new File(
					System.getProperty("java.io.tmpdir") + File.separator + "locale version.txt");
			try {
				updateInfo = new URL("http://goo.gl/RElDjT");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				updateText.setText(R.getString("0000087"));
				return;
			}
			getThis().add(progress);
			progress.setIndeterminate(true);
			progress.setStringPainted(true);
			progress.setString(R.getString("0000088"));
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			progress.setString(R.getString("0000089"));
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				URLConnection connection = updateInfo.openConnection();
				InputStream is = connection.getInputStream();
				FilesUtils.copyFile(is, remoteVersionFile);
				progress.setString(R.getString("0000090"));
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				is = Thread.currentThread().getContextClassLoader().getResourceAsStream("version.txt");
				FilesUtils.copyFile(is, localeVersionFile);
				progress.setString(R.getString("0000091"));
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int localVersion = Integer.parseInt(PropReader.getProp("version.incrimental", localeVersionFile));
				int remoteVersion = Integer.parseInt(PropReader.getProp("version.incrimental", remoteVersionFile));
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (localVersion < remoteVersion) {
					progress.setString(R.getString("0000084"));
					getThis().initMainView();
					getThis().updateText
							.setText(R.getString("0000092") + PropReader.getProp("version.name", remoteVersionFile));
					getThis().checkForUpdate.setEnabled(false);
					getThis().downloadNewVersion.setEnabled(true);
					getThis().openDownloadpage.setEnabled(true);
					getThis().downloadLink = new URL(PropReader.getProp("download.link", remoteVersionFile));
					getThis().downloadPage = new URL(PropReader.getProp("download.page", remoteVersionFile));
				} else {
					progress.setString("done ! ");
					getThis().updateText.setText(R.getString("0000093"));
					getThis().updateText.setForeground(Color.BLUE);
					getThis().initMainView();
					disableAllButtons();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				updateText.setText(R.getString("0000087"));
				return;
			}

		}

		public void start() {
			new Thread(this).start();
		}
	}

	public static void CheckForUpdate(LoggerPan logger) {
		File remoteVersionFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "remoteVersion.txt");
		File localeVersionFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "locale version.txt");
		URL updateInfo;
		try {
			updateInfo = new URL("http://goo.gl/RElDjT");
			URLConnection connection = updateInfo.openConnection();
			InputStream is = connection.getInputStream();
			FilesUtils.copyFile(Thread.currentThread().getContextClassLoader().getResourceAsStream("version.txt"),
					localeVersionFile);
			FilesUtils.copyFile(is, remoteVersionFile);
			int localVersion = Integer.parseInt(PropReader.getProp("version.incrimental", localeVersionFile));
			int remoteVersion = Integer.parseInt(PropReader.getProp("version.incrimental", remoteVersionFile));
			if (localVersion < remoteVersion) {
				logger.addLog(R.getString(S.LOG_INFO) + "[" + R.getString("0000094") + "]");
			} else {
				logger.addLog(R.getString(S.LOG_INFO) + "[" + R.getString("0000096") + "]");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.addLog(R.getString(S.LOG_INFO) + "[" + R.getString("0000087") + "]");
			return;
		}

	}
}
