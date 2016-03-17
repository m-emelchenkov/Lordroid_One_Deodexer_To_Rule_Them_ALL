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


import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import deodex.R;

/**
 * @author lord-ralf-adolf
 *
 */
public class LicensePan extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JEditorPane text ;
	public LicensePan(){
		this.setSize(600,600);
		this.setLayout(new BorderLayout());
		this.setBackground(R.PANELS_BACK_COLOR);
		this.setBounds(0, 0, 600, 600);
		URL url = Thread.currentThread().getContextClassLoader().getResource("license.html");
		try {
			text = new JEditorPane(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		text.setEditable(false);
		this.add(new JScrollPane(text),BorderLayout.CENTER);

	}
}
