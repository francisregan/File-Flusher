package com.pirate3d.piratefileflusher.utils;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.log4j.Logger;

import com.pirate3d.piratefileflusher.parser.TextParser;

public class FetchDirectoryImpl {

	private static Logger logger = Logger.getLogger(FetchDirectoryImpl.class);

	public void selectAllFiles() {
		getDefaultFolder();
	}

	public final File getDefaultFolder() {
		File folder = null;
		try {
			folder = new File(Utilities.DEFAULT_DIR);
		} catch (Error e) {
			logger.equals("Directory location set is incorrect " + e);
		}
		return folder;
	}

	public File[] listFilesForFolder(final File folder) {
		File[] filelist = folder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.startsWith(".")) {
					return false;
				} else {
					return true;
				}
			}
		});
		return filelist;
	}

	public void removeFile(String filename) {
		File defaultFolder = getDefaultFolder();
		for (final File filesListed : defaultFolder.listFiles()) {
			try {
				if (filesListed.getName().equals(filename)) {
					if (filesListed.isDirectory()) {
						deleteAllFiles(filesListed);
					} else {
						boolean isDeleted = filesListed.delete();
						if (!isDeleted) {
							System.out.println(filesListed.getName()
									+ " has been deleted successfully");
						}
					}
				}
			} catch (Exception e) {
				logger.error("Exception occured ::" + e);
			}
		}
	}

	private void deleteAllFiles(File root) {
		try {
			if (root.isDirectory()) {
				for (String s : root.list()) {
					System.out.println("Deleting file " + s);
					deleteAllFiles(new File(root, s));
				}
			}

			boolean isDeleted = root.delete();
			if (!isDeleted) {
				System.out.println("Delete of file is failed : " + root.getAbsoluteFile());
			}
		} catch (Exception e) {
			logger.error("Unable to delete file" + e);
		}
	}
}
