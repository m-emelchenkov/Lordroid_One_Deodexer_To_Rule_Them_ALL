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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import deodex.R;
import deodex.S;
import deodex.SessionCfg;
import deodex.controlers.MainWorker;
import deodex.tools.FilesUtils;

public class Window extends JFrame {

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
	
	
	
	JPanel rootPanel = new JPanel() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g) {

			// TODO still doesn"t work see this when you are less tired for
			// god's sake !
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

	public Window() {
		this.setResizable(false);
		this.setTitle(R.getString(S.APP_NAME));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - W_WIDTH) / 2,
				((Toolkit.getDefaultToolkit().getScreenSize().height - W_HEIGHT) / 2));
		this.setSize(W_WIDTH, W_HEIGHT);
		this.setVisible(true);

		this.setContentPane(rootPanel);

		initBrowseView();
	}

	private void initBrowseView() {
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
		signCheck.setBackground(new Color(206, 194, 229));
		deodexNow.setBackground(new Color(89, 195, 216));
		logger.setBackground(Color.WHITE);
		// colors Forground

		// default actions
		browseBtn.setEnabled(true);
		browseField.setEnabled(true);
		zipalignCheck.setSelected(true);
		signCheck.setSelected(false);
		deodexNow.setEnabled(false);

		// Components bounds
		logo.setBounds(0, 0, 802, 100);
		browseField.setBounds(10, 110, 650, 40);
		browseBtn.setBounds(660, 110, 100, 40);
		optionalPan.setBounds(10, 150, 440, 100);
		// zipalignCheck.setBounds(15, 170, 430, 35);
		// signCheck.setBounds(15, 207, 430, 35);
		zipalignCheck.setBounds(5, 20, 115, 35);
		signCheck.setBounds(5, 57, 168, 35);
		deodexNow.setBounds(500, 170, 260, 60);
		logger.setBounds(1, 270, 798, 300);

		// borders
		browseField.setBorder(BorderFactory.createLineBorder(new Color(89, 195, 216)));
		optionalPan.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new Color(89, 195, 216), 2), R.getString("optionalPan")));

		// toolTips
		// zipalignCheck.setToolTipText(R.getString("zipalignCheck.ToolTip"));
		// signCheck.setToolTipText(R.getString("signCheck.ToolTip"));
		// other propreties
		optionalPan.setOpaque(true);
		optionalPan.setLayout(null);

		// adding component

		// XXX: need this to steal the focus from textField ?
		// is there an other way to do this ?
		focusStealer.setBounds(-50, -50, 1, 1);
		
		rootPane.add(logger);
		rootPane.add(focusStealer);
		rootPane.add(deodexNow);
		optionalPan.add(this.signCheck);
		optionalPan.add(this.zipalignCheck);
		rootPane.add(logo);
		rootPane.add(browseField);
		rootPane.add(browseBtn);
		rootPane.add(optionalPan);
		rootPane.revalidate();
		this.repaint();

		// TODO remove this
		browseBtn.addActionListener(new BrowseAction());
		this.deodexNow.addActionListener(new DeodexNowAction());
	}
	
	
	class BrowseAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			boolean valide =false;
			JFileChooser f = new JFileChooser();
			f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int i = f.showOpenDialog(rootPane);
			if(i == 0){
			//	logger.clearAllLogs();
				valide = FilesUtils.isAValideSystemDir(f.getSelectedFile(), logger);
			}
			if(valide){
				browseField.setText(f.getSelectedFile().getAbsolutePath());
				deodexNow.setEnabled(true);
			} else {
				deodexNow.setEnabled(false);
			}
		}
		
	}

	class DeodexNowAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			SessionCfg.setSign(signCheck.isSelected());
			SessionCfg.setZipalign(zipalignCheck.isSelected());
			Thread t = new Thread(new MainWorker(SessionCfg.getSystemFolder(), logger));
			t.start();
			
		}
		
	}
}
