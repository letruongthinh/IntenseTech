/**
 * Created by Le Thinh
 */

package io.github.lethinh.intensetech.utils;

import java.util.Collection;
import java.util.function.Predicate;

public final class Commons {

	private Commons() {

	}

	public static int[] toUnboxedIntArray(Integer[] arr) {
		if (arr.length == 0) {
			return new int[0];
		}

		int[] ret = new int[arr.length];

		for (int i = 0; i < arr.length; ++i) {
			ret[i] = arr[i];
		}

		return ret;
	}

	public static <T> boolean addIfNotExists(Collection<T> collection, T elementToAdd) {
		return elementToAdd != null && collection != null && !collection.contains(elementToAdd)
				&& collection.add(elementToAdd);
	}

	public static <T> boolean removeIfExists(Collection<T> collection, T elementToRemove) {
		return removeIfExists(collection, elementToRemove, null);
	}

	public static <T> boolean removeIfExists(Collection<T> collection, T elementToRemove, Predicate<T> predicate) {
		return elementToRemove != null && collection != null && !collection.isEmpty()
				&& collection.contains(elementToRemove)
				&& collection.removeIf(e -> e.equals(elementToRemove) && (predicate == null || predicate.test(e)));
	}

}
