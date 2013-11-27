package com.pirate3d.piratefileflusher.watcher;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.pirate3d.piratefileflusher.filehandler.DownloadCalculator;
import com.pirate3d.piratefileflusher.parser.ParserMap;
import com.pirate3d.piratefileflusher.parser.TextParser;
import com.pirate3d.piratefileflusher.parser.TextSorter;
import com.pirate3d.piratefileflusher.utils.FetchDirectoryImpl;
import com.pirate3d.piratefileflusher.utils.Utilities;


public class Watcher extends Thread {
	private Boolean checkUpdates = true;
	private FetchDirectoryImpl impl;
	private DownloadCalculator downloadCalc;
	private static Logger logger = Logger.getLogger(Watcher.class);
	
	@Override
	public void run() {
		TextSorter sort = new TextSorter();
		RoutineChecker checker = new RoutineChecker();
		impl = new FetchDirectoryImpl();
		downloadCalc = new DownloadCalculator();
		
		while (checkUpdates) {
			try {
				TimeUnit.MINUTES.sleep(15);
//				Thread.sleep(5000);
				logger.info(" Checking folder for new files ");
				Map<String, Double> getSortedVal = sort.getMigrationValues();
				checker.checkFolder();
				if ( getFileSize() >= Utilities.MAX_THRESHOLD_LIMIT_IN_BYTES ) {
					for (Map.Entry<String, Double> entry : getSortedVal.entrySet()) {
						logger.info(entry.getKey() + "/"	+ entry.getValue());
							removeEntries(entry.getKey());
							ParserMap.getInstance().deleteMessage(entry.getKey());
							TextParser.getInstance().removePropertyValues(entry.getKey());
							if ( getFileSize() <= Utilities.MIN_THRESHOLD_LIMIT_IN_BYTES ){
								logger.info("Folder space cleared - " + System.currentTimeMillis());
								break;
							}else{
								logger.info("Continue to clear files");	
							}
					}
				}
			} catch (InterruptedException e) {
				logger.error("Watcher Error " + e);
			}
		}
	}

	public void removeEntries(String fileName) {
		impl = new FetchDirectoryImpl();
		impl.removeFile(fileName);
	}
	
	public long getFileSize(){
		impl = new FetchDirectoryImpl();
		downloadCalc = new DownloadCalculator();
		return downloadCalc.getFileSizeInFolder(impl.getDefaultFolder());
	}
}