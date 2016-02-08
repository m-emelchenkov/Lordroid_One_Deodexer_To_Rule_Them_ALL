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

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import deodex.R;
import deodex.controlers.LoggerPan;
import deodex.tools.PathUtils;
import deodex.tools.PropReader;

public class LoggerPane extends JPanel implements LoggerPan {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image bg;
	JList<String> logs = new JList<String>();
	DefaultListModel<String> model = new DefaultListModel<String>();
	
	public LoggerPane() {
		super();
		this.setSize(798, 300);
		try {
			this.bg = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/logger_back.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logs = new JList<String>(model);
		logs.setFont(R.COURIER_LOGGER);
		// logs.setBounds(2, 2, this.getWidth()-4, this.getHeight()-4);
		this.setLayout(null);
		JScrollPane scroll = new JScrollPane(logs);
		scroll.setBounds(2, 2, this.getWidth() - 4, this.getHeight() - 4);
		this.add(scroll);

	}

	// multiple threads can log here !
	public synchronized void addLogR(String str) {
		long yourmilliseconds = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); // dd/MMM/yyyy
		Date resultdate = new Date(yourmilliseconds);
		String str2 = sdf.format(resultdate);
		model.addElement("[" + str2 + "]" + str);
		this.repaint();
	}

	public void clearAllLogs() {
		model.removeAllElements();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bg, 0, 0, this);
	}

	@Override
	public void addLog(String str) {
		// TODO Auto-generated method stub
		addLogR(str);
	}

	@Override
	public synchronized void saveToFile() {
		// TODO Auto-generated method stub
		
		long yourmilliseconds = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss"); // dd/MMM/yyyy
		Date resultdate = new Date(yourmilliseconds);
		String str = PathUtils.getExcutionPath()+File.separator +"logs"+File.separator+ sdf.format(resultdate)+".log";
		File logFile = new File(str);
		
		ArrayList <String> logs = new ArrayList<String>();
		
		for (int i = 0 ; i < model.size() ; i++){
			logs.add(model.getElementAt(i));
		}
		PropReader.ArrayToProp(logs, logFile);
		this.repaint();
	}
	
	
}
