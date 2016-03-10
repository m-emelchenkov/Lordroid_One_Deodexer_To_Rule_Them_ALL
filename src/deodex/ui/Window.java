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
package deodex.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.alee.laf.progressbar.WebProgressBar;

import deodex.Cfg;
import deodex.R;
import deodex.S;
import deodex.SessionCfg;
import deodex.controlers.FlashableZipCreater;
import deodex.controlers.MainWorker;
import deodex.controlers.ThreadWatcher;
import deodex.controlers.Watchable;
import deodex.tools.AdbUtils;
import deodex.tools.CmdUtils;
import deodex.tools.FilesUtils;

public class Window extends JFrame implements ThreadWatcher, ChangeListener {

	class AdbWorker implements Runnable, Watchable {
		File systemFolder;
		ThreadWatcher watcher;

		public AdbWorker(File folder) {
			this.systemFolder = folder;
		}

		@Override
		public void addThreadWatcher(ThreadWatcher watcher) {
			this.watcher = watcher;
		}

		@Override
		public void run() {

			boolean extractStatus = AdbUtils.extractSystem(systemFolder, logger);
			if (!extractStatus) {
				logger.addLog(R.getString(S.LOG_ERROR)
						+ "Couldn't extract system please check your cable and your phone before trying again !");
				this.updateWatcherFail();
				return;
			}
			boolean deodexable = FilesUtils.isAValideSystemDir(systemFolder, logger);
			if (!deodexable) {
				this.updateWatcherFail();
				return;
			}
			this.updateWatcherSuccess();
		}

		public void updateWatcherFail() {
			watcher.updateProgress();
		}

		public void updateWatcherSuccess() {
			watcher.done(this);
		}

	}

