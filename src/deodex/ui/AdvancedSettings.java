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
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.alee.laf.list.WebListCellRenderer;
import com.alee.laf.list.WebListElement;

import deodex.Cfg;
import deodex.R;
import deodex.S;

public class AdvancedSettings extends JPanel {

	class Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (arg0.getSource().equals(fontsCombo)) {

				Cfg.setCurrentFont((String) fontsCombo.getSelectedItem());
				setFonts();

			} else if (arg0.getSource().equals(langsCombo)) {

				Cfg.setCurrentLang((String) langsCombo.getSelectedItem());

			} else if (arg0.getSource().equals(HeapsizeCombo)) {

				Cfg.setMaxHeadSize((String) HeapsizeCombo.getSelectedItem());

			} else if (arg0.getSource().equals(threadCombo)) {

				Cfg.setMaxJobs((int) threadCombo.getSelectedItem());

			} else if (arg0.getSource().equals(compMethodCombo)) {

				for (int i = 0; i < AdvancedSettings.COMPRESSION_METHODS.length; i++) {

					String method = COMPRESSION_METHODS[i];

					if (method.equals(compMethodCombo.getSelectedItem())) {

						Cfg.setCompresionMathod(i);

						break;

					}

				}

			}

		}

	}
	class WhiteYellowCellRenderer extends WebListCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			WebListElement c = (WebListElement) super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			c.setFont(new Font((String) value, Font.BOLD, 22));
			if (isSelected) {

				c.setForeground(Color.RED);

			}

			return c;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String[] COMPRESSION_METHODS = { "aapt (best compatibility)", "J4zip (smaller size)",
	"7z (if you feel unlucky)" };
	JTabbedPane p = new JTabbedPane();
	// Strings
	String tabbedTitleLang = "Launguage";

	String tabbedTitlePerf = "Performance";
	String tabbedTitleTools = "Customize tools";
	// Pannels
	JPanel langPan = new JPanel();

	JPanel performancePan = new JPanel();
	JPanel toolsPan = new JPanel();
	// JComboBoxes
	JComboBox<String> fontsCombo = new JComboBox<String>();
	public JComboBox<String> langsCombo = new JComboBox<String>();
	public JComboBox<Integer> threadCombo = new JComboBox<Integer>();

	public JComboBox<String> HeapsizeCombo = new JComboBox<String>();
	public JComboBox<String> compMethodCombo = new JComboBox<String>();
	// JLAbels
	public JLabel HeapsizeLab = new JLabel("Max Heap Size");
	public JLabel zipMethodLab = new JLabel(R.getString("0000053"));
	public JLabel langsLab = new JLabel(R.getString("0000053"));
	public JLabel threadLab = new JLabel(R.getString("0000054"));

	public JLabel fontsLab = new JLabel("Font (will be availble next launch)");

	public JLabel compresion = new JLabel("Compression Method");

	@SuppressWarnings("unchecked")
	public AdvancedSettings() {
		// this props
		this.setSize(800, 310);
		// this.setBackground(R.PANELS_BACK_COLOR);
		this.setLayout(null);
		this.setBounds(0, 0, 800, 310);

		// JComboBoxes
		// font add it
		fontsCombo.addItem("Arial");
		for (String font : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
			this.fontsCombo.addItem(font);
		fontsCombo.setSelectedItem(Cfg.getCurrentFont());

		// jobs add items
		int[] avJobs = { 1, 2, 3, 4 };
		for (int i : avJobs)
			this.threadCombo.addItem(i);
		this.threadCombo.setSelectedItem(Cfg.getMaxJobs());

		// langs add items
		for (String lang : Cfg.getAvailableLaunguages())
			this.langsCombo.addItem(lang);
		this.langsCombo.setSelectedItem(Cfg.getCurrentLang());

		// HEAP size combo
		String[] heaps = S.HEAP_SIZES;
		for (String heap : heaps)
			this.HeapsizeCombo.addItem(heap);
		this.HeapsizeCombo.setSelectedItem(Cfg.getMaxHeadSize());

		// comp method
		if (Cfg.isAaptAvailable()) {
			this.compMethodCombo.addItem(COMPRESSION_METHODS[0]);
		}
		this.compMethodCombo.addItem(COMPRESSION_METHODS[1]);
		if (Cfg.is7ZipAvailable()) {
			this.compMethodCombo.addItem(COMPRESSION_METHODS[2]);
		}
		compMethodCombo.setSelectedItem(AdvancedSettings.COMPRESSION_METHODS[Cfg.getCompresionMathod()]);

		// tool tips
		this.fontsCombo.setToolTipText(
				"<HTML><p>The <b>font</b> to be used ,change this if you like an other font </p><p>\nor if you have trouble displaying some weird chars</p></HTML>");
		this.langsCombo.setToolTipText(
				"<html><p>The Launguage to be used</p><p><b>Note:</b> </br>you need to restart the application in order for the changes to take effect</p></html>");
		this.HeapsizeCombo.setToolTipText("<HTML><p>the max heap size to be used</p><p>"
				+ "only change this if you are having OutOfMemoryExceptions</p>"
				+ "<p>The default Value is <b>512m<b> </p></HTML>");

		// cell rendrers
		this.fontsCombo.setRenderer(new WhiteYellowCellRenderer());

		// Action listeners
		this.fontsCombo.addActionListener(new Listener());
		this.langsCombo.addActionListener(new Listener());
		this.HeapsizeCombo.addActionListener(new Listener());
		this.threadCombo.addActionListener(new Listener());
		compMethodCombo.addActionListener(new Listener());
		// labels

		// bounds combos and their labels
		this.langsLab.setBounds(20, 20, 350, 40);
		this.langsCombo.setBounds(370, 20, 300, 40);

		this.fontsLab.setBounds(20, 80, 350, 40);
		this.fontsCombo.setBounds(370, 80, 300, 40);

		this.threadLab.setBounds(20, 20, 350, 40);
		this.threadCombo.setBounds(370, 20, 300, 40);

		this.HeapsizeLab.setBounds(20, 80, 350, 40);
		this.HeapsizeCombo.setBounds(370, 80, 300, 40);

		this.compresion.setBounds(20, 140, 350, 40);
		this.compMethodCombo.setBounds(370, 140, 300, 40);

		// fiead

		// tabbed Pan props

		// add components
		p.add(this.tabbedTitleLang, this.langPan);
		p.add(this.tabbedTitlePerf, this.performancePan);
		// p.add(this.tabbedTitleTools, this.toolsPan);

		// bounds
		p.setBounds(10, 10, 780, 300);

		// pannels
		// bounds
		this.langPan.setBounds(5, 5, 770, 570);
		this.performancePan.setBounds(5, 5, 770, 570);
		this.toolsPan.setBounds(5, 5, 770, 570);

		// layouts
		this.langPan.setLayout(null);
		this.performancePan.setLayout(null);
		this.toolsPan.setLayout(null);

		// add comp to lan pan
		this.langPan.add(fontsCombo);
		this.langPan.add(fontsLab);
		this.langPan.add(langsCombo);
		this.langPan.add(langsLab);

		// add comp to performance pan
		this.performancePan.add(HeapsizeCombo);
		this.performancePan.add(this.HeapsizeLab);
		this.performancePan.add(this.threadCombo);
		this.performancePan.add(threadLab);
		this.performancePan.add(compMethodCombo);
		this.performancePan.add(compresion);
		/// add components to this
		this.add(p);
		setFonts();
	}

	private void setFonts() {
		this.fontsLab.setFont(R.getNormalFont());
		this.threadLab.setFont(R.getNormalFont());
		this.langsLab.setFont(R.getNormalFont());
		this.HeapsizeLab.setFont(R.getNormalFont());
		this.fontsCombo.setFont(R.getNormalFont());
		this.threadCombo.setFont(R.getNormalFont());
		this.langsCombo.setFont(R.getNormalFont());
		this.HeapsizeCombo.setFont(R.getNormalFont());
		this.compMethodCombo.setFont(R.getNormalFont());
		this.compresion.setFont(R.getNormalFont());
		p.setFont(R.getNormalFont());
		this.repaint();

	}

}
