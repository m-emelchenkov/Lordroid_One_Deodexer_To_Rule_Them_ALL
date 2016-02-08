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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;

import deodex.tools.FilesUtils;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class TestXz {

	public static void main(String[] args) {

		// FileInputStream fin = new FileInputStream(new
		// File("/tmp/ImsSettings.odex.art.xz"));
		// BufferedInputStream in = new BufferedInputStream(fin);
		// FileOutputStream out = new FileOutputStream(new
		// File("/tmp/ImsSettings.odex.art"));
		// XZCompressorInputStream xzIn = new XZCompressorInputStream(in);
		// final byte[] buffer = new byte[1024];
		// int n = 0;
		// while (-1 != (n = xzIn.read(buffer))) {
		// out.write(buffer, 0, n);
		// }
		// out.close();
		// xzIn.close();
		// boolean copyStatus = FilesUtils.copyFile(new File("/tmp/a.log"), new
		// File("/tmp/blabla/blabla/hohohoho.a"));
		// System.out.println(copyStatus);
		// File file = new File("/tmp/app");
		// FilesUtils.deleteRecursively(file);

		// TODO : add a method to add multiple files as an array that way
		// because calling this like it is now will use lots resources
		// extract and compress for eatch file !
		// System.out.println(addFileToZip(new
		// File("/tmp/CalendarGoogle.apk"),new File("/tmp/Bluetooth.apk")));
		// System.out.println(addFileToZip(new File("/tmp/Bluetooth2.apk"),new
		// File("/tmp/Bluetooth.apk")));
		// System.out.println(addFileToZip(new File("/tmp/Bluetooth.odex"),new
		// File("/tmp/Bluetooth.apk")));
		System.out.println(FilesUtils.copyFileRecurcively(new File("/tmp/system"), new File("/tmp/test")));
	}

	/**
	 * adds a file to the given zip
	 * 
	 * @param fileToAdd
	 * @param zipFile
	 * @return true only and only if the file is present in the zip after adding
	 *         it !
	 */
	public static boolean addFileToZip(File fileToAdd, File zipFile) {
		try {
			File tmpFolder = unzip(zipFile);
			if (!tmpFolder.exists()) {
				return false;
			}
			FilesUtils.copyFile(fileToAdd,
					new File(tmpFolder.getAbsolutePath() + File.separator + fileToAdd.getName()));
			addFilesToZip(tmpFolder, zipFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ArchiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			return isFileinZip(fileToAdd.getName(), new ZipFile(zipFile));
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

		// return true;
	}

	/**
	 * search a filename is a zip file
	 * 
	 * @param fileName
	 * @param zipFile
	 * @return returns true is a file with the same name is in the zip file !
	 */
	private static boolean isFileinZip(String fileName, ZipFile zipFile) {
		try {
			// Initiate ZipFile object with the path/name of the zip file.
			// ZipFile zipFile = new ZipFile("/tmp/Maps.apk");

			// Get the list of file headers from the zip file
			List fileHeaderList = zipFile.getFileHeaders();

			// Loop through the file headers
			for (int i = 0; i < fileHeaderList.size(); i++) {
				FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
				// FileHeader contains all the properties of the file
				System.out.println("Name: " + fileHeader.getFileName());
				String name = fileHeader.getFileName();
				if (name.contains("/")) {
					name = name.substring(name.lastIndexOf("/"));
				}
				if (name.equals(fileName)) {
					return true;
				}
				// Various other properties are available in FileHeader. Please
				// have a look at FileHeader
				// class to see all the properties
			}

		} catch (ZipException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * unzip all files from the zipfile return the folder containing the
	 * extracted files
	 * 
	 * @param zipfile
	 * @return the folder containing the extracted files in the same path as the
	 *         zip and have the same name with no extension
	 * 
	 * @throws FileNotFoundException
	 */

	private static File unzip(File zipfile) throws FileNotFoundException {
		File path = new File(zipfile.getParentFile().getAbsolutePath() + File.separator
				+ zipfile.getName().substring(0, zipfile.getName().lastIndexOf(".")));
		path.mkdirs();
		try {
			ZipArchiveInputStream zais = new ZipArchiveInputStream(new FileInputStream(zipfile));
			ZipArchiveEntry z1 = null;
			while ((z1 = zais.getNextZipEntry()) != null) {
				String fn = z1.getName();
				if (fn.endsWith("/")) {
					fn = fn.substring(z1.getName().lastIndexOf("/"));
				}
				File f = new File(path + File.separator + fn);
				f.getParentFile().mkdirs();
				FileOutputStream fos = new FileOutputStream(f);
				BufferedOutputStream bos = new BufferedOutputStream(fos, 32768);

				int n = 0;
				byte[] content = new byte[32768];
				while (-1 != (n = zais.read(content))) {
					fos.write(content, 0, n);
				}

				bos.flush();
				bos.close();
				fos.close();
			}

			zais.close();
			zipfile.delete();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return path;
	}

	/**
	 * Add all files from the source directory to the destination zip file
	 *
	 * @param source
	 *            the directory with files to add
	 * @param destination
	 *            the zip file that should contain the files
	 * @throws IOException
	 *             if the io fails
	 * @throws ArchiveException
	 *             if creating or adding to the archive fails
	 */
	private static void addFilesToZip(File source, File destination) throws IOException, ArchiveException {
		OutputStream archiveStream = new FileOutputStream(destination);
		ArchiveOutputStream archive = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP,
				archiveStream);

		Collection<File> fileList = FileUtils.listFiles(source, null, true);

		for (File file : fileList) {
			String entryName = getEntryName(source, file);
			ZipArchiveEntry entry = new ZipArchiveEntry(entryName);
			archive.putArchiveEntry(entry);

			BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));

			IOUtils.copy(input, archive);
			input.close();
			archive.closeArchiveEntry();
		}

		archive.finish();
		archiveStream.close();
	}

	/**
	 * Remove the leading part of each entry that contains the source directory
	 * name
	 *
	 * @param source
	 *            the directory where the file entry is found
	 * @param file
	 *            the file that is about to be added
	 * @return the name of an archive entry
	 * @throws IOException
	 *             if the io fails
	 */
	private static String getEntryName(File source, File file) throws IOException {
		int index = source.getAbsolutePath().length() + 1;
		String path = file.getCanonicalPath();

		return path.substring(index);
	}
}
