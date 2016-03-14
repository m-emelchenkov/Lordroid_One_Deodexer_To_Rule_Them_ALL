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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.alee.laf.progressbar.WebProgressBar;

import deodex.R;
import deodex.S;
import deodex.controlers.ThreadWatcher;
import deodex.controlers.ZipalignWorker;
import deodex.tools.FilesUtils;

public class ZipalignWindow extends JFrame implements ThreadWatcher, MouseListener, MouseMotionListener {

	class BrowseAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setDialogTitle(R.getString("zipalign.fram.file.chooser.title"));
			int exitCode = fileChooser.showOpenDialog(rootPannel);
			if (exitCode == 0) {
				int apkCount = FilesUtils.searchrecursively(fileChooser.getSelectedFile(), ".apk").size();
				if (apkCount == 0) {
					logger.addLog(R.getString("log.no.apk.to.zipalign"));
					// initProgress();
				} else {
					logger.addLog(R.getString("log.there.is") + apkCount + R.getString("apk.to.be.zipaligned.log"));
					zip = new ZipalignWorker(FilesUtils.searchrecursively(fileChooser.getSelectedFile(), ".apk"), bar,
							logger);
					addWatcher();
					zipalignBtn.setEnabled(true);
				}
			}
		}

	}

	class checkAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			zipalignChk.setBackground(R.PANELS_BACK_COLOR);
			signChk.setBackground(R.PANELS_BACK_COLOR);
		}

	}

	class OkAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			logger.clearAllLogs();
			initBrowse();
		}

	}

	class ZipNowAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (!zipalignChk.isSelected() && !signChk.isSelected()) {
				signChk.setBackground(Color.RED);
				zipalignChk.setBackground(Color.RED);
				JOptionPane.showMessageDialog(rootPannel, R.getString("no.job.selected.zipalign.frame"),
						R.getString("you.shall.not.pass"), JOptionPane.ERROR_MESSAGE);
			} else {
				zip.setDoSign(signChk.isSelected());
				zip.setDoZipalign(zipalignChk.isSelected());
				new Thread(zip).start();
			}
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected int posX = 0;
	protected int posY = 0;

	ZipalignWorker zip;
	JTextField browseField = new JTextField(R.getString(S.BROWSE_FEILD));
	MyWebButton browseBtn = new MyWebButton("...");
	MyWebButton zipalignBtn = new MyWebButton(R.getString("zipalign.now.btn"));
	WebProgressBar bar = new WebProgressBar();
	JPanel rootPannel = new JPanel();
	LoggerPane logger = new LoggerPane("");
	MyWebButton okBtn = new MyWebButton("OK");
	JCheckBox zipalignChk = new JCheckBox("zipalign");
	JCheckBox signChk = new JCheckBox("sign");

	/**
	 * 
	 */
	public ZipalignWindow(Component c) {
		this.setIconImage(R.icon);
		this.setLocationRelativeTo(c);
		this.setSize(500, 300);
		this.setResizable(false);
		this.setTitle("Batch Zipalign/Sign ");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.add(rootPannel, BorderLayout.CENTER);
		// FIXME : set this undecorated and handlee the closing oprations
		// because right now when closed it get disposed but the process keeps
		// running in the background !
		// this.setUndecorated(true);
		// backcolors
		rootPannel.setBackground(R.PANELS_BACK_COLOR);
		browseBtn.setBackground(R.BUTTONS_BACK_COLOR);
		browseField.setBackground(R.FIELDS_BACK_COLOR);
		zipalignBtn.setBackground(R.BUTTONS_BACK_COLOR);
		okBtn.setBackground(R.BUTTONS_BACK_COLOR);
		zipalignChk.setBackground(R.PANELS_BACK_COLOR);
		signChk.setBackground(R.PANELS_BACK_COLOR);

		// font
		browseBtn.setFont(R.getSmallFont());
		browseField.setFont(R.getSmallFont());
		zipalignBtn.setFont(R.getCouriernormal());
		okBtn.setFont(R.getCouriernormal());
		zipalignChk.setFont(R.getCouriernormal());
		signChk.setFont(R.getCouriernormal());

		//
		bar.setBackground(Color.WHITE);
		bar.setForeground(new Color(0, 183, 92));
		bar.setFont(R.getCouriernormal());

		// actions
		browseBtn.addActionListener(new BrowseAction());
		zipalignBtn.addActionListener(new ZipNowAction());
		okBtn.addActionListener(new OkAction());
		zipalignChk.addActionListener(new checkAction());
		signChk.addActionListener(new checkAction());

		rootPannel.addMouseListener(this);
		rootPannel.addMouseMotionListener(this);
		this.setVisible(true);
		initBrowse();
	}

	private void addWatcher() {
		zip.addThreadWatcher(this);
	}

	@Override
	public void done(Runnable r) {
		this.okBtn.setEnabled(true);
		logger.saveToFile("ZIPALIGN");
	}

	private void initBrowse() {
		rootPannel.removeAll();
		rootPannel.setLayout(null);

		zipalignBtn.setEnabled(false);
		browseField.setEnabled(false);
		browseField.setEditable(false);
		zipalignChk.setSelected(true);
		signChk.setSelected(false);

		// bounds
		browseField.setBounds(5, 5, 400, 30);
		browseBtn.setBounds(405, 5, 90, 30);
		zipalignBtn.setBounds(295, 40, 200, 40);
		logger.setSize(490, 185);
		logger.repaint();
		logger.setBounds(5, 85, 490, 185);
		zipalignChk.setBounds(5, 40, 140, 40);
		signChk.setBounds(150, 40, 95, 40);

		// TOOL tip
		zipalignChk.setToolTipText(R.getString("0000055"));
		signChk.setToolTipText(R.getString("0000056"));
		browseBtn.setToolTipText(
				R.getString("0000057"));

		// add components
		rootPannel.add(browseField);
		rootPannel.add(browseBtn);
		rootPannel.add(zipalignChk);
		rootPannel.add(signChk);
		rootPannel.add(zipalignBtn);
		rootPannel.add(logger);
		rootPannel.revalidate();
		rootPannel.repaint();
	}

	private void initProgress() {
		rootPannel.removeAll();
		rootPannel.setLayout(null);

		bar.setBounds(5, 5, 490, 30);
		okBtn.setBounds(5, 40, 490, 40);
		okBtn.setEnabled(false);

		logger.setSize(490, 185);
		logger.repaint();
		logger.setBounds(5, 85, 490, 185);

		rootPannel.add(bar);
		rootPannel.add(okBtn);
		rootPannel.add(logger);

		rootPannel.revalidate();
		rootPannel.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent ev) {
		int oldX = ev.getXOnScreen();
		int oldY = ev.getYOnScreen();

		this.setLocation(this.getLocation().x + (oldX - this.posX), this.getLocation().y + (oldY - this.posY));
		this.posX = ev.getXOnScreen();
		this.posY = ev.getYOnScreen();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.posX = e.getXOnScreen();
		this.posY = e.getYOnScreen();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendFailed(Runnable r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateProgress() {
		this.initProgress();
	}
}
