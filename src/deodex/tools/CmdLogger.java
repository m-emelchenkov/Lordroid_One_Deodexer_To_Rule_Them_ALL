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
package deodex.tools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import deodex.controlers.LoggerPan;

/**
 * simple implementation of the LoggerPan interface for cmd line usage
 * 
 * @author lord-ralf-adolf
 *
 */
public class CmdLogger implements LoggerPan {
	ArrayList<String> logs = new ArrayList<String>();

	@Override
	public synchronized void addLog(String str) {
		// TODO Auto-generated method stub
		synchronized (logs) {
			long yourmilliseconds = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); // dd/MMM/yyyy
			Date resultdate = new Date(yourmilliseconds);
			String str2 = sdf.format(resultdate);
			logs.add("[" + str2 + "]" + str);
			System.out.println("[" + str2 + "]" + str);
		}
	}

	@Override
	public synchronized void saveToFile() {

		String str = Logger.LOG_FILE.getParentFile().getAbsolutePath()+ "/Deodexing_log.txt";
		File logFile = new File(str);
		this.addLog("Log file saved to " + logFile.getAbsolutePath());
		PropReader.ArrayToProp(logs, logFile);
	}

}
