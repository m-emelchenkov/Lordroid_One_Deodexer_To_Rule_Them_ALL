/*
 * 
 * 
 * Copyright 2016 Rachid Boudjelida <rachidboudjelida@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package deodex.tools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import deodex.controlers.LoggerPan;

public class CmdLogger implements LoggerPan {
	ArrayList<String> logs = new ArrayList<String>();

	@Override
	public synchronized void addLog(String str) {
		// TODO Auto-generated method stub
		long yourmilliseconds = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); // dd/MMM/yyyy
		Date resultdate = new Date(yourmilliseconds);
		String str2 = sdf.format(resultdate);
		logs.add("[" + str2 + "]" + str);
		System.out.println("[" + str2 + "]" + str);
	}

	@Override
	public synchronized void saveToFile() {
		// TODO Auto-generated method stub
		long yourmilliseconds = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss"); // dd/MMM/yyyy
		Date resultdate = new Date(yourmilliseconds);
		String str = PathUtils.getExcutionPath() + File.separator + "logs" + File.separator + sdf.format(resultdate)
				+ ".log";
		File logFile = new File(str);
		this.addLog("Log file saved to " + logFile.getAbsolutePath());
		PropReader.ArrayToProp(logs, logFile);
	}

}
