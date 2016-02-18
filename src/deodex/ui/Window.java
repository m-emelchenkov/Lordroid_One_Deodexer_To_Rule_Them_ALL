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
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import deodex.R;
import deodex.S;
import deodex.SessionCfg;
import deodex.controlers.FlashableZipCreater;
import deodex.controlers.MainWorker;
import deodex.controlers.ThreadWatcher;
import deodex.tools.FilesUtils;

public class Window extends JFrame implements ThreadWatcher {

	class MenuItemsListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			Object source = arg0.getSource();
			if(source.equals(batchZipalignSignMenuItem)){
				new ZipalignWindow(logo);
			} else if(source.equals(exitMenuItem)){
				int i = JOptionPane.showConfirmDialog(rootPanel, R.getString("dialog.sure.exit.message"),
						R.getString("dialog.sure.exit"), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
				if(i == 0){
					System.exit(0);
				}
			} else if(source.equals(aboutThisMenu)){
				JOptionPane.showMessageDialog(logo, R.getString("0000002")+"\n"+R.getString("0000003")+"\n"+R.getString("0000004"), R.getString("0000005"), JOptionPane.INFORMATION_MESSAGE);
			}
			
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
				deodexNow.setEnabled(true);
			} else {
				deodexNow.setEnabled(false);
			}
		}

	}

	class DeodexNowAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			boolean yes = Alerts.showDeodexNowAlert(rootPanel);
			if (yes) {
				// TODO ShowThradAlert
				maxJobs = Alerts.showThreadDialog(rootPanel);

				deodexNow.setEnabled(false);
				initwaiting();
				SessionCfg.setSign(signCheck.isSelected());
				SessionCfg.setZipalign(zipalignCheck.isSelected());
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO the Thread max number will be calculated like
						// this
						// cpu count/2
						mainWorker = new MainWorker(SessionCfg.getSystemFolder(), logger, maxJobs);
						addThreadWatcher();
						Thread t = new Thread(mainWorker);
						t.start();
					}

				}).start();

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
	JPanel rootPanel = new JPanel() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g) {

			super.paintComponent(g);
			g.setColor(new Color(206, 194, 229));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.drawImage(R.borderLeft, 0, 90, this);
			g.drawImage(R.borderRight, 802 - 1, 90, this);
		}
	};
	// fields BrowseView
	LogoPane logo = new LogoPane();
	JTextField browseField = new JTextField(R.getString(S.BROWSE_FEILD));
	JButton browseBtn = new JButton(R.getString("browseBtn"));
	JPanel optionalPan = new JPanel();
	JCheckBox zipalignCheck = new JCheckBox(R.getString("zipalignCheck"));
	JCheckBox signCheck = new JCheckBox(R.getString("signCheck"));
	JButton deodexNow = new JButton(R.getString("deodexNow"));
	JRadioButton focusStealer = new JRadioButton();
	LoggerPane logger = new LoggerPane();
	JButton quitbtn = new JButton(R.getString("window.exitbtn"));
	JButton restart = new JButton(R.getString("window.restartbtn"));
	JButton zipIt;
	ImageIcon icon;

	// JMuneBar & MenuItems
	JMenuBar menuBar = new JMenuBar();
	
	// File Menu
	JMenu fichierMenu = new JMenu(R.getString("file"));
	JMenuItem exitMenuItem = new JMenuItem(R.getString("exit"));
	
	// Tools Menu
	JMenu toolsMenu = new JMenu(R.getString("tools"));
	JMenuItem batchZipalignSignMenuItem = new JMenuItem(R.getString("batch.zipalign.sign.menu.items")); 
	
	// About 
	JMenu aboutMenu = new JMenu(R.getString("about.menu"));
	JMenuItem aboutThisMenu = new JMenuItem(R.getString("About.this.program"));
	
	
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
		//this.setContentPane(rootPanel);
		this.add(rootPanel,BorderLayout.CENTER);
		icon = new ImageIcon(Window.this.getClass().getResource("/loading.gif"));
		browseBtn.addActionListener(new BrowseAction());
		this.deodexNow.addActionListener(new DeodexNowAction());
		initMenuBar();
		initBrowseView();
	}

	private void initMenuBar(){
		
		menuBar.setVisible(true);
		// attach menus to the bar
		//menuBar.add(fichierMenu);
		menuBar.add(toolsMenu);
		menuBar.add(aboutMenu);
		
		// attach items to File menu
		this.fichierMenu.add(exitMenuItem);
		
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
	}
	
	public void addThreadWatcher() {
		mainWorker.addThreadWatcher(this);
	}

	@Override
	public void done(Runnable r) {
		// TODO Auto-generated method stub
		this.initProgress();
		this.quitbtn.setEnabled(true);
		this.restart.setEnabled(true);
		this.zipIt.setEnabled(true);
		this.repaint();
	}

	private void initBrowseView() {
		JLabel boxsLabel = new JLabel(R.getString("box.jobs"));
		rootPanel.removeAll();
		rootPanel.setLayout(null);
		rootPanel.setBackground(new Color(206, 194, 229));
		rootPanel.setOpaque(true);

		// fonts
		browseField.setFont(R.COURIER_NORMAL);
		browseBtn.setFont(R.COURIER_NORMAL);
		zipalignCheck.setFont(R.COURIER_NORMAL);
		signCheck.setFont(R.COURIER_NORMAL);
		optionalPan.setFont(R.COURIER_NORMAL);
		deodexNow.setFont(R.COURIER_NORMAL);

		// colors Backgrounds
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

		// Components bounds
		logo.setBounds(0, 0, 802, 100);
		browseField.setBounds(10, 110, 650, 40);
		browseBtn.setBounds(660, 110, 130, 40);
		optionalPan.setBounds(10, 150, 440, 100);
		zipalignCheck.setBounds(5, 20, 115, 35);
		signCheck.setBounds(5, 57, 168, 35);
		deodexNow.setBounds(500, 170, 290, 60);
		logger.setBounds(1, 270, 798, 300);

		// borders
		browseField.setBorder(BorderFactory.createLineBorder(new Color(89, 195, 216)));
		optionalPan.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new Color(89, 195, 216), 2), R.getString("optionalPan")));

		// toolTips
		 zipalignCheck.setToolTipText(R.getString("zipalignCheck.ToolTip"));
		 signCheck.setToolTipText(R.getString("signCheck.ToolTip"));

		// other propreties
		optionalPan.setOpaque(false);
		optionalPan.setLayout(null);

		// adding component

		// XXX: need this to steal the focus from textField ?
		// is there an other way to do this ?
		focusStealer.setBounds(-50, -50, 1, 1);

		rootPanel.add(optionalPan);
		rootPanel.add(logger);
		rootPanel.add(focusStealer);
		rootPanel.add(deodexNow);
		optionalPan.add(this.signCheck);
		optionalPan.add(this.zipalignCheck);
		// rootPane.add(jobs);
		// rootPane.add(boxsLabel);
		rootPanel.add(logo);
		rootPanel.add(browseField);
		rootPanel.add(browseBtn);
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

	public void initProgress() {
		rootPanel.removeAll();
		rootPanel.setLayout(null);
		rootPanel.setBackground(new Color(206, 194, 229));
		rootPanel.setOpaque(true);

		quitbtn = new JButton(R.getString("window.exitbtn"));
		restart = new JButton(R.getString("window.restartbtn"));
		zipIt = new JButton(R.getString("create.zip.btn"));
		//
		mainWorker.mainPannel.setBounds(0, 101, 795, 128);
		logo.setBounds(0, 0, 802, 100);
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
		zipIt.setBackground(new Color(69, 179, 157)/*new Color(0, 183, 92)*/);
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
				// TODO Auto-generated method stub
				logger.clearAllLogs();
				initBrowseView();
			}

		});
		
		zipIt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int agree = JOptionPane.showConfirmDialog(zipIt,
							R.getString("0000009")+"\n"+
							R.getString("0000010")+"\n"+
							R.getString("0000011")+"\n"+
							R.getString("0000012")+"\n"+
							R.getString("0000013")+"\n"+	
							R.getString("0000014")+"\n"+
							R.getString("0000015")+"\n"+
							R.getString("0000016")+"\n\n"+
							R.getString("0000017"),
							R.getString("0000018"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(agree == 0){
				String name = JOptionPane.showInputDialog(zipIt,R.getString("0000006")+"\n"+R.getString("0000007"));
				if(name != null){
					boolean valid = false;
					try {
						valid = new File(name).createNewFile();
						
						
					} catch (InvalidPathException  | IOException ex){
						ex.printStackTrace();
						valid = false;
					}
					System.out.println(valid);
					if(valid){
						new File(name).delete();
						String abName = name.endsWith(".zip")? name.substring(0, name.lastIndexOf(".")) : name;
						zipIt.setEnabled(false);
						File zipFile = new File(S.ZIP_OUTPUT+File.separator+abName+".zip");
						new FlashableZipCreater(SessionCfg.getSystemFolder(), zipFile, getThisFram());
					} else {
						JOptionPane.showMessageDialog(zipIt, R.getString("0000008"));
					}
				}
				}
			}
			
		});
		//
		rootPanel.add(logo);
		rootPanel.add(logger);
		rootPanel.add(mainWorker.mainPannel);
		rootPanel.add(quitbtn);
		rootPanel.add(restart);
		rootPanel.add(zipIt);
		
		rootPanel.revalidate();
		this.repaint();
	}

	private JFrame getThisFram(){
		return this;
	}
	private void initwaiting() {
		rootPanel.removeAll();
		rootPanel.setLayout(null);
		rootPanel.setBackground(new Color(206, 194, 229));
		rootPanel.setOpaque(true);

		JLabel waiting = new JLabel("Preparing working environnement this may take a minute...");
		waiting.setFont(R.COURIER_NORMAL);
		waiting.setBounds(50, 200, 748, 50);
		waiting.setBackground(new Color(0, 0, 0, 0));
		logo.setBounds(0, 0, 802, 100);
		logger.setBounds(1, 270, 798, 300);

		int min = 0;
		int max = 100;
		JProgressBar progress = new JProgressBar(min, max);
		JLabel progLAb = new JLabel(icon);
		// Play animation
		progress.setIndeterminate(true);
		progLAb.setBounds(2, 95, 798, this.getHeight() - 2);

		rootPanel.add(progLAb);
		// rootPane.add(logger);
		rootPanel.add(logo);
		rootPanel.add(waiting);
		rootPanel.revalidate();
		this.repaint();
	}

	@Override
	public void updateProgress() {
		// TODO Auto-generated method stub
		initProgress();
	}
}
