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

public class Logger {
	private static final boolean LOG = true;
	public static final String INFO = "[INFO]";
	public static final String WARNNING = "[WARNNING]";
	public static final String ERROR = "[ERROR]";
	public static final String FATAL = "[FATAL]";

	public static void logToStdIO(String str) {
		if (LOG)
			System.out.println(str);
	}

}
