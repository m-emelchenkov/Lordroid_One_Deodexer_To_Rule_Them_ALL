/*
 * 
 * 
 * Copyright 2016 Rachid Boudjelida <rachidboudjelida@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package deodex.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import deodex.R;
import deodex.S;
import deodex.SessionCfg;
import deodex.controlers.LoggerPan;

public class FilesUtils {

	public static boolean copyFileRecurcively(File in, File out) {
		boolean status = true;
		if (in.isDirectory()) {
			out.mkdir();
			File[] list = in.listFiles();
			for (File f : list) {
				// if (f.isDirectory()){
				status = status
						&& copyFileRecurcively(f, new File(out.getAbsolutePath() + File.separator + f.getName()));
				// } else {
				// status = status && copyFile(f,new
				// File(out.getAbsolutePath()+File.separator+f.getName()));
				// }
			}
		} else {
			status = status && copyFile(in, out);
		}

		return out.exists() && status;
	}

	public static boolean copyFile(File input, File dest) {
		// making sure the path is there and writable !
		dest.getParentFile().mkdirs();
		// dest.delete();

		if (dest.getParentFile().exists()) { // if the parent doesn't exist then
												// don't bother copy

			try {

				InputStream is = new FileInputStream(input);
				OutputStream out = new FileOutputStream(dest);
				byte[] buffer = new byte[32768];
				int len;
				while ((len = is.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				is.close();
				out.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return false;
		}
		return dest.exists();
	}

	public static boolean moveFile(File in, File dest) {
		boolean iscopied = copyFile(in, dest);
		if (!iscopied)
			return false;
		if (dest.exists())
			in.delete();

		return !in.exists();
	}

	public static boolean copyFile(InputStream in, File dest) {
		dest.mkdirs();
		dest.delete();
		if (dest.getParentFile().exists()) {
			try {
				// InputStream is = in;
				OutputStream out = new FileOutputStream(dest);
				byte[] buffer = new byte[32768];
				int len;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				in.close();
				out.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return false;
		}
		return dest.exists();
	}

	public static boolean isAValideSystemDir(File systemFolder, LoggerPan log) {
		// File files[] = systemFolder.listFiles();

		if (!new File(systemFolder.getAbsolutePath() + File.separator + S.SYSTEM_BUILD_PROP).exists()) {
			log.addLog(R.getString(S.LOG_ERROR) + R.getString(S.LOG_NO_BUILD_PROP));
			return false;
		}
		int sdkLevel;
		try {
			sdkLevel = Integer.parseInt(PropReader.getProp(S.SDK_LEVEL_PROP,
					new File(systemFolder.getAbsolutePath() + File.separator + S.SYSTEM_BUILD_PROP)));
			// String str = PropReader.getProp(S.SDK_LEVEL_PROP, new
			// File(systemFolder.getAbsolutePath()+File.separator+S.SYSTEM_BUILD_PROP));
			// Logger.logToStdIO("[WHAT ?] "+str);
		} catch (Exception e) {
			e.printStackTrace();
			log.addLog(R.getString(S.LOG_ERROR) + R.getString(S.CANT_READ_SDK_LEVEL));
			return false;
		}

		boolean isapp = new File(systemFolder.getAbsolutePath() + File.separator + S.SYSTEM_APP).exists();
		boolean isprivApp = new File(systemFolder.getAbsolutePath() + File.separator + S.SYSTEM_PRIV_APP).exists();
		boolean isframwork = new File(systemFolder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK).exists();
		if (!isapp && !isprivApp && !isframwork) {
			log.addLog(R.getString(S.LOG_ERROR) + R.getString(S.LOG_NOT_A_SYSTEM_FOLDER));
		}
		// is there /app
		if (isapp) {
			log.addLog(R.getString(S.LOG_INFO) + R.getString(S.LOG_SYSTEM_APP_FOUND));
		} else {
			log.addLog(R.getString(S.LOG_WARNING) + R.getString(S.LOG_SYSTEM_APP_NOT_FOUND));
		}
		// is there privz app
		if (sdkLevel > 18)
			if (isprivApp) {
				log.addLog(R.getString(S.LOG_INFO) + R.getString("log.privapp.found"));
			} else {
				log.addLog(R.getString(S.LOG_WARNING) + R.getString("log.privapp.not.found"));
			}

		if (isapp) {

			log.addLog(R.getString(S.LOG_INFO) + R.getString("log.framework.found"));
		} else {
			log.addLog(R.getString(S.LOG_WARNING) + R.getString("log.framwork.not.found"));
			if (sdkLevel > 20) {
				log.addLog(R.getString(S.LOG_ERROR) + R.getString("log.framwork.not.found.error"));
				return false;
			}
		}
		String arch = getRomArch(systemFolder);
		// can we detetect arch ?
		if (arch.equals("null")) {
			log.addLog(R.getString(S.LOG_ERROR) + R.getString("log.no.arch.detected"));
			return false;
		}
		// is boot .oat there ?
		if (sdkLevel > 20) {
			if (!new File(systemFolder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK + File.separator + arch
					+ File.separator + S.SYSTEM_FRAMEWORK_BOOT).exists()) {
				log.addLog(R.getString(S.LOG_ERROR) + R.getString("log.no.boot.oat"));
				return false;
			}
		}

		// Session Settings set them
		SessionCfg.setSdk(sdkLevel);
		log.addLog(R.getString(S.LOG_INFO) + " Detected Sdk level : " + sdkLevel);
		SessionCfg.setArch(arch);
		log.addLog(R.getString(S.LOG_INFO) + " Detected ARCH : " + arch);
		SessionCfg.setSystemFolder(systemFolder);
		log.addLog(R.getString(S.LOG_INFO) + " chosen systemFolder :" + systemFolder);

		return true;
	}

	public static String getRomArch(File systemFolder) {
		File frameworkFolder = new File(systemFolder.getAbsolutePath() + File.separator + S.SYSTEM_FRAMEWORK);
		File[] list = frameworkFolder.listFiles();
		for (File f : list) {
			if (f.isDirectory()) {
				for (String str : S.ARCH) {
					if (str.equals(f.getName())) {
						return str;
					}
				}
			}
		}
		return "null";
	}

	// be very very carefull when using this ! it will delete folder and all
	// it's subfolder's and files !

	public static boolean deleteRecursively(File f) {
		boolean done = false;
		if (f.isFile()) {
			f.delete();
			return true;
		}
		if (f.isDirectory()) {
			File[] list = f.listFiles();
			if (list.length < 0) {
				return f.delete();
			} else {

				for (File file : list) {
					deleteRecursively(file);
				}

				return f.delete();
			}

		}
		return done;
	}
}
