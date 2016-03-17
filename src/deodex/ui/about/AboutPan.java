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
/**
 * 
 */
package deodex.ui.about;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import deodex.R;

/**
 * @author lord-ralf-adolf
 *
 */
public class AboutPan extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AboutPan(){
		this.setSize(600,600);
		this.setBounds(0,0,600,600);
		this.setLayout(null);
		this.setBackground(Color.WHITE);
		JLabel title = new JLabel("Lordroid One Deodexer To Rule Them All");
		String text = 
				"<html><h1 color=\"#7d3c98\" bgcolor=\"#f4d03f\">Lordroid One Deodexer To Rule Them All</h1>"
				+"<h2 color=\"#f4d03f\" bgcolor=\"#a325a7\">Description :</h2>"
				+"<p>Lordroid One Deodexer To Rule Them All is a free Software writen in java ,capable of deodexing any android rom under any platform (OS) as long as that OS have JRE 7 or Higher Installed</p>"
				+"<h2 color=\"#f4d03f\" bgcolor=\"#a325a7\">License :</h2>"
				+"<p>Software redistributed under GPL V3 see license tab for more details</p>"
				+"<h2 color=\"#f4d03f =\" bgcolor=\"#a325a7\">Author :</h2>"
				+"<p>© Rachid Boudjelida rachidboudjelida@gmail.com all rights reserved</p>"
				+"<h2 color=\"#f4d03f\" bgcolor=\"#a325a7\">Translators :</h2>"
				+"<p>Droid-Angel (English),realtebo (Italian),Raphael Mangini(Portugues) , pabloc97(spanish)</p>"
				+"<h2 color=\"#f4d03f\" bgcolor=\"#a325a7\">Used Libraries :</h2>"
				+"<p>commons-compress-1.10.jar , zip4j_1.3.2.jar ,weblaf-1.28.jar , xz.jar</p>"
				+"<h2 color=\"#f4d03f\" bgcolor=\"#a325a7\">Used binaries :</h2>"
				+"<p>oat2dex.jar ,smali.jar ,baksmali.jar ,signApk.jar ,adb ,aapt ,unsquash ,7z.exe</p>"
				+"</html>";
		title.setText(text);
		title .setBounds(10, -20, 580, 600);
		title .setFont(R.getNormalFont());
		title .setBackground(Color.WHITE);

		this.add(title);
	}
}
