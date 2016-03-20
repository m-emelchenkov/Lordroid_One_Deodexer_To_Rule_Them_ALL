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
import java.util.ArrayList;

public class FailTracker {
	static BufferedWriter out;
	public static int failCount = 0;
	public static ArrayList<String> failedFiles = new ArrayList<String>();

	public static synchronized void addFailed(File f) {
		synchronized (failedFiles) {
			failedFiles.add(f.getName());
			failCount = failedFiles.size();
			append(f.getName());
		}
	}

	public static synchronized void append(String str) {

		try {
			out = new BufferedWriter(new FileWriter(Logger.LOG_FILE.getParentFile() + "/failed_to_deodex.txt", true));
			out.write(str);
			if (!str.endsWith("\n"))
				out.newLine();

			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
