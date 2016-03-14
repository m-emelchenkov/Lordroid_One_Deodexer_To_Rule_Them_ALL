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
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.alee.laf.list.WebListCellRenderer;
import com.alee.laf.list.WebListElement;

import deodex.R;
import deodex.S;
import deodex.controlers.LoggerPan;
import deodex.tools.PathUtils;
import deodex.tools.PropReader;

public class LoggerPane extends JPanel implements LoggerPan {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image bg;
	JList<String> logs = new JList<String>();
	DefaultListModel<String> model = new DefaultListModel<String>();
	JScrollPane scroll;

	@SuppressWarnings("unchecked")
	public LoggerPane() {
		super();
		this.setSize(798, 300);
		try {
			this.bg = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/logger_back.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logs = new JList<String>(model);
		logs.setFont(R.getCourierLogger());
		logs.setAutoscrolls(true);
		logs.setCellRenderer(new WhiteYellowCellRenderer());
		this.setLayout(null);
		scroll = new JScrollPane(logs);
		scroll.setBounds(0, 0, this.getWidth(), this.getHeight() - 0);
		this.add(scroll);

	}

	public LoggerPane(String type) {
		super();
		this.setSize(490, 185);
		try {
			this.bg = ImageIO
					.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("images/logger_back.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logs = new JList<String>(model);
		logs.setFont(R.getCourierLogger());
		logs.setAutoscrolls(true);
		this.setLayout(null);
		scroll = new JScrollPane(logs);
		scroll.setBounds(0, 0, this.getWidth(), this.getHeight() - 0);
		this.add(scroll);

	}

	@Override
	public synchronized void addLog(String str) {
		try {
			addLogR(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// multiple threads can log here !
	private synchronized void addLogR(String str) throws Exception {
		synchronized (model) {
			synchronized (scroll) {
				synchronized (logs) {
					long yourmilliseconds = System.currentTimeMillis();
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); // dd/MMM/yyyy
					Date resultdate = new Date(yourmilliseconds);
					String str2 = sdf.format(resultdate);
					model.addElement("[" + str2 + "]" + str);
					scroll.getViewport().setViewPosition(logs.indexToLocation(model.size() - 1));
				}
			}
		}

		this.repaint();
	}

	public void clearAllLogs() {
		model.removeAllElements();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bg, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	@Override
	public synchronized void saveToFile() {
		long yourmilliseconds = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss"); // dd/MMM/yyyy
		Date resultdate = new Date(yourmilliseconds);
		String str = PathUtils.getExcutionPath() + File.separator + "logs" + File.separator + sdf.format(resultdate)
				+ ".log";
		File logFile = new File(str);

		ArrayList<String> logs = new ArrayList<String>();

		for (int i = 0; i < model.size(); i++) {
			logs.add(model.getElementAt(i));
		}
		PropReader.ArrayToProp(logs, logFile);
		this.repaint();
	}

	public synchronized void saveToFile(String string) {
		long yourmilliseconds = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss"); // dd/MMM/yyyy
		Date resultdate = new Date(yourmilliseconds);
		String str = PathUtils.getExcutionPath() + File.separator + "logs" + File.separator + sdf.format(resultdate)
				+ "_" + string + "_.log";
		File logFile = new File(str);

		ArrayList<String> logs = new ArrayList<String>();

		for (int i = 0; i < model.size(); i++) {
			logs.add(model.getElementAt(i));
		}
		PropReader.ArrayToProp(logs, logFile);
		this.repaint();
	}
    class WhiteYellowCellRenderer extends WebListCellRenderer {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent( @SuppressWarnings("rawtypes") JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
        	WebListElement c =(WebListElement) super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
        	if ( ((String)value).contains(R.getString(S.LOG_ERROR)) ) {
            	c.setForeground(new Color(169, 50, 38));
            } else if (((String)value).contains(R.getString(S.LOG_WARNING))) {
            	c.setForeground(new Color( 241, 196, 15));
            } else if (((String)value).contains("[SUCCESS]")){
            	c.setForeground(new Color(20, 90, 50));
            } 
            return c;
        }
    }
     
}
