package com.pirate3d.piratefileflusher.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.rmi.CORBA.Util;

import org.apache.log4j.Logger;

import com.pirate3d.piratefileflusher.utils.Utilities;

public class TextParser {

	private Object syncObject = new Object();
	Logger logger = Logger.getLogger(TextParser.class);
	private static TextParser map = new TextParser();
	
	public Object getSyncObject() {
		return syncObject;
	}
	
	public static TextParser getInstance(){
		return map;
	}

	public void setSyncObject(Object syncObject) {
		this.syncObject = syncObject;
	}

	public void setFileValues(String fileHash, double value) {
		synchronized (syncObject) {
			Properties prop = createPropertyFile();
			prop.setProperty("fileHash", "value");
		}
	}

	public Properties createPropertyFile() {
		Properties prop = new Properties();
		synchronized (syncObject) {
			try {
				if (!(new File(Utilities.DEFAULT_DIR + "/" + ".filecleanser").exists())) {
					prop.store(new FileOutputStream(Utilities.DEFAULT_DIR + "/" + ".filecleanser"), null);
				}
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return prop;
	}
	
	public String fetchPropertyValue(String fileName) {
		synchronized (syncObject) {
			String hashVal = "";
			try {
				Properties prop = createPropertyFile();
				prop.load(new FileInputStream(Utilities.DEFAULT_DIR + "/" + ".filecleanser"));
				hashVal = prop.getProperty(fileName);
			} catch (FileNotFoundException fileNotFound) {
				logger.error("File not found" + fileNotFound);
			} catch (Exception inputException) {
				logger.error("Input Exception" + inputException);
			}
			return hashVal;
		}
	}

	public void setPropertyValue(String fileName, double migrationValue) {
		synchronized (syncObject) {
			try {
				Properties prop = createPropertyFile();
				prop.load(new FileInputStream(Utilities.DEFAULT_DIR + "/" + ".filecleanser"));
				prop.setProperty(fileName, String.valueOf(migrationValue));
				prop.store(new FileOutputStream(Utilities.DEFAULT_DIR + "/" + ".filecleanser"), null);
			} catch (FileNotFoundException fileNotFound) {
				logger.error("File not found" + fileNotFound);
			} catch (Exception inputException) {
				logger.error("Input Exception" + inputException);
			}
		}
	}
	
	public void removePropertyValues(String fileName){
		synchronized (syncObject) {
			try {
				Properties prop = createPropertyFile();
				prop.load(new FileInputStream(Utilities.DEFAULT_DIR + "/" + ".filecleanser"));
				prop.remove(fileName);
				prop.store(new FileOutputStream(Utilities.DEFAULT_DIR + "/" + ".filecleanser"), null);
			} catch (FileNotFoundException fileNotFound) {
				logger.error("File not found" + fileNotFound);
			} catch (Exception inputException) {
				logger.error("Input Exception" + inputException);
			}
		}
	}
	
	public void checkFileFlusherProperties() {
		synchronized (syncObject) {
			Map<String, Double> migrationVal = new HashMap<String, Double>();
			
			try {
				if (new File(Utilities.DEFAULT_DIR + "/" + ".filecleanser").exists()) {
					Properties prop = new Properties();
					prop.load(new FileInputStream(Utilities.DEFAULT_DIR + "/" + ".filecleanser"));
					prop.keySet();
					Enumeration em = prop.keys();
					
					while(em.hasMoreElements()){
						String str = (String) em.nextElement();
						migrationVal.put(str, Double.parseDouble(prop.getProperty(str).toString()));
					}
					ParserMap.getInstance().setMessages(migrationVal);
				}
			} catch (Exception inputException) {
				logger.error("Input Exception" + inputException);
			}
		}
	}
	
	public void setDefaultLocationValue(String fileLocation, String portValue, String minumumSize, String maximumSize) {
		synchronized (syncObject) {
			try {
				Utilities.DEFAULT_DIR = fileLocation;
				Utilities.DEFAULT_PORT_LOCATION = Integer.parseInt(portValue);
				Utilities.MIN_THRESHOLD_LIMIT_IN_BYTES = Long.parseLong(minumumSize);
				Utilities.MAX_THRESHOLD_LIMIT_IN_BYTES = Long.parseLong(maximumSize);
			} catch (Exception inputException) {
				logger.error("Input Exception" + inputException);
			}
		}
	}

	
	public String readFileFlusherProperties(String name){
		String defaultLocation = "";
		synchronized (syncObject) {
			try {
				defaultLocation = Utilities.DEFAULT_DIR;
			} catch (Exception inputException) {
				logger.error("Directory set incorrectly" + inputException);
			}
		}
		return defaultLocation;
	}
}
