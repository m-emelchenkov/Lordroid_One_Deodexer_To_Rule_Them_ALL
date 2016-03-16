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

import org.apache.commons.compress.archivers.ArchiveException;

import com.alee.laf.WebLookAndFeel;

public class Test {

	public static void main(String[] args) throws InterruptedException, IOException, ArchiveException {
//		File targz = new File("/tmp/test.tar.gz");
//		File tar = new File("/tmp/test.tar");
//		File outDir = new File("/tmp/out");
//		TarGzUtils.unGzip(targz, new File("/tmp"));
//		TarGzUtils.unTar(tar, outDir);
		WebLookAndFeel.install();
		R.initResources();
		//JFrame win = new JFrame();
		//win.setLocationRelativeTo(null);
//		win.setSize(650, 250);
//		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		win.add(new CheckUpdatePan());
//		win.setVisible(true);
		//Alerts.showUpdateAlertDialog(win);
		
	}
}
