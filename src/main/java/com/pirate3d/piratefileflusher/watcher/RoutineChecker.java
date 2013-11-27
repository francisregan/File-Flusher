package com.pirate3d.piratefileflusher.watcher;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;

import com.pirate3d.piratefileflusher.filehandler.MigrationCalculator;
import com.pirate3d.piratefileflusher.parser.TextSorter;
import com.pirate3d.piratefileflusher.utils.FetchDirectoryImpl;

public class RoutineChecker {
	private static Logger logger = Logger.getLogger(RoutineChecker.class);

	public void checkFolder() {
		FetchDirectoryImpl impl = new FetchDirectoryImpl();
		MigrationCalculator calc = new MigrationCalculator();
		TextSorter sort = new TextSorter();
		File[] filelist = null;
		File file = null;
		ArrayList<String> fileInFolder = null;
		ArrayList<String> calculatedFileInfo = null;

		try {
			file = impl.getDefaultFolder();
			filelist = file.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.startsWith(".")) {
						return false;
					} else {
						return true;
					}
				}
			});
			
			fileInFolder = convertToList(filelist);
			calculatedFileInfo = getHashmapKeys(sort.getMigrationValues());
			System.out.println("File in folder " + fileInFolder.size());
			System.out.println("Calculated file size in folder " + calculatedFileInfo.size());
		} catch (Exception e) {
			logger.error("File list is empty" + e);
		}
		
		try {
			if (fileInFolder.size() > calculatedFileInfo.size() && (calculatedFileInfo.size() > 0) ) {
				for (String fileExisting : fileInFolder) {
					if (!calculatedFileInfo.contains(fileExisting)) {
						calc.updateFileInformation(fileExisting,1.0);
					}
				}
			} else if((calculatedFileInfo.size() < 1) && (fileInFolder.size() > 0)){
					System.out.println(fileInFolder.get(0));
					calc.updateFileInformation(fileInFolder.get(0),1.0);
					
			} else if((fileInFolder.size() > 0) && (calculatedFileInfo.size() == 0)){
				calc.updateFileInformation(fileInFolder.get(0),1.0);
			}
		} catch (Exception e) {
			logger.error("Exception in calculating size " + e);
		}
	}

	@SuppressWarnings("null")
	public ArrayList<String> convertToList(File[] arrayOfFiles) {
		ArrayList<String> listOfFiles = new ArrayList<String>();
		try {
			if (!(arrayOfFiles == null) || (arrayOfFiles.length == 0)) {
				for (int i = 0; i < arrayOfFiles.length; i++) {
					listOfFiles.add(arrayOfFiles[i].getName());
				}
			}
		} catch (NullPointerException nullException) {
			logger.error(nullException);
		} catch (IllegalArgumentException illegalArgument) {
			logger.error(illegalArgument);
		}
		return listOfFiles;
	}

	public ArrayList<String> getHashmapKeys(Map<String, Double> keyList) {
		ArrayList<String> listOfKeys = new ArrayList<String>();
		try {
			if (!(keyList.isEmpty())) {
				for (Map.Entry<String, Double> entry : keyList.entrySet()) {
					listOfKeys.add(entry.getKey());
				}
			}
		} catch (NullPointerException nullException) {
			logger.error(nullException);
		} catch (IllegalArgumentException illegalArgument) {
			logger.error(illegalArgument);
		}
		return listOfKeys;
	}
}
