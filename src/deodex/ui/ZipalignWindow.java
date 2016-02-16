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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import deodex.Cfg;
import deodex.R;
import deodex.S;
import deodex.controlers.ThreadWatcher;
import deodex.controlers.ZipalignWorker;
import deodex.tools.FilesUtils;

public class ZipalignWindow extends JFrame implements ThreadWatcher{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	class BrowseAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setDialogTitle("Choose a folder containing apks !");
			int exitCode = fileChooser.showOpenDialog(rootPannel);
			if (exitCode == 0) {
				int apkCount = FilesUtils.searchrecursively(fileChooser.getSelectedFile(), ".apk").size();
				if (apkCount == 0) {
					logger.addLog(R.getString("log.no.apk.to.zipalign"));
					initProgress();
					} else {
					logger.addLog(R.getString("log.there.is") + apkCount + R.getString("apk.to.be.zipaligned.log"));
					zip = new ZipalignWorker(FilesUtils.searchrecursively(fileChooser.getSelectedFile(), ".apk"), bar, logger);
					addWatcher();
					zipalignBtn.setEnabled(true);
				}
			}
		}

	}

	class ZipNowAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			new Thread(zip).start();
		}

	}

	class OkAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			logger.clearAllLogs();
			initBrowse();
		}
		
	}
	ZipalignWorker zip ;
	JTextField browseField = new JTextField(R.getString(S.BROWSE_FEILD));
	JButton browseBtn = new JButton("...");
	JButton zipalignBtn = new JButton(R.getString("zipalign.now.btn"));
	JProgressBar bar = new JProgressBar();
	JPanel rootPannel = new JPanel();
	LoggerPane logger = new LoggerPane("");
	JButton okBtn = new JButton("OK");
	
	/**
	 * 
	 */
	public ZipalignWindow() {
		this.setSize(500, 300);
		this.setResizable(false);
		this.setTitle("ODTRTA >> Batch Zipalign ");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.add(rootPannel, BorderLayout.CENTER);

		// backcolors
		rootPannel.setBackground(R.PANELS_BACK_COLOR);
		browseBtn.setBackground(R.BUTTONS_BACK_COLOR);
		browseField.setBackground(R.FIELDS_BACK_COLOR);
		zipalignBtn.setBackground(R.BUTTONS_BACK_COLOR);
		okBtn.setBackground(R.BUTTONS_BACK_COLOR);
		
		
		// font
		browseBtn.setFont(R.SMALL_FONT);
		browseField.setFont(R.SMALL_FONT);
		zipalignBtn.setFont(R.COURIER_NORMAL);
		okBtn.setFont(R.COURIER_NORMAL);
		//
		bar.setBackground(Color.WHITE);
		bar.setForeground(new Color(0, 183, 92));
		bar.setFont(R.COURIER_NORMAL);
		// actions
		browseBtn.addActionListener(new BrowseAction());
		zipalignBtn.addActionListener(new ZipNowAction());
		okBtn.addActionListener(new OkAction());
		this.setVisible(true);
		initBrowse();
	}

	private void addWatcher(){
		zip.addThreadWatcher(this);
	}
	private void initBrowse() {
		rootPannel.removeAll();
		rootPannel.setLayout(null);

		zipalignBtn.setEnabled(false);
		browseField.setEditable(false);

		// bounds
		browseField.setBounds(5, 5, 400, 30);
		browseBtn.setBounds(405, 5, 90, 30);
		zipalignBtn.setBounds(5, 40, 490, 40);
		logger.setSize(490, 185);
		logger.repaint();
		logger.setBounds(5, 85, 490, 185);

		rootPannel.add(browseField);
		rootPannel.add(browseBtn);
		rootPannel.add(zipalignBtn);
		rootPannel.add(logger);
		rootPannel.revalidate();
		rootPannel.repaint();
	}

	private void initProgress(){
		rootPannel.removeAll();
		rootPannel.setLayout(null);

		bar.setBounds(5, 5, 490, 30);
		okBtn.setBounds(5, 40, 490, 40);
		okBtn.setEnabled(false);
		
		logger.setSize(490, 185);
		logger.repaint();
		logger.setBounds(5, 85, 490, 185);
		
		rootPannel.add(bar);
		rootPannel.add(okBtn);
		rootPannel.add(logger);
	
		
		rootPannel.revalidate();
		rootPannel.repaint();
	}
	
	public static void main(String[] args) {
		Cfg.setCurrentLang(S.ENGLISH);
		Cfg.writeCfgFile();
		Cfg.readCfg();
		R.initResources();

		new ZipalignWindow();
	}

	@Override
	public void done(Runnable r) {
		this.okBtn.setEnabled(true);
		logger.saveToFile("ZIPALIGN");
	}

	@Override
	public void updateProgress() {
		this.initProgress();
	}
}
