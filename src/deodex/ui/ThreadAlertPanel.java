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

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import deodex.R;

public class ThreadAlertPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel text1=new JLabel("<HTML><p>"+R.getString("alert.thread.line1")+
	R.getString("alert.thread.line2")+"</p><p><br>"+
	R.getString("alert.thread.line3")+
	R.getString("alert.thread.line4")+"</p></HTML>");
	JCheckBox box = new JCheckBox(R.getString("alert.thread.box.dont.ask.again"));
	Integer ints[] = {1,2,3,4};
	JLabel jobsLab = new JLabel(R.getString("box.jobs"));
	JComboBox<Integer> count = new JComboBox<Integer>(ints);
	public ThreadAlertPanel (){
//		for (Integer i : ints)
//			count.addItem(i);
		
		text1.setBounds(5, 5, 450, 80);
//		text2.setBounds(5, 25, 500, 20);
//		text3.setBounds(5, 50, 500, 20);
//		text4.setBounds(5, 70, 500, 20);
		jobsLab.setBounds(5, 110, 120, 20);
		count.setBounds(130, 110, 100, 20);
		box.setBounds(5, 140, 400, 20);
		count.setSelectedIndex(1);
		
		
		this.setSize(450, 130);
		this.setLayout(null);
		this.add(text1);
//		this.add(text2);
//		this.add(text3);
//		this.add(text4);

		this.add(jobsLab);
		this.add(count);
		this.add(box);
		
	}

}
