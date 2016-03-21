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

import java.io.File;
import java.io.IOException;
import java.net.URL;

import deodex.tools.FilesUtils;

public class Test {

	public static void main(String[] args) throws IOException  {
			// just for testing purpose 
		for (int i = 0 ; i< 2 ; i++){
			URL url = new URL("http://prodroid.eu5.org/lordroid/release.php");
			url.openConnection();
			System.out.println(""+i);
			FilesUtils.copyFile(url.openStream(), new File("/tmp/link"));
		}
	}
}
