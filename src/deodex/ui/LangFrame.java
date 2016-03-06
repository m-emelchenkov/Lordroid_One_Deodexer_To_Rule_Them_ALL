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

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import deodex.Cfg;
import deodex.R;
import deodex.S;
import deodex.tools.Logger;

public class LangFrame extends JFrame {

	class LangsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Logger.logToStdIO(
					LOG_HEADER + Logger.INFO + "User choose " + (String) langs.getSelectedItem() + " As language");
			Cfg.setCurrentLang((String) langs.getSelectedItem());
			R.initResources();
			reloadStrings();
		}

	}

	class NextListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Cfg.writeCfgFile();
			disposeThis();
			@SuppressWarnings("unused")
			Window win = new Window();

		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String LOG_HEADER = "[LangFrame]";
	JLabel title = new JLabel(R.getString(S.APP_WELCOME));
	JLabel text = new JLabel(R.getString(S.APP_WELCOME_MESSAGE));
	LogoPane logo = new LogoPane();
	JComboBox<String> langs = new JComboBox<String>();

	JLabel langLab = new JLabel(R.getString(S.APP_LANG_BOX_LAB));

	MyWebButton next = new MyWebButton(R.getString(S.APP_NEXT_BTN));

	public LangFrame() {

		try {
			this.setIconImage(ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/icon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.setLocationRelativeTo(null);
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - 802) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - 400) / 2);

		this.setSize(802, 400);
		this.setTitle(R.getString(S.APP_NAME));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		// SET FONTS
		title.setFont(R.TITLE_FONT);
		text.setFont(R.NORMAL_FONT);
		title.setForeground(Color.RED);
		langLab.setFont(R.NORMAL_FONT);
		langs.setFont(R.NORMAL_FONT);
		next.setFont(R.NORMAL_FONT);
		// SET Bounds
		logo.setBounds(0, 0, LogoPane.M_WIDTH, LogoPane.M_HEIGHT);
		title.setBounds(10, 110, 790, 50);
		text.setBounds(10, 170, 800, 50);
		langLab.setBounds(10, 230, 300, 50);
		langs.setBounds(315, 230, 300, 50);
		next.setBounds(500, 300, 260, 50);
		// combo box add items
		for (String str : Cfg.getAvailableLaunguages()) {
			langs.addItem(str);
		}
		langs.setSelectedItem(S.ENGLISH);

		// add Components
		JPanel main = new JPanel();
		main.setBackground(Color.WHITE);
		main.setLayout(null);
		this.setContentPane(main);
		main.add(logo);
		main.add(title);
		main.add(text);
		main.add(langLab);
		main.add(langs);
		main.add(next);
		initActionsListeners();
		this.setVisible(true);
	}

	private void disposeThis() {
		this.dispose();

	}

	private void initActionsListeners() {
		langs.addActionListener(new LangsListener());
		next.addActionListener(new NextListener());
	}

	private void reloadStrings() {
		title.setText(R.getString(S.APP_WELCOME));
		text.setText(R.getString(S.APP_WELCOME_MESSAGE));
		langLab.setText(R.getString(S.APP_LANG_BOX_LAB));
		next.setText(R.getString(S.APP_NEXT_BTN));
		this.setTitle(R.getString(S.APP_NAME));
	}
}
