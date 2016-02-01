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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import deodex.R;

public class LoggerPan extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image bg ;
	JList<String> logs = new JList<String>();
	DefaultListModel<String> model = new DefaultListModel<String>();
	public LoggerPan(){
		super();
		this.setSize(798,300);
		try {
			this.bg = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/logger_back.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		logs = new JList<String>(model);
		logs.setFont(R.COURIER_LOGGER);
		//logs.setBounds(2, 2, this.getWidth()-4, this.getHeight()-4);
		this.setLayout(null);
		JScrollPane scroll = new JScrollPane(logs);
		scroll.setBounds(2, 2, this.getWidth()-4, this.getHeight()-4);
		this.add(scroll);
		
	}

	
	// TODO remove this and add it to a new OBserver / Observabe interface keep it synchronized ! 
	// multiple threads can log here !
	public synchronized void  addLog(String str){
		model.addElement(str);
		this.repaint();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(bg, 0, 0, this);
	}
	
}
