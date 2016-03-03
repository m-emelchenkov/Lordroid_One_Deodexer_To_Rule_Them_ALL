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
import java.util.ArrayList;

import deodex.Cfg;
import deodex.S;

public class UnsquashUtils {

	public static boolean unsquash(File systemFolder){
		File appSquash = new File(systemFolder.getAbsolutePath()+File.separator+"odex.app.sqsh");
		File privAppSquash = new File(systemFolder.getAbsolutePath()+File.separator+"odex.priv-app.sqsh");
		File destFile = new File(S.TMP+File.separator+"unsquash");
		// get the commands 
		String[] cmd1 = getUnsquashCommand(appSquash,destFile);
		String[] cmd2 = getUnsquashCommand(privAppSquash,destFile);
		if(appSquash.exists()){
			// unsquash app 
			boolean sucess = CmdUtils.runCommand(cmd1)==0;
			if(sucess){
				ArrayList<File> files = FilesUtils.listAllFiles(destFile);
				if(!destFile.exists() || files == null || files.size() == 0){
					return false;
				}else {
					boolean copied = FilesUtils.copyFileRecurcively(destFile, new File(systemFolder.getAbsolutePath()+File.separator+"app"));
					FilesUtils.deleteRecursively(destFile);
					if(!copied)
						return false;
				}
			}
			
		}
		
		if (privAppSquash.exists()){
			// unsquash priv-app 
			boolean sucess = CmdUtils.runCommand(cmd2)==0;
			if(sucess){
				ArrayList<File> files = FilesUtils.listAllFiles(destFile);
				if(!destFile.exists() || files == null || files.size() == 0){
					return false;
				}else {
					boolean copied = FilesUtils.copyFileRecurcively(destFile, new File(systemFolder.getAbsolutePath()+File.separator+"priv-app"));
					FilesUtils.deleteRecursively(destFile);
					if(!copied)
						return false;
				}
			}
		}
		
		return true;
	}
	
	private static String[] getUnsquashCommand(File squashFile,File dest){
		String[] cmd =null;
		String os = Cfg.getOs();
		if(os.equals(S.WINDOWS)){
			String cmdwin[] = {S.UNSQUASH_WIN.getAbsolutePath(),"-no-xattrs","-f","-n","-d",dest.getAbsolutePath(),squashFile.getAbsolutePath()};
			cmd = cmdwin;
		} else if(os.equals(S.LINUX)){
			String cmdlinux[] = {"unsquashfs","-no-xattrs","-f","-n","-d",dest.getAbsolutePath(),squashFile.getAbsolutePath()};
			cmd = cmdlinux;
		} else if(os.equals(S.MAC)){
			// FIXME : test on MAC
			String cmdOsx[] = {"unsquashfs","-no-xattrs","-f","-n","-d",dest.getAbsolutePath(),squashFile.getAbsolutePath()};
			cmd = cmdOsx;
		}
		return cmd;
	}
	
	public static boolean haveUnsquash(){
		String[] cmd =null;
		String os = Cfg.getOs();
		if(os.equals(S.WINDOWS)){
			String cmdwin[] = {S.UNSQUASH_WIN.getAbsolutePath(),"-h"};
			cmd = cmdwin;
		} else if(os.equals(S.LINUX)){
			String cmdlinux[] = {"unsquashfs","-h"};
			cmd = cmdlinux;
		} else if(os.equals(S.MAC)){
			String cmdOsx[] = {"unsquashfs","-h"};
			cmd = cmdOsx;
		}
		int exitValue = CmdUtils.runCommand(cmd);
		return exitValue == 0 || exitValue == 1;
	}
}
