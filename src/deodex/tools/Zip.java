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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import deodex.Cfg;
import deodex.S;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class Zip {

	/**
	 * 
	 * @param zipFile
	 * @param files
	 * @return Success
	 * @throws IOException
	 */
	public static boolean addFilesToExistingZip(File zipFile, ArrayList<File> files) throws IOException {
		// get a temp file
		File tempFile = File.createTempFile(zipFile.getName(), null);
		// delete it, otherwise you cannot rename your existing zip to it.
		tempFile.delete();

		boolean renameOk = zipFile.renameTo(tempFile);
		if (!renameOk) {
			throw new RuntimeException(
					"could not rename the file " + zipFile.getAbsolutePath() + " to " + tempFile.getAbsolutePath());
		}
		byte[] buf = new byte[1024];

		ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

		ZipEntry entry = zin.getNextEntry();
		while (entry != null) {
			String name = entry.getName();
			boolean notInFiles = true;
			for (File f : files) {
				if (f.getName().equals(name)) {
					notInFiles = false;
					break;
				}
			}
			if (notInFiles) {
				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(name));
				// Transfer bytes from the ZIP file to the output file
				int len;
				while ((len = zin.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
			}
			entry = zin.getNextEntry();
		}
		// Close the streams
		zin.close();
		// Compress the files
		for (int i = 0; i < files.size(); i++) {
			InputStream in = new FileInputStream(files.get(i));
			// Add ZIP entry to output stream.
			out.putNextEntry(new ZipEntry(files.get(i).getName()));
			// Transfer bytes from the file to the ZIP file
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			// Complete the entry
			out.closeEntry();
			in.close();
		}
		// Complete the ZIP file
		out.close();
		tempFile.delete();
		boolean success = true;

		for (File f : files) {
			try {
				success = success && ZipTools.isFileinZip(f.getName(), new ZipFile(zipFile));
			} catch (ZipException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				success = false;
			}
		}
		tempFile.delete();

		return success;
	}

	public static boolean zipAlignAPk(File in, File out) throws IOException, InterruptedException {
		if (out.exists()) {
			return true;
		}
		String[] cmd = { new File(S.ZIPALIGN_BIN + File.separator + Cfg.getOs()).getAbsolutePath(), "4",
				in.getAbsolutePath(), out.getAbsolutePath() };
		Process p;
		p = Runtime.getRuntime().exec(cmd);

		p.waitFor();

		return out.exists();
	}
}
