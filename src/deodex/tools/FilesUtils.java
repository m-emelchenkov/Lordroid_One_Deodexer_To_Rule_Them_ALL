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

public class FilesUtils {

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

}
