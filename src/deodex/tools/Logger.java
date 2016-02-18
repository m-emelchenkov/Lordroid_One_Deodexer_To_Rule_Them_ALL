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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private static final boolean LOG = false;
	public static final String INFO = "[INFO]";
	public static final String WARNNING = "[WARNNING]";
	public static final String ERROR = "[ERROR]";
	public static final String FATAL = "[FATAL]";
	public static final File LOG_FILE =  new File(getlogFileName());
	private static int init = 0;
	
	public static boolean logToStd = true;
	
	public static void logToStdIO(String str) {
		long yourmilliseconds = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]"); // dd/MMM/yyyy
		Date resultdate = new Date(yourmilliseconds);
		if (LOG)
			System.out.println(sdf.format(resultdate)+str);
	}

	public static String getlogFileName(){
		if(init == 0){
		long yourmilliseconds = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss"); // dd/MMM/yyyy
		Date resultdate = new Date(yourmilliseconds);
		init++;
		new File(PathUtils.getExcutionPath() + File.separator + "logs" + File.separator + sdf.format(resultdate)
		+ "_full.log").getParentFile().mkdirs();
		return  PathUtils.getExcutionPath() + File.separator + "logs" + File.separator + sdf.format(resultdate)
				+ "_full.log";
	}
		
		return LOG_FILE.getAbsolutePath();
	}
	
	public static synchronized void writLog(String str){
		long yourmilliseconds = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]"); // dd/MMM/yyyy
		Date resultdate = new Date(yourmilliseconds);
		if(logToStd)
		System.out.println(sdf.format(resultdate)+str);
		BufferedWriter out; 
		try {
		out= new BufferedWriter(new FileWriter(LOG_FILE.getAbsolutePath(), true));
		out.write(sdf.format(resultdate)+str);
		if(!str.endsWith("\n"));
		out.newLine();
		out.flush();
		out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
