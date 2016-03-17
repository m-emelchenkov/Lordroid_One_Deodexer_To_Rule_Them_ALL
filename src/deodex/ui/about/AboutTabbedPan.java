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
package deodex.ui.about;

import javax.swing.JTabbedPane;

public class AboutTabbedPan extends JTabbedPane{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AboutTabbedPan(){
		super(JTabbedPane.LEFT);
		this.setSize(770, 700);
		this.setBounds(0,0,770,700);
		this.addTab("About", new AboutPan());
		this.addTab("Links", new LinksPan());
		this.addTab("License", new LicensePan());
		
	}
	

}
