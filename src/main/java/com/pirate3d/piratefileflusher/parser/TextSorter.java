package com.pirate3d.piratefileflusher.parser;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TextSorter {

	public Map<String, Double> getMigrationValues() {
		Map<String, Double> sortedVal = new HashMap<String, Double>();
		try {
			sortedVal = sortByComparator(ParserMap.getInstance().getMessages());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sortedVal;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<String, Double> sortByComparator(Map unsortMap) {

		List list = new LinkedList(unsortMap.entrySet());

		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public void storeValues(Map listOfMap) {
		List list = new LinkedList(listOfMap.entrySet());
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); 
				it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			TextParser.getInstance().setPropertyValue((String)entry.getKey(), (Double)entry.getValue());
		}
	}
}
