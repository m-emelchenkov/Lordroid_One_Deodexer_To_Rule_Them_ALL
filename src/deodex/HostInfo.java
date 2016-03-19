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
package deodex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import deodex.tools.Logger;

public class HostInfo {
	/**
	 * 
	 * @return NumberOfCores the number of available cpus (cores)
	 */
	public static final int availableCpus() {
		return ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
	}

	/**
	 * 
	 * @return the maxMemory
	 */
	public static long getMaxMemory() {
		return Runtime.getRuntime().maxMemory();
	}

	public static void logInfo() {
		try {
			File f = new File(Logger.LOG_FILE.getParentFile().getAbsolutePath()+"/system_info.txt");
			if (f.exists()) {
				f.delete();
			}
			f.getParentFile().mkdirs();
			BufferedWriter out = new BufferedWriter(new FileWriter(f.getAbsolutePath(), true));
			System.getProperties().store(out, "System informations ");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
