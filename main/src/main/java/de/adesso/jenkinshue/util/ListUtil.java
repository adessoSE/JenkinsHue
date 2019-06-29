package de.adesso.jenkinshue.util;

import java.util.Collections;
import java.util.List;

public class ListUtil {

	public static <T> List<T> nullSafe(List<T> list) {
		return list == null ? Collections.emptyList() : list;
	}
}
