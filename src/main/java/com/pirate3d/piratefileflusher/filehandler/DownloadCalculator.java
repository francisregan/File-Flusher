package com.pirate3d.piratefileflusher.filehandler;

import java.io.File;
import java.io.FilenameFilter;

import com.pirate3d.piratefileflusher.utils.FetchDirectoryImpl;

public class DownloadCalculator {

	private FetchDirectoryImpl impl;

	public double getFileSize(String fileName) {
		impl = new FetchDirectoryImpl();
		double value = 0;
		try{
		File downloadsFolder = impl.getDefaultFolder();
		for (File downloadedFile : downloadsFolder.listFiles()) {
			if(downloadedFile.getName().equals(fileName)) {
				if(downloadedFile.isDirectory()){
				value = getFileSizeInFolder(downloadedFile);
				}else{
				value = downloadedFile.length();
			}
		}
		}
		}catch(Exception ex){
			ex.toString();
		}
		return value;
	}
	
	public double calculateAverageDownloaded() throws Exception{
		impl = new FetchDirectoryImpl();
		File downloadsFolder = impl.getDefaultFolder();
		System.out.println("Total file size in folder " + getFileSizeInFolder(downloadsFolder));
		System.out.println("Total files in folder " + calculateTotalFilesDownloaded());
		return getFileSizeInFolder(downloadsFolder) / calculateTotalFilesDownloaded();
	}

	public long getFileSizeInFolder(File folder) {
		long foldersize = 0;
		try{
			File[] filelist = folder.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.startsWith(".")) {
						return false;
					} else {
						return true;
					}
				}
			});
		for (int i = 0; i < filelist.length; i++) {
			if (filelist[i].isDirectory()) {
				foldersize += getFileSizeInFolder(filelist[i]);
			} else {
				foldersize += filelist[i].length();
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return foldersize;
	}
	
	@SuppressWarnings("unused")
	public int calculateTotalFilesDownloaded() {
		impl = new FetchDirectoryImpl();
		File downloadsFolder = impl.getDefaultFolder();
		int sizeCount = 0;

		File[] filelist = downloadsFolder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.startsWith(".")) {
					return false;
				} else {
					return true;
				}
			}
		});

		for (File downloadedFile : filelist) {
			sizeCount = sizeCount + 1;
		}
		return sizeCount;
	}
}
