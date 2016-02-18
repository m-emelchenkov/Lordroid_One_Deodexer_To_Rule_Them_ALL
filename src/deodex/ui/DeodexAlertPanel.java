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

import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import deodex.R;

public class DeodexAlertPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel textLab = new JLabel(R.getString("alert.deodexNow"));
	JLabel textLab1 = new JLabel(R.getString("alert.deodexNow1"));
	JLabel textLab2 = new JLabel(R.getString("alert.deodexNow.areyousure"));
	public JCheckBox box = new JCheckBox(R.getString("dont.show.this.again"));
	Font font = R.SMALL_FONT;

	public DeodexAlertPanel() {
		super();
		this.setLayout(null);
		super.setSize(310, 500);
		textLab.setBounds(5, 5, 380, 20);
		textLab.setFont(font);
		textLab1.setBounds(5, 25, 380, 20);
		textLab1.setFont(font);
		textLab2.setBounds(5, 45, 380, 20);
		textLab2.setFont(font);
		box.setBounds(5, 70, 380, 20);
		box.setFont(font);
		this.setBounds(0, 0, 380, 500);
		this.add(textLab);
		this.add(textLab1);
		this.add(textLab2);
		this.add(box);

	}

}