	class BrowseAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean valide = false;
			JFileChooser f = new JFileChooser();
			f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int i = f.showOpenDialog(rootPane);
			if (i == 0) {
				logger.clearAllLogs();
				valide = FilesUtils.isAValideSystemDir(f.getSelectedFile(), logger);
				FilesUtils.LogFilesListToFile(f.getSelectedFile());
			}
			if (valide) {
				browseField.setText(f.getSelectedFile().getAbsolutePath());
				SessionCfg.sessionFrom = 0;
				deodexNow.setEnabled(true);
			} else {
				deodexNow.setEnabled(false);
			}
		}

	}

	class DeodexNowAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (inputPan.getSelectedIndex() == 0) {
				deodexNow();
			} else {
				adbDeodexNow();
			}
		}

	}

	class MenuItemsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Object source = arg0.getSource();
			if (source.equals(batchZipalignSignMenuItem)) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						String[] cmd = {"java","-jar",S.TOOLS_JAR.getAbsolutePath(),"BZW"};
						// TODO Auto-generated method stub
						CmdUtils.runCommand(cmd);
					}
				}).start();

				//new ZipalignWindow(getThisFram());
			} else if (source.equals(exitMenuItem)) {
				int i = JOptionPane.showConfirmDialog(getThisFram(), R.getString("dialog.sure.exit.message"),
						R.getString("dialog.sure.exit"), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
				if (i == 0) {
					System.exit(0);
				}
			} else if (source.equals(aboutThisMenu)) {
				JOptionPane.showMessageDialog(getThisFram(),
						R.getString("0000002") + "\n" + R.getString("0000003") + "\n" + R.getString("0000004"),
						R.getString("0000005"), JOptionPane.INFORMATION_MESSAGE);
			} else if (source.equals(settingsItem)) {
				Alerts.showSettingsDialog(settingsItem);
			}

		}

	}

	public static final int W_WIDTH = 802;
	public static final int W_HEIGHT = 630;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @author lord-ralf-adolf
	 */

	//
	boolean sign = false;
	boolean zipalign = true;
	File systemFolder;
	int maxJobs;
	MainWorker mainWorker;

	JPanel rootPanel = new JPanel();
	// fields BrowseView
	JTextField browseField = new JTextField(R.getString(S.BROWSE_FEILD));
	MyWebButton browseBtn = new MyWebButton(R.getString("browseBtn"));
	JPanel optionalPan = new JPanel();
	JCheckBox zipalignCheck = new JCheckBox(R.getString("zipalignCheck"));
	JCheckBox signCheck = new JCheckBox(R.getString("signCheck"));
	MyWebButton deodexNow = new MyWebButton(R.getString("deodexNow"));
	LoggerPane logger = new LoggerPane();
	MyWebButton quitbtn = new MyWebButton(R.getString("window.exitbtn"));
	MyWebButton restart = new MyWebButton(R.getString("window.restartbtn"));
	MyWebButton zipIt;
	ImageIcon icon;
	JTabbedPane inputPan = new JTabbedPane();
	JPanel fromDevicePanel = new JPanel();

	JPanel fromFolderPanel = new JPanel();
	JLabel deviceName = new JLabel(R.getString("0000044"));
	JLabel deviceStatus = new JLabel(R.getString("0000045"));
	JTextField devieNameField = new JTextField();
	JTextField devieStatusField = new JTextField();
	MyWebButton refreshDevices = new MyWebButton(R.getString("0000046"));

	// JMuneBar & MenuItems
	JMenuBar menuBar = new JMenuBar();
	// File Menu
	JMenu fichierMenu = new JMenu(R.getString("file"));
	JMenuItem exitMenuItem = new JMenuItem(R.getString("exit"));

	JMenuItem settingsItem = new JMenuItem(R.getString("0000052"));
	// Tools Menu
	JMenu toolsMenu = new JMenu(R.getString("tools"));

	JMenuItem batchZipalignSignMenuItem = new JMenuItem(R.getString("batch.zipalign.sign.menu.items"));
	// About
	JMenu aboutMenu = new JMenu(R.getString("about.menu"));
	JMenuItem aboutThisMenu = new JMenuItem(R.getString("About.this.program"));
	int currentSelectedtab = 0;
	boolean workInProgress = false;

	JRadioButton focusStealer = new JRadioButton();

	public Window() {
		this.setResizable(false);
		this.setIconImage(R.icon);
		this.setTitle(R.getString(S.APP_NAME));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - W_WIDTH) / 2,
				((Toolkit.getDefaultToolkit().getScreenSize().height - W_HEIGHT) / 2));
		this.setSize(W_WIDTH, W_HEIGHT);
		this.setJMenuBar(menuBar);
		rootPanel.setSize(W_WIDTH, W_HEIGHT);
		this.add(rootPanel, BorderLayout.CENTER);
		icon = new ImageIcon(Window.this.getClass().getResource("/loading.gif"));
		browseBtn.addActionListener(new BrowseAction());
		this.deodexNow.addActionListener(new DeodexNowAction());
		inputPan.addChangeListener(this);
		inputPan.add(R.getString("0000047"), fromFolderPanel);
		inputPan.add(R.getString("0000048"), fromDevicePanel);
		refreshDevices.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Move this somewhere else when you have tile
				refressAdb();
			}

		});

		initMenuBar();
		initBrowseView();
	}

	private void adbDeodexNow() {
		deodexNow.setEnabled(false);
		this.workInProgress = true;
		String name = JOptionPane.showInputDialog(zipIt, R.getString("0000050") + "\n" + R.getString("0000051"));
		File outPutFolder;
		boolean valid = false;
		if (name != null) {

			try {
				new File(System.getProperty("java.io.tmpdir") + File.separator + name).getParentFile().mkdirs();
				valid = new File(System.getProperty("java.io.tmpdir") + File.separator + name).createNewFile();
				new File(name).delete();
			} catch (InvalidPathException | IOException ex) {
				ex.printStackTrace();
				valid = false;
			}
		}
		if (valid) {
			outPutFolder = new File(S.EXTRACTED_SYSTEMS.getAbsolutePath() + File.separator + name);

			AdbWorker adbworker = new AdbWorker(outPutFolder);
			adbworker.addThreadWatcher(new ThreadWatcher() {

				@Override
				public void done(Runnable r) {
					deodexNow.setEnabled(true);
					deodexNow();
				}

				@Override
				public void sendFailed(Runnable r) {
					// TODO Auto-generated method stub

				}

				@Override
				public void updateProgress() {

					workInProgress = false;
				}

			});
			new Thread(adbworker).start();

		} else {
			JOptionPane.showMessageDialog(deodexNow, "The chosen name is not valid for a file name ! try again");
			deodexNow.setEnabled(true);
			this.workInProgress = false;
		}

	}

	public void addThreadWatcher() {
		mainWorker.addThreadWatcher(this);
	}

	private void deodexNow() {
		boolean yes;
		if (inputPan.getSelectedIndex() == 0)
			yes = Alerts.showDeodexNowAlert(rootPanel);
		else
			yes = true;
		if (yes) {
			if (inputPan.getSelectedIndex() == 0)
				maxJobs = Alerts.showThreadDialog(rootPanel);
			else
				maxJobs = Cfg.getMaxJobs();

			deodexNow.setEnabled(false);
			initwaiting();
			SessionCfg.setSign(signCheck.isSelected());
			SessionCfg.setZipalign(zipalignCheck.isSelected());
			new Thread(new Runnable() {

				@Override
				public void run() {

					mainWorker = new MainWorker(SessionCfg.getSystemFolder(), logger, maxJobs);
					addThreadWatcher();
					Thread t = new Thread(mainWorker);
					t.start();
				}

			}).start();
			this.workInProgress = true;
		}
	}

	@Override
	public void done(Runnable r) {

		this.initProgress();
		this.quitbtn.setEnabled(true);
		this.restart.setEnabled(true);
		this.zipIt.setEnabled(true);
		this.repaint();
		this.workInProgress = false;
	}

	private JFrame getThisFram() {
		return this;
	}

	private void initBrowseView() {

		JLabel boxsLabel = new JLabel(R.getString("box.jobs"));
		rootPanel.removeAll();
		rootPanel.setLayout(null);
		rootPanel.setOpaque(true);
		rootPanel.setBackground(new Color(189, 195, 199));

		// fonts
		// from folder tab
		browseField.setFont(R.COURIER_LOGGER);
		browseBtn.setFont(R.COURIER_NORMAL);
		// optional
		zipalignCheck.setFont(R.COURIER_NORMAL);
		signCheck.setFont(R.COURIER_NORMAL);
		optionalPan.setFont(R.COURIER_NORMAL);
		deodexNow.setFont(R.COURIER_NORMAL);
		// from device
		this.deviceName.setFont(R.SMALL_FONT);
		this.deviceStatus.setFont(R.SMALL_FONT);
		this.devieStatusField.setFont(R.COURIER_NORMAL);
		this.devieNameField.setFont(R.COURIER_NORMAL);
		this.refreshDevices.setFont(R.COURIER_LOGGER);

		// colors Backgrounds
		deviceName.setBackground(new Color(184, 207, 229));
		deviceStatus.setBackground(new Color(184, 207, 229));
		devieStatusField.setBackground(R.FIELDS_BACK_COLOR);
		devieNameField.setBackground(R.FIELDS_BACK_COLOR);
		refreshDevices.setBackground(R.BUTTONS_BACK_COLOR);

		browseBtn.setBackground(new Color(89, 195, 216));
		browseField.setBackground(new Color(220, 237, 193));
		optionalPan.setBackground(new Color(206, 194, 229));
		zipalignCheck.setBackground(new Color(206, 194, 229));
		boxsLabel.setBackground(new Color(206, 194, 229));
		signCheck.setBackground(new Color(206, 194, 229));
		deodexNow.setBackground(new Color(89, 195, 216));
		logger.setBackground(Color.WHITE);

		// colors Forground

		// default actions
		browseBtn.setEnabled(true);
		browseField.setEditable(false);
		zipalignCheck.setSelected(true);
		signCheck.setSelected(false);
		deodexNow.setEnabled(false);
		browseField.setEnabled(true);
		devieNameField.setEditable(false);
		devieStatusField.setEditable(false);

		// Components bounds
		inputPan.setBounds(10, 0, 780, 140);
		browseField.setBounds(15, 35, 620, 40);
		browseBtn.setBounds(635, 35, 130, 40);
		deviceName.setBounds(10, 5, 300, 35);
		this.deviceStatus.setBounds(315, 5, 300, 35);
		this.devieNameField.setBounds(10, 50, 300, 35);
		this.devieStatusField.setBounds(315, 50, 300, 35);
		this.refreshDevices.setBounds(620, 50, 145, 35);

		optionalPan.setBounds(10, 150, 440, 100);
		zipalignCheck.setBounds(5, 20, 115, 35);
		signCheck.setBounds(5, 57, 168, 35);
		deodexNow.setBounds(500, 170, 290, 60);
		logger.setBounds(1, 270, 798, 300);

		focusStealer.setBounds(-5, -5, 1, 1);

		// borders
		optionalPan.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(2), R.getString("optionalPan")));

		// toolTips
		zipalignCheck.setToolTipText(R.getString("zipalignCheck.ToolTip"));
		signCheck.setToolTipText(R.getString("signCheck.ToolTip"));

		// other propreties
		optionalPan.setOpaque(false);
		optionalPan.setLayout(null);
		fromFolderPanel.setLayout(null);
		fromDevicePanel.setLayout(null);
		// adding component

		this.fromDevicePanel.add(devieNameField);
		this.fromDevicePanel.add(devieStatusField);
		this.fromDevicePanel.add(deviceName);
		this.fromDevicePanel.add(deviceStatus);
		this.fromDevicePanel.add(refreshDevices);

		rootPanel.add(optionalPan);
		rootPanel.add(logger);
		rootPanel.add(deodexNow);
		optionalPan.add(this.signCheck);
		optionalPan.add(this.zipalignCheck);
		// rootPane.add(jobs);
		rootPane.add(focusStealer);
		fromFolderPanel.add(browseField);
		fromFolderPanel.add(browseBtn);
		rootPanel.add(inputPan);
		rootPanel.revalidate();
		this.repaint();

		@SuppressWarnings("unused")
		FileDrop fd = new FileDrop(this.browseField, new FileDrop.Listener() {

			@Override
			public void filesDropped(File[] files) {
				File file = files[0];
				if (!file.equals(null) && file.exists() && file.isDirectory()) {
					boolean valid = FilesUtils.isAValideSystemDir(file, logger);
					if (valid) {
						browseField.setText(file.getAbsolutePath());
						deodexNow.setEnabled(true);
					} else {
						deodexNow.setEnabled(false);
					}

				}

			}
		});
	}

	private void initFatalError() {
		rootPanel.removeAll();
		rootPanel.setLayout(null);
		rootPanel.setBackground(R.PANELS_BACK_COLOR);
		rootPanel.setOpaque(true);
		// TODO : externalize those
		JLabel errorLab = new JLabel("<HTML><p>Oops ... we couldn't initialize the working environement "
				+ " please make sure that you have followed all the guide lines if you think this is a bug please send a bug report along with the full log to rachidboudjelida@gmail.com or post it on XDA </p></HTML>");
		MyWebButton exit = new MyWebButton("Back");
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				initBrowseView();
			}

		});
		errorLab.setFont(R.COURIER_NORMAL);
		errorLab.setBounds(50, 10, 700, 100);
		exit.setBounds(100, 150, 600, 60);
		rootPanel.add(errorLab);
		rootPanel.add(exit);
		rootPanel.add(logger);
		rootPanel.revalidate();
		this.repaint();
	}

	private void initMenuBar() {

		menuBar.setVisible(true);
		// attach menus to the bar
		menuBar.add(fichierMenu);
		menuBar.add(toolsMenu);
		menuBar.add(aboutMenu);

		// attach items to File menu
		this.fichierMenu.add(this.settingsItem);
		this.fichierMenu.add(exitMenuItem);
		fichierMenu.setFont(R.COURIER_NORMAL);
		settingsItem.setFont(R.COURIER_NORMAL);
		exitMenuItem.setFont(R.COURIER_NORMAL);
		// attach tools Items
		this.toolsMenu.add(this.batchZipalignSignMenuItem);
		toolsMenu.setFont(R.COURIER_NORMAL);
		batchZipalignSignMenuItem.setFont(R.COURIER_NORMAL);

		// attach about Items
		this.aboutMenu.add(this.aboutThisMenu);
		aboutMenu.setFont(R.COURIER_NORMAL);
		aboutThisMenu.setFont(R.COURIER_NORMAL);

		/// les Action

		this.setVisible(true);
		batchZipalignSignMenuItem.addActionListener(new MenuItemsListener());
		exitMenuItem.addActionListener(new MenuItemsListener());
		aboutThisMenu.addActionListener(new MenuItemsListener());
		this.settingsItem.addActionListener(new MenuItemsListener());
	}

	public void initProgress() {
		rootPanel.removeAll();
		rootPanel.setLayout(null);
		rootPanel.setBackground(R.PANELS_BACK_COLOR);
		rootPanel.setOpaque(true);

		quitbtn = new MyWebButton(R.getString("window.exitbtn"));
		restart = new MyWebButton(R.getString("window.restartbtn"));
		zipIt = new MyWebButton(R.getString("create.zip.btn"));
		//
		mainWorker.mainPannel.setBounds(0, 5, 795, 224);
		logger.setBounds(1, 270, 798, 300);

		quitbtn.setBounds(690, 235, 100, 30);
		zipIt.setBounds(115, 235, 570, 30);
		restart.setBounds(10, 235, 100, 30);

		quitbtn.setFont(R.COURIER_NORMAL);
		restart.setFont(R.COURIER_NORMAL);
		zipIt.setFont(R.COURIER_NORMAL);

		quitbtn.setEnabled(false);
		restart.setEnabled(false);
		zipIt.setEnabled(false);
		quitbtn.setBackground(new Color(89, 195, 216));
		restart.setBackground(new Color(89, 195, 216));
		zipIt.setBackground(new Color(69, 179, 157)/* new Color(0, 183, 92) */);
		// Action listners
		quitbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int i = JOptionPane.showConfirmDialog(rootPanel, R.getString("dialog.sure.exit.message"),
						R.getString("dialog.sure.exit"), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

				if (i == 0) {
					System.exit(0);
				}
			}

		});

		restart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				logger.clearAllLogs();
				initBrowseView();
			}

		});

		zipIt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int agree = JOptionPane.showConfirmDialog(zipIt,
						R.getString("0000009") + "\n" + R.getString("0000010") + "\n" + R.getString("0000011") + "\n"
								+ R.getString("0000012") + "\n" + R.getString("0000013") + "\n" + R.getString("0000014")
								+ "\n" + R.getString("0000015") + "\n" + R.getString("0000016") + "\n\n"
								+ R.getString("0000017"),
						R.getString("0000018"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (agree == 0) {
					String name = JOptionPane.showInputDialog(zipIt,
							R.getString("0000006") + "\n" + R.getString("0000007"));
					if (name != null) {
						boolean valid = false;
						try {
							new File(System.getProperty("java.io.tmpdir") + File.separator + name).getParentFile().mkdirs();
							valid = new File(System.getProperty("java.io.tmpdir") + File.separator + name).createNewFile();

						} catch (InvalidPathException | IOException ex) {
							ex.printStackTrace();
							valid = false;
						}
						System.out.println(valid);
						if (valid) {
							new File(name).delete();
							String abName = name.endsWith(".zip") ? name.substring(0, name.lastIndexOf(".")) : name;
							zipIt.setEnabled(false);
							File zipFile = new File(S.ZIP_OUTPUT + File.separator + abName + ".zip");
							new FlashableZipCreater(SessionCfg.getSystemFolder(), zipFile, getThisFram());
						} else {
							JOptionPane.showMessageDialog(zipIt, R.getString("0000008"));
						}
					}
				}
			}

		});
		//
		// rootPanel.add(logo);
		rootPanel.add(logger);
		rootPanel.add(mainWorker.mainPannel);
		rootPanel.add(quitbtn);
		rootPanel.add(restart);
		rootPanel.add(zipIt);

		rootPanel.revalidate();
		this.repaint();
	}

	private void initwaiting() {
		rootPanel.removeAll();
		rootPanel.setLayout(null);
		rootPanel.setBackground(new Color(26, 135, 197));
		rootPanel.setOpaque(true);

		JLabel waiting = new JLabel("De-Optimizing boot.oat this may take a minute...");
		waiting.setFont(R.COURIER_NORMAL.deriveFont(25.0f));
		waiting.setBounds(10, 25, 748, 50);
		waiting.setBackground(new Color(0, 0, 0, 0));
		logger.setBounds(1, 270, 798, 300);

		int min = 0;
		int max = 100;
		WebProgressBar progress = new WebProgressBar(min, max);
		JLabel progLAb = new JLabel(icon);
		// Play animation
		progress.setIndeterminate(true);
		progLAb.setBounds(2, 95, 798, this.getHeight() - 2);

		rootPanel.add(progLAb);
		rootPanel.add(waiting);
		rootPanel.revalidate();
		this.repaint();
	}

	private void refressAdb() {
		// do nothing if an operation is in progress
		if (this.workInProgress) {
			return;
		}
		String formatedDevice = AdbUtils.getDevices(logger);
		this.devieNameField.setText("  " + formatedDevice.substring(0, formatedDevice.lastIndexOf("|")));
		this.devieStatusField.setText("  " + formatedDevice.substring(formatedDevice.lastIndexOf("|") + 1));
		if (!formatedDevice.equals(AdbUtils.NULL_DEVICE)) {
			this.deodexNow.setEnabled(true);
			logger.addLog(R.getString(S.LOG_INFO) + "You are good to go :D  click deodex Now to proceed ");
			SessionCfg.sessionFrom = 1;
		} else {
			// TODO POPUP instructions
			this.deodexNow.setEnabled(false);
		}
	}

	@Override
	public void sendFailed(Runnable r) {
		// TODO Auto-generated method stub
		initFatalError();
		this.workInProgress = false;
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {

		if (arg0.getSource().equals(inputPan)) {
			if (!this.workInProgress) {
				if (inputPan.getSelectedIndex() == 0) {
					// we are on the folder selection
					if (SessionCfg.getSystemFolder() != null && SessionCfg.sessionFrom == 0) {
						this.deodexNow.setEnabled(true);
					} else {
						// we are from device
						this.deodexNow.setEnabled(false);
					}
				} else {
					this.deodexNow.setEnabled(false);
				}
				this.currentSelectedtab = inputPan.getSelectedIndex();
			} else {
				JOptionPane.showMessageDialog(inputPan, R.getString("0000049"));
				this.inputPan.setSelectedIndex(this.currentSelectedtab);
			}
		}

	}

	@Override
	public void updateProgress() {

		initProgress();
	}
}
