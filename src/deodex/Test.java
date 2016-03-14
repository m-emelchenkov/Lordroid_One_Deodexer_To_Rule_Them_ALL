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
package deodex;

import java.io.IOException;

import com.alee.laf.WebLookAndFeel;

import deodex.ui.Alerts;

public class Test {

	public static void main(String[] args) throws InterruptedException, IOException {
		// String[] cmd = {"aapt","v"};
		// System.out.println(CmdUtils.runCommand(cmd));
		// URL url = new URL("https://goo.gl/KdVKWi");
		// URLConnection connection = url.openConnection();
		// System.out.println(connection.getContentLengthLong());
		// InputStream is = connection.getInputStream();
		// FilesUtils.copyFile(is, new File("/tmp/a1.tar.gz"));
		System.out.println(Cfg.isAaptAvailable());
		// if(cmd != null){
		// Thread.sleep(100);
		// return;
		// }
		WebLookAndFeel.install();
		// String fonts[] =
		// GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		//
		// for ( int i = 0; i < fonts.length; i++ )
		// {
		// System.out.println(fonts[i]);
		// }
		// String[] cmd = {"aapt"};
		// System.out.println(CmdUtils.runCommand(cmd));
		Cfg.readCfg();
		R.initResources();
		Cfg.writeCfgFile();
		System.out.println("7z ? " + Cfg.is7ZipAvailable());
		// JFrame win = new JFrame();
		// win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// win.setSize(820, 350);
		// win.setLayout(null);
		// AdvancedSettings setings = new AdvancedSettings();
		// win.add(setings);
		// win.setVisible(true);
		Alerts.showAdvancedSettingsDialog(null);
	}
}
