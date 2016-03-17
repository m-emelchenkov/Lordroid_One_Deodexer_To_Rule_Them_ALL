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
		JLabel title = new JLabel();
		String text = 
				 R.getString("0000099")
				+R.getString("0000100")
				+R.getString("0000101")
				+R.getString("0000102")
				+R.getString("0000103")
				+R.getString("0000104")
				+R.getString("0000105")
				+R.getString("0000106")
				+R.getString("0000107")
				+R.getString("0000108")
				+R.getString("0000109")
				+R.getString("0000110")
				+R.getString("0000111");
		
		title.setText(text);
		title .setBounds(10, -20, 580, 600);
		title .setFont(R.getNormalFont());
		title .setBackground(Color.WHITE);

		this.add(title);
	}
}
