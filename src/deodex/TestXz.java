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
package deodex;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;

import deodex.tools.FilesUtils;

public class TestXz {

	public static void main(String[] args)
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		FileInputStream fin = new FileInputStream(new File("/tmp/ImsSettings.odex.art.xz"));
		BufferedInputStream in = new BufferedInputStream(fin);
		FileOutputStream out = new FileOutputStream(new File("/tmp/ImsSettings.odex.art"));
		XZCompressorInputStream xzIn = new XZCompressorInputStream(in);
		final byte[] buffer = new byte[1024];
		int n = 0;
		while (-1 != (n = xzIn.read(buffer))) {
			out.write(buffer, 0, n);
		}
		out.close();
		xzIn.close();
		boolean copyStatus = FilesUtils.copyFile(new File("/tmp/a.log"), new File("/tmp/blabla/blabla/hohohoho.a"));
		System.out.println(copyStatus);
	}

}
