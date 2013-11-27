package com.pirate3d.piratefileflusher.parser;

import java.util.HashMap;
import java.util.Map;

import com.pirate3d.piratefileflusher.utils.Utilities;

public class ParserMap {

	private static Map<String, Double> mapVal = new HashMap<String, Double>();
	private static ParserMap map = new ParserMap();
	private static Boolean val = true;
	
	private Object syncObject = new Object();
	
	private ParserMap(){
	}
	
	public static ParserMap getInstance(){
		return map;
	}
	
	public boolean checkSwitch(){
		synchronized (syncObject) {
			if(val.equals(true))
			{
				return false;
			}else{
				return true;
			}
		}
	}
	
	public void addMessage(String fileName, Double migrationValue) {
		synchronized (syncObject) {
			try {
				System.out.println(fileName + " - " + migrationValue);
				System.out.println("Data updated " + Utilities.DATA_UPDATED);
				mapVal.put(fileName, migrationValue);
				if (Utilities.DATA_UPDATED == 0) {
					Utilities.DATA_UPDATED = 1;
				}
			} catch (Exception ex) {
				ex.toString();
			}
		}
	}
	
	public Map<String, Double> getMessages(){
		synchronized (syncObject) {
			return mapVal;
		}
	}
	
	public void setMessages(Map<String, Double> messageSet) {
		synchronized (syncObject) {
			try {
				mapVal.putAll(messageSet);
			} catch (Exception e) {
				e.toString();
			}
		}
	}
		
	public Double getMessage(String fileName) {
		Double value = null;
		synchronized (syncObject) {
			try {
				value = mapVal.get(fileName);
			} catch (Exception ex) {
				ex.toString();
			}
		}
		return value;
	}
	
	public void deleteMessage(String fileName) {
		synchronized (syncObject) {
			try {
				mapVal.remove(fileName);
			} catch (Exception e) {
				e.toString();
			}
		}
	}
}
