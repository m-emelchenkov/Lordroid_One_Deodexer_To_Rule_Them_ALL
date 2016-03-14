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

import java.io.IOException;

/**
 * 
 * @author lord-ralf-adolf
 *
 */
public class CmdUtils {

	/**
	 * runs a command (Runtime) and returns the exit value all the streams are
	 * handled and printed to the stdio
	 * 
	 * @param cmd
	 * @return
	 */
	public static int runCommand(String[] cmd) {
		String strCmd = "running command : ";

		for (String str : cmd)
			strCmd = strCmd + str + " ";
		Logger.writLog(strCmd);

		Runtime rt = Runtime.getRuntime();
		Process proc = null;
		try {
			proc = rt.exec(cmd);

		} catch (IOException e) {
			// We clearly didn't find the needed file
			e.printStackTrace();
			return 2;
		}

		StreamReader stdInputReader = new StreamReader("stdIn", proc.getInputStream());
		StreamReader stdErrorReader = new StreamReader("stdError", proc.getErrorStream());
		stdInputReader.start();
		stdErrorReader.start();

		int exitValue = 1000;
		try {
			exitValue = proc.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (proc != null)
				proc.destroy();
		}

		Logger.writLog("It's exit value was : " + exitValue);

		proc.destroy();
		return exitValue;
	}

}
