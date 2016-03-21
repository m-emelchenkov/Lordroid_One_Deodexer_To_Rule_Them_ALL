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
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class ArrayUtils {

	/**
	 * remove all duplicate file names from the list with 
	 * full path doesn't matter only the name matters
	 * @param filesList a list of files
	 * @return filesListWithNoFileNaleDuplicates
	 */
	public static ArrayList<File> deletedupricates(ArrayList<File> filesList) {
	    Set<File> s = new TreeSet<File>(new Comparator<File>() {

	        @Override
	        public int compare(File o1, File o2) {
	        		return o1.getName().compareTo(o2.getName()); 
	        }
	    });
	    s.addAll(filesList);

	    filesList = new ArrayList<File> (s);
	    return filesList;
	}

}
