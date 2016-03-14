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

import java.io.File;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import deodex.Cfg;
import deodex.R;
import deodex.tools.FilesUtils;

public class Alerts {

	/**
	 * show the advanced settings dialog 
	 * @param jFrame the Jframe which the dialog's Location will be relative to
	 */
	public static void showAdvancedSettingsDialog(JFrame jFrame) {
		FilesUtils.copyFile(new File(Cfg.CFG_PATH), new File(Cfg.CFG_PATH + ".bak"));
		AdvancedSettings setings = new AdvancedSettings();
		JOptionPane pane = new JOptionPane(setings, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		JDialog dialog = pane.createDialog(jFrame, R.getString("0000052"));
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setSize(840, 430);
		dialog.setLocationRelativeTo(jFrame);
		dialog.setVisible(true);

		int status = 10;
		try {
			status = (int) pane.getValue();
		} catch (Exception e) {
			FilesUtils.copyFile(new File(Cfg.CFG_PATH + ".bak"), new File(Cfg.CFG_PATH));
		}
		if (status != 0) {
			FilesUtils.copyFile(new File(Cfg.CFG_PATH + ".bak"), new File(Cfg.CFG_PATH));
			Cfg.readCfg();
			
		} else {
			Cfg.writeCfgFile();
			Cfg.readCfg();
			R.initResources();
		}
		new File(Cfg.CFG_PATH + ".bak").delete();
	}

	/**
	 * 
	 * @param comp
	 * @return agree the user agreed and wanna proceed ?
	 */
	public static boolean showDeodexNowAlert(JComponent comp) {
		int i = 1;
		if (Cfg.doShowDeodexAlert()) {
			DeodexAlertPanel alertPane = new DeodexAlertPanel();
			JOptionPane pane = new JOptionPane(alertPane, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null);
			JDialog dialog = pane.createDialog(comp, R.getString("alert.deodex.now.title"));
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setSize(450, 200);
			dialog.setLocationRelativeTo(comp);
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

	/**
	 * @deprecated use Advanced settings instead this is here just for reference 
	 * @param com
	 */
	public static void showSettingsDialog(JComponent com) {
		SettingsPanel setings = new SettingsPanel();
		JOptionPane pane = new JOptionPane(setings, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		JDialog dialog = pane.createDialog(com, R.getString("0000052"));
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setSize(500, 220);
		dialog.setVisible(true);
		int status = 10;
		try {
			status = (int) pane.getValue();
		} catch (Exception e) {

		}
		if (status == 0) {
			Cfg.setCurrentLang((String) setings.langs.getSelectedItem());
			Cfg.setMaxJobs((int) setings.thread.getSelectedItem());
			Cfg.writeCfgFile();
		}
	}

	/**
	 * 
	 * @param comp
	 * @return dialogExitValue the exit value of the dialog
	 */
	public static int showThreadDialog(JComponent comp) {
		if (Cfg.doShowThreadAlert()) {
			ThreadAlertPanel alertPane = new ThreadAlertPanel();
			JOptionPane pane = new JOptionPane(alertPane, JOptionPane.PLAIN_MESSAGE);
			JDialog dialog = pane.createDialog(comp, R.getString("box.jobs"));
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setSize(500, 260);
			dialog.setLocationRelativeTo(comp);
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
