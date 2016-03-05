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
import java.io.IOException;
import java.net.URISyntaxException;

public class PathUtils {
	public static String getExcutionPath() {
		String path = "";
		try {
			path = PathUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Somthing went wrong couldn't detemine our current location !");
		}
		return path.substring(0, path.lastIndexOf("/"));
	}

	public static void logCallingProcessLocation() {
		File f = new File("testFile11");

		try {
			f.createNewFile();
			f.delete();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Logger.writLog("[PathUtils][EX]  " + e1.getStackTrace());
		}
		String calledfrom = f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator));
		Logger.writLog("[PathUtils][I] we were called from " + calledfrom);
		Logger.writLog("[PathUtils][I] we are located in : " + getExcutionPath());
	}
}
