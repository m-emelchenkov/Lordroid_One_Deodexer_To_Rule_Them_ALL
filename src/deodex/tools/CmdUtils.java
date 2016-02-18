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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CmdUtils {

	public static void runCommand(String[] cmd)  {
		String strCmd = "running command : ";
		
		for (String str : cmd)
			strCmd = strCmd+str+" ";
		Logger.writLog(strCmd);
		
		Runtime rt = Runtime.getRuntime();
		Process proc = null;
		try {
			proc = rt.exec(cmd);
			proc.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedReader stdInput = new BufferedReader(new 
		     InputStreamReader(proc.getInputStream()));

		BufferedReader stdError = new BufferedReader(new 
		     InputStreamReader(proc.getErrorStream()));

		// read the output from the command
		ArrayList<String> outPut = new ArrayList<String>();
		outPut.add("it's Out put was :");
		String s = null;
		try {
			while ((s = stdInput.readLine()) != null) {
				outPut.add(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// read any errors from the attempted command
		ArrayList<String> errors = new ArrayList<String>();
		errors.add("Errors thrown by this command are(if any ) : ");
		try {
			while ((s = stdError.readLine()) != null) {
				errors.add(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// lets log the cmdoutput
		for(String str : outPut){
			Logger.writLog(str);
		}
		if(errors.size()>1)
		for(String str : errors){
			Logger.writLog(str);
		}
		proc.destroy();

	}

}
