/**
 * 
 */
package be.spitech.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author pis
 *
 */
public final class CollectionUtils {

	public static <T> Map<T, T> toMap(T[][] array) {
		HashMap<T, T> map = new HashMap<T, T>();
		for (int i = 0; i < array.length; i++) {
			if (array[i].length != 2)
				throw new IllegalArgumentException("can't convert array to map" +
						" - wrong array dimension : " + array.length);
			map.put(array[i][0], array[i][1]);
		}
		return Collections.unmodifiableMap(map);
	}
	
	public static <T> SortedSet<T> toSortedSet(T[] fractions) {
		TreeSet<T> set = new TreeSet<T>();
		for (int i = 0; i < fractions.length; i++) {
			set.add(fractions[i]);
		}
		return set;
	}
	
	public static <T> LinkedList<T> toLinkedList(T[] fractions) {
		LinkedList<T> list = new LinkedList<T>();
		for (int i = 0; i < fractions.length; i++) {
			if (list.contains(fractions[i])) {
				throw new IllegalArgumentException("doublon: " + fractions[i]);
			}
			list.add(fractions[i]);
		}
		return list;
	}
}
