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
package deodex;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import deodex.ui.ThreadAlertPanel;

public class Launcher {

	static Integer[] ints = {1,2,3,4};
	
	public static void main(String[] args) {
		Cfg.readCfg();
		R.initResources();

		ThreadAlertPanel alertPane = new ThreadAlertPanel();
		JOptionPane pane = new JOptionPane(alertPane, JOptionPane.PLAIN_MESSAGE);
		JDialog dialog = pane.createDialog(null, R.getString("box.jobs"));
		//TODO : no exit on close 
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setSize(500,250);
		dialog.setVisible(true);
		
			//Cfg.setShowDeodexAlert(!alertPane.box.isSelected());


		

	}
}
