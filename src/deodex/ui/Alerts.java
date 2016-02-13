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

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import deodex.Cfg;
import deodex.R;

public class Alerts {

	public static boolean showDeodexNowAlert(JComponent comp) {
		int i = 1;
		if (Cfg.doShowDeodexAlert()) {
			DeodexAlertPanel alertPane = new DeodexAlertPanel();
			JOptionPane pane = new JOptionPane(alertPane, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null);
			JDialog dialog = pane.createDialog(comp, "INFO");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setSize(450, 180);
			dialog.setVisible(true);
			try {
				i = (int) pane.getValue();
			} catch (Exception e) {
				return false;
			}
			if (i == 0) {
				Cfg.setShowDeodexAlert(!alertPane.box.isSelected());
			}
		} else {
			return true;
		}
		return i == 0;
	}

	public static int showThreadDialog() {
		if (Cfg.doShowThreadAlert()) {
			ThreadAlertPanel alertPane = new ThreadAlertPanel();
			JOptionPane pane = new JOptionPane(alertPane, JOptionPane.PLAIN_MESSAGE);
			JDialog dialog = pane.createDialog(null, R.getString("box.jobs"));
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setSize(500, 250);
			dialog.setVisible(true);
			int status = 8;
			try {
				status = (int) pane.getValue();
			} catch (Exception e) {

			}
			if (status == 0) {
				Cfg.setShowThreadAlert(!alertPane.box.isSelected());
				Cfg.setMaxJobs((int) alertPane.count.getSelectedItem());
			}
		}

		return Cfg.getMaxJobs();
	}
}
