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

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import deodex.Cfg;
import deodex.R;

public class SettingsPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JComboBox<String> langs = new JComboBox<String>();
	public JComboBox<Integer> thread = new JComboBox<Integer>();
	public JLabel langsLab = new JLabel(R.getString("0000053"));
	public JLabel threadLab = new JLabel(R.getString("0000054"));
	
	public SettingsPanel(){
		this.setSize(500, 200);
		this.setBounds(0, 0, 500, 200);
		this.setLayout(null);
		langsLab.setBounds(10, 10, 300, 40);
		langs.setBounds(315, 10, 120, 40);
		for (String str : Cfg.getAvailableLaunguages()){
			langs.addItem(str);
		}
		langs.setSelectedItem(Cfg.getCurrentLang());
		int[] ints = {1,2,3,4};
		threadLab.setBounds(10, 70, 300, 40);
		thread.setBounds(315, 70, 120, 40);
		for (int i : ints){
			thread.addItem(i);
		}
		thread.setSelectedItem(Cfg.getMaxJobs());
		this.add(thread);
		this.add(threadLab);
		this.add(langs);
		this.add(langsLab);
	}
}
