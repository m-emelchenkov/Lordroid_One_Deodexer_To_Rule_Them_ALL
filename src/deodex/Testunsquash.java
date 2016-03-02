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

import deodex.tools.CmdUtils;
import deodex.tools.FilesUtils;

public class Testunsquash {

	public static void main(String args[]) throws Exception{
		File sourceFile = new File("/tmp/odex.app.sqsh");
		File destFile = new File("/tmp/test");
		String cmd[] = {"unsquashfs","-no-xattrs","-f","-n","-d",destFile.getAbsolutePath(),sourceFile.getAbsolutePath()};
		CmdUtils.runCommand(cmd);
		
		boolean success = FilesUtils.copyFileRecurcively(destFile, new File("/tmp/system/app"));
		if(success)
		FilesUtils.deleteRecursively(destFile);
		
	}
}
