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
import java.util.ArrayList;

public class ArrayUtils {

	public static ArrayList<File> deletedupricates(ArrayList<File> list) {

		int[] index = getDuplicatesindex(list);
		for (int x = 0; x < index.length; x++) {
			list.remove(index[x] - x);
		}
		return list;
	}

	public static int[] getDuplicatesindex(ArrayList<File> list) {
		int index[] = new int[duplicateCount(list)];
		for (int ii = 0, y = 0; ii < list.size() - 1; ii++) {

			for (int n = ii + 1; n < list.size(); n++) {
				if (list.get(ii).getName().equals(list.get(n).getName())) {
					index[y] = ii;
					y++;
				}
			}
		}
		return index;
	}

	public static int duplicateCount(ArrayList<File> list) {
		int x = 0;
		ArrayList<File> tempList = new ArrayList<File>();

		for (int i = 0; i < list.size() - 1; i++) {

			for (int n = i + 1; n < list.size(); n++) {
				if (list.get(i).getName().equals(list.get(n).getName())) {
					tempList.add(list.get(i));
					x++;

				}
			}
		}

		return x;
	}

}
