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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import deodex.R;
import deodex.tools.DesktopUtils;

/**
 * @author lord-ralf-adolf
 *
 */
public class LinksPan extends JPanel {
	private static final String[] URLS = {"http://goo.gl/Rp7e7v","https://goo.gl/RCWR4l",
			"https://github.com/lord-ralf-adolf/Lordroid_One_Deodexer_To_Rule_Them_ALL/issues/new",
			"http://forum.xda-developers.com/devdb/project/?id=13851#bugReporter",
			"http://forum.xda-developers.com/devdb/project/?id=13851#reviews",
			"http://forum.xda-developers.com/devdb/project/?id=13851#featureRequests",
			"https://github.com/lord-ralf-adolf/Lordroid_One_Deodexer_To_Rule_Them_ALL/releases/latest",
			"mailto:rachidboudjelida@gmail.com"};
	ArrayList<JButton> buttons = new ArrayList<JButton>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LinksPan(){
		this.setSize(600,600);
		this.setLayout(null);
		JPanel container = new JPanel();
		container.setBounds(40, 40, 520, 490);
		GridLayout layout =new GridLayout(8,0);
		layout.setHgap(20);
		layout.setVgap(20);
		container.setLayout(layout);
		JButton xdaThread = new JButton("Visite XDA Thread");
		JButton github = new JButton("See sources (Github)");
		JButton reportBug = new JButton("Report issue (Github)");
		JButton reportBugXda = new JButton("Report issue (XDA)");
		JButton writeReview = new JButton("Write a review (XDA)");
		JButton requesteFeature = new JButton("Post feature request (XDA)");
		JButton githubRelease = new JButton("Open latest release page");
		JButton eMailDev = new JButton("Send and email to the dev");
		buttons.add(xdaThread);
		buttons.add(github);
		buttons.add(reportBug);
		buttons.add(reportBugXda);
		buttons.add(writeReview);
		buttons.add(requesteFeature);
		buttons.add(githubRelease);
		buttons.add(eMailDev);

		for (JButton b : buttons){
			b.setFont(R.getNormalFont());
			b.setFocusable(false);
			b.addActionListener(new Actions());
			container.add(b);
		}
		
		this.add(container);
	}
	
	class Actions implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			for (int i = 0 ; i< buttons.size();i++){
				if(buttons.get(i).equals(arg0.getSource())){
					try {
						DesktopUtils.openWebpage(new URL(URLS[i]));
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}
}
