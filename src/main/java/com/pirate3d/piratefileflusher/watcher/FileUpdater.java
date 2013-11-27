package com.pirate3d.piratefileflusher.watcher;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.pirate3d.piratefileflusher.parser.ParserMap;
import com.pirate3d.piratefileflusher.parser.TextSorter;
import com.pirate3d.piratefileflusher.utils.Utilities;

public class FileUpdater extends Thread{
	
	public void storeFilesInLocation() {

		Map<String, Double> mapOfFiles = null;
		TextSorter sorter = null;

		if (Utilities.DATA_UPDATED == 1) {
			System.out.println("Storing files to the properties file");
			sorter = new TextSorter();
			mapOfFiles = ParserMap.getInstance().getMessages();
			sorter.storeValues(mapOfFiles);
			Utilities.DATA_UPDATED = 0;
			System.out.println("Data resetted");
		}
	}
	
	@Override
	public void run() {

		while (true) {
			try {
				storeFilesInLocation();
				TimeUnit.MINUTES.sleep(1);
//				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
