package org.imogene.web.gwt.client.util;

import java.util.HashSet;
import java.util.Set;

public class MiscTools {

	/**
	 * Construct a set with one item, the specified one.
	 * 
	 * @param <T>
	 *            the item type
	 * @param toAdd
	 *            the item to add
	 * @return the set.
	 */
	public static <T> Set<T> getHashSet(T toAdd) {
		Set<T> set = new HashSet<T>();
		set.add(toAdd);
		return set;

	}
}
