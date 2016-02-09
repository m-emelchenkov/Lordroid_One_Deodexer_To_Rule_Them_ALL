/*
 * 
 * 
 * Copyright 2016 Rachid Boudjelida <rachidboudjelida@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package deodex.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import deodex.R;
import deodex.S;
import deodex.SessionCfg;
import deodex.controlers.MainWorker;
import deodex.controlers.ThreadWatcher;
import deodex.tools.FilesUtils;

public class Window extends JFrame implements ThreadWatcher{

	public static final int W_WIDTH = 802;
	public static final int W_HEIGHT = 597;
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
			g.drawImage(R.borderLeft, 6, 90, this);
			g.drawImage(R.borderRight, 802 - 2, 90, this);

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
	ImageIcon icon;
	JComboBox<Integer> jobs = new JComboBox<Integer>();
	Integer[] ints = {1,2,3,4};
	public Window()  {
		this.setResizable(false);
		this.setIconImage(R.icon);
		this.setTitle(R.getString(S.APP_NAME));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - W_WIDTH) / 2,
				((Toolkit.getDefaultToolkit().getScreenSize().height - W_HEIGHT) / 2));
		this.setSize(W_WIDTH, W_HEIGHT);
		this.setVisible(true);
		rootPanel.setSize(W_WIDTH, W_HEIGHT);
		//this.setContentPane(rootPanel);
		this.add(rootPanel);
		icon = new ImageIcon(Window.this.getClass().getResource("/loading.gif"));
		browseBtn.addActionListener(new BrowseAction());
		this.deodexNow.addActionListener(new DeodexNowAction());

		for (Integer i : ints)
		jobs.addItem(i);
		
		initBrowseView();
		}

	private void initBrowseView() {
		JLabel boxsLabel = new JLabel(R.getString("box.jobs"));
		rootPane.removeAll();
		rootPane.setLayout(null);
		rootPane.setBackground(new Color(206, 194, 229));
		rootPane.setOpaque(true);

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
		jobs.setBackground(new Color(206, 194, 229));
		signCheck.setBackground(new Color(206, 194, 229));
		deodexNow.setBackground(new Color(89, 195, 216));
		logger.setBackground(Color.WHITE);
		// colors Forground

		// default actions
		browseBtn.setEnabled(true);
		browseField.setEnabled(true);
		zipalignCheck.setSelected(true);
		jobs.setSelectedIndex(0);
		signCheck.setSelected(false);
		deodexNow.setEnabled(false);
		browseField.setEnabled(false);
		
		
		// Components bounds
		logo.setBounds(0, 0, 802, 100);
		browseField.setBounds(10, 110, 650, 40);
		browseBtn.setBounds(660, 110, 100, 40);
		optionalPan.setBounds(10, 150, 440, 100);
		// zipalignCheck.setBounds(15, 170, 430, 35);
		// signCheck.setBounds(15, 207, 430, 35);
		zipalignCheck.setBounds(5, 20, 115, 35);
//		boxsLabel.setBounds(125, 20, 50, 35);
//		jobs.setBounds(180, 20, 50, 35);
		signCheck.setBounds(5, 57, 168, 35);
		deodexNow.setBounds(500, 170, 260, 60);
//		deodexNow.setBounds(610, 170, 150, 60);
//		boxsLabel.setBounds(470, 185, 100, 30);
//		jobs.setBounds(570, 185, 34, 30);
		logger.setBounds(1, 270, 798, 300);

		// borders
		browseField.setBorder(BorderFactory.createLineBorder(new Color(89, 195, 216)));
		optionalPan.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new Color(89, 195, 216), 2), R.getString("optionalPan")));

		// toolTips
		// zipalignCheck.setToolTipText(R.getString("zipalignCheck.ToolTip"));
		// signCheck.setToolTipText(R.getString("signCheck.ToolTip"));
		// other propreties
		optionalPan.setOpaque(false);
		optionalPan.setLayout(null);

		// adding component

		// XXX: need this to steal the focus from textField ?
		// is there an other way to do this ?
		focusStealer.setBounds(-50, -50, 1, 1);

		rootPane.add(optionalPan);
		rootPane.add(logger);
		rootPane.add(focusStealer);
		rootPane.add(deodexNow);
		optionalPan.add(this.signCheck);
		optionalPan.add(this.zipalignCheck);
//		rootPane.add(jobs);
//		rootPane.add(boxsLabel);
		rootPane.add(logo);
		rootPane.add(browseField);
		rootPane.add(browseBtn);
		rootPane.revalidate();
		this.repaint();


		@SuppressWarnings("unused")
		FileDrop fd = new FileDrop(this.browseField , new FileDrop.Listener() {
			
			@Override
			public void filesDropped(File[] files) {
				File file = files[0];
				if(!file.equals(null) && file.exists() && file.isDirectory()){
					boolean valid = FilesUtils.isAValideSystemDir(file, logger);
					if(valid){
						browseField.setText(file.getAbsolutePath());
						deodexNow.setEnabled(true);
					} else {
						deodexNow.setEnabled(false);
					}
					
				}
				
				
			}
		});
	}

	public void initProgress(){
		rootPane.removeAll();
		rootPane.setLayout(null);
		rootPane.setBackground(new Color(206, 194, 229));
		rootPane.setOpaque(true);
		
		quitbtn = new JButton(R.getString("window.exitbtn"));
		restart = new JButton(R.getString("window.restartbtn"));
		
		
		// 
		mainWorker.mainPannel.setBounds(0, 101, 795, 128);
		logo.setBounds(0, 0, 802, 100);
		logger.setBounds(1, 270, 798, 300);
		quitbtn.setBounds(483, 235, 300, 30);
		restart.setBounds(10, 235, 300, 30);
		
		quitbtn.setFont(R.COURIER_NORMAL);
		restart.setFont(R.COURIER_NORMAL);
		
		quitbtn.setEnabled(false);
		restart.setEnabled(false);
		
		quitbtn.setBackground(new Color(89, 195, 216));
		restart.setBackground(new Color(89, 195, 216));
		
		// Action listners
		quitbtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int i =JOptionPane.showConfirmDialog(rootPane, R.getString("dialog.sure.exit.message"), R.getString("dialog.sure.exit"), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
				
				if(i == 0){
					System.exit(0);
				}
			}
			
		});
		
		restart.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				logger.clearAllLogs();
				initBrowseView();
			}
			
		});
		//
		rootPane.add(logo);
		rootPane.add(logger);
		rootPane.add(mainWorker.mainPannel);
		rootPane.add(quitbtn);
		rootPane.add(restart);
		
		rootPane.revalidate();
		this.repaint();
	}
	
	private void initwaiting(){
		rootPane.removeAll();
		rootPane.setLayout(null);
		rootPane.setBackground(new Color(206, 194, 229));
		rootPane.setOpaque(true);
		
		JLabel waiting = new JLabel("Preparing working environnement this may take a minute...");
		waiting.setFont(R.COURIER_NORMAL);
		waiting.setBounds(50, 200, 748, 50);
		waiting.setBackground(new Color(0,0,0,0));
		logo.setBounds(0, 0, 802, 100);
		logger.setBounds(1, 270, 798, 300);
		
	    int min = 0;
	    int max = 100;
	    JProgressBar progress = new JProgressBar(min, max);
	    JLabel progLAb = new JLabel(icon);
	    // Play animation
	    progress.setIndeterminate(true);
	    progLAb.setBounds(0, 95, 798, this.getHeight());

	    rootPane.add(progLAb);
	    //rootPane.add(logger);
	    rootPane.add(logo);
	    rootPane.add(waiting);
		rootPane.revalidate();
		this.repaint();
	}
	
	class BrowseAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean valide = false;
			JFileChooser f = new JFileChooser();
			f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int i = f.showOpenDialog(rootPane);
			if (i == 0) {
				// logger.clearAllLogs();
				valide = FilesUtils.isAValideSystemDir(f.getSelectedFile(), logger);
			}
			if (valide) {
				browseField.setText(f.getSelectedFile().getAbsolutePath());
				deodexNow.setEnabled(true);
			} else {
				deodexNow.setEnabled(false);
			}
		}

	}

	public void addThreadWatcher(){
		mainWorker.addThreadWatcher(this);
	}
	class DeodexNowAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			initwaiting();
			SessionCfg.setSign(signCheck.isSelected());
			SessionCfg.setZipalign(zipalignCheck.isSelected());
			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mainWorker = new MainWorker(SessionCfg.getSystemFolder(), logger,4);
					addThreadWatcher();
					Thread t = new Thread(mainWorker);
					t.start();
					deodexNow.setEnabled(false);
				}
				
			}).start();

		}

	}

	@Override
	public void done(Runnable r) {
		// TODO Auto-generated method stub
		this.initProgress();
		this.quitbtn.setEnabled(true);
		this.restart.setEnabled(true);
		this.repaint();
	}

	@Override
	public void updateProgress() {
		// TODO Auto-generated method stub
		initProgress();
	}
}
