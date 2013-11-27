package com.pirate3d.piratefileflusher.filehandler;

import java.io.File;

import org.apache.log4j.Logger;

import com.pirate3d.piratefileflusher.parser.ParserMap;
import com.pirate3d.piratefileflusher.parser.TextParser;
import com.pirate3d.piratefileflusher.utils.FetchDirectoryImpl;

public class MigrationCalculator {
	
	private static double FILE_AGING_VALUE = 0.9;
	private double migrationValue;
	private TextParser parser = new TextParser();
	private static Logger logger = Logger.getLogger(MigrationCalculator.class);
		
	public double firstTimeCalculator(double fileSize, double averageValue, double iterationValue){
		migrationValue = (averageValue/fileSize) * Math.pow(FILE_AGING_VALUE,iterationValue);
		return migrationValue;
	}

	public double updateAgingValue(double fileSize, double oldValue, double averageValue, double iterationValue){
		migrationValue = oldValue + ((averageValue/fileSize) * Math.pow(FILE_AGING_VALUE,iterationValue));
		return migrationValue;
	}
	
	public double retainOldValue(double oldValue, double iterationValue){
		migrationValue = oldValue * Math.pow(FILE_AGING_VALUE,iterationValue);
		return migrationValue;
	}
	
	public void calculateAgingAlgorithm(String fileName, double fileSize, double averageValue, double iterationValue, boolean fileSeeded){
		double calculatedValue;
		
	try{
		if(ParserMap.getInstance().getMessage(fileName) == null)
		{
			calculatedValue = firstTimeCalculator(fileSize, averageValue, iterationValue);
			ParserMap.getInstance().addMessage(fileName, calculatedValue);
		}
		else if((ParserMap.getInstance().getMessage(fileName) != null) && (fileSeeded == true))
		{
			calculatedValue = updateAgingValue(fileSize, ParserMap.getInstance().getMessage(fileName), averageValue, iterationValue);
			ParserMap.getInstance().addMessage(fileName, calculatedValue);
		}
		else if((ParserMap.getInstance().getMessage(fileName) != null) && (fileSeeded == false))
		{
			calculatedValue = retainOldValue(ParserMap.getInstance().getMessage(fileName), iterationValue);
			ParserMap.getInstance().addMessage(fileName, calculatedValue);
		}
		}catch(Exception e){
			logger.error("Calculation error " + e);
		}
	}
	
	public void calculateAgingAlgorithm1(String fileName, double fileSize, double averageValue, double iterationValue, boolean fileSeeded){
		double calculatedValue;
		
		try{
		if(parser.fetchPropertyValue(fileName) == null){
			calculatedValue = firstTimeCalculator(fileSize, averageValue, iterationValue);
			parser.setPropertyValue(fileName, calculatedValue);
		}else if((parser.fetchPropertyValue(fileName) != null) && (fileSeeded == true)){
			calculatedValue = updateAgingValue(fileSize, Double.valueOf(parser.fetchPropertyValue(fileName)), averageValue, iterationValue);
			parser.setPropertyValue(fileName, calculatedValue);
		}else if((parser.fetchPropertyValue(fileName) != null) && (fileSeeded == false)){
			calculatedValue = retainOldValue(Double.valueOf(parser.fetchPropertyValue(fileName)), iterationValue);
			parser.setPropertyValue(fileName, calculatedValue);
		}
		}catch(Exception e){
			logger.error("Calculation error " + e);
		}
	}
	
	public void updateFileInformation(String fileName, double iterationValue) {
		double averageFileSize;
		DownloadCalculator calc = new DownloadCalculator();
		FetchDirectoryImpl impl = new FetchDirectoryImpl();
		try {
			averageFileSize = calc.calculateAverageDownloaded();
			File[] listOfFiles = impl.listFilesForFolder(impl.getDefaultFolder());
			if (checkFileNameExists(listOfFiles,fileName)) {
				for (File file : listOfFiles) {
//					System.out.println(file.getName() + " - " + calc.getFileSize(file.getName()) + " - " + averageFileSize);
					if (file.getName().equals(fileName)) {
						calculateAgingAlgorithm(fileName, calc.getFileSize(fileName), averageFileSize, iterationValue, true);
					} else {
						calculateAgingAlgorithm(file.getName(), calc.getFileSize(file.getName()), averageFileSize, iterationValue, false);
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Calculation error " + ex);
		}
	}
	
	public Boolean checkFileNameExists(File[] listOfFiles,String fileName ){
		Boolean val = null;
		for (File file : listOfFiles) {
			if((file.getName()).equals(fileName)){
				val = true;
			}
		}
		System.out.println(val);
		return val;
	}
}
