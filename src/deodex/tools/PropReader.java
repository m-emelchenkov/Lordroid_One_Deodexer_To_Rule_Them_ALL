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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * 
 * @author lord-ralf-adolf
 *
 */
public class PropReader {

	/**
	 * saves a list of string to a file with UTF-8 encoding
	 * 
	 * @param lines
	 *            a list of string to be saved to the given File
	 * @param propFile
	 *            prop File to save
	 */
	public static void ArrayToProp(ArrayList<String> lines, File propFile) {
		BufferedWriter bw = null;
		propFile.delete();
		propFile.getParentFile().mkdirs();
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(propFile));
			bw = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(dos), "UTF-8"));
			for (String str : lines) {
				bw.write(str);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param prop
	 *            String property to search
	 * @param propFile
	 *            the pro file
	 * @return the property value
	 */
	public static String getProp(String prop, File propFile) {
		BufferedReader br = null;
		String value = null;

		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(propFile));
			br = new BufferedReader(
					new InputStreamReader(new BufferedInputStream(dis), Charset.forName("UTF-8").newDecoder()));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0 && line.charAt(0) != '#' && line.lastIndexOf("=") > 0) {
					String tmpProp = StringUtils.getCropString(line, line.lastIndexOf("="));
					tmpProp = StringUtils.removeSpaces(tmpProp);
					if (tmpProp.equals(prop)) {
						value = StringUtils.getSubString(line, line.lastIndexOf("="));
						dis.close();
						break;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// make sure we close the input stream
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return value;

	}

	/**
	 * 
	 * @param propFile
	 *            text file encode in UTF-8
	 * @return the lines count
	 */
	public static int linesCont(File propFile) {
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader br = null;

		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(propFile));
			br = new BufferedReader(
					new InputStreamReader(new BufferedInputStream(dis), Charset.forName("UTF-8").newDecoder()));
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines.size();
	}

	/**
	 * get an array with all the line from a given propfile
	 * 
	 * @param propFile
	 *            the propFile
	 * @return the list of all the properties
	 */
	public static ArrayList<String> propToArray(File propFile) {
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader br = null;

		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(propFile));
			br = new BufferedReader(
					new InputStreamReader(new BufferedInputStream(dis), Charset.forName("UTF-8").newDecoder()));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0 && line.charAt(0) != '#') {
					lines.add(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	/**
	 * this will write of change the current value of the given property if it
	 * was found if not found a new line will be created
	 * 
	 * @param prop
	 *            the property to write
	 * @param value
	 *            the value of the property to write
	 * @param propFile
	 *            the prop file
	 */
	public static void writeProp(String prop, String value, File propFile) {
		ArrayList<String> lines;

		if (propFile.exists()) {
			lines = propToArray(propFile);
			boolean found = false;
			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i);
				if (line.startsWith(prop)) {
					line = prop + "=" + value;
					lines.remove(i);
					lines.add(line);
					found = true;
					break;
				}
			}
			if (!found)
				lines.add(prop + "=" + value);
		} else {
			lines = new ArrayList<String>();
			lines.add(prop + "=" + value);
		}

		ArrayToProp(lines, propFile);
	}

}
