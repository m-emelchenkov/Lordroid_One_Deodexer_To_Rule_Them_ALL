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

	public static ArrayList<File> deletedupricates(ArrayList<File> l) {
	    Set<File> s = new TreeSet<File>(new Comparator<File>() {

	        @Override
	        public int compare(File o1, File o2) {
	        	//Logger.writLog("comparing "+o1.getName() +" to "+o2.getName());
	        		return o1.getName().compareTo(o2.getName()); 
	        }
	    });
	    s.addAll(l);

	    l = new ArrayList<File> (s);
	    return l;
	}
	// TODO : test this one out ! no sorting ...
//	public static List<File> deleteDupricates(List<File> list) {
//	    Set<String> seenNames = new HashSet<>();
//	    for (Iterator<File> it = list.iterator(); it.hasNext(); ) {
//	      String fileName = it.next().getName();
//	      if (!seenNames.add(fileName)) {
//	        it.remove();
//	      }
//	    }
//	    return list;
//	  }
}
