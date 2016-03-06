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
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamReader implements Runnable {
	String name;
	InputStream is;
	Thread thread;

	public StreamReader(String name, InputStream is) {
		this.name = name;
		this.is = is;
	}

	@Override
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			while (true) {
				String s = br.readLine();
				if (s == null)
					break;
				Logger.writLog("[" + name + "] " + s);
			}
			is.close();
		} catch (Exception ex) {
			Logger.writLog("Problem reading stream " + name + "... :" + ex);
			ex.printStackTrace();
		}
	}

	public void start() {
		thread = new Thread(this);
		thread.start();
	}
}