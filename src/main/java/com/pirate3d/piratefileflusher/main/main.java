package com.pirate3d.piratefileflusher.main;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import com.pirate3d.piratefileflusher.network.NetworkCommunication;
import com.pirate3d.piratefileflusher.parser.TextParser;
import com.pirate3d.piratefileflusher.watcher.FileUpdater;
import com.pirate3d.piratefileflusher.watcher.Watcher;

public class main {

	static Logger logger = Logger.getLogger(main.class);

	public static void main(String[] args) throws IOException {
		BasicConfigurator.configure();
		TextParser parser = new TextParser();
		boolean proceed = false;
		String defaultFolderLocation = "";
		String defaultPortLocation = "";
		String folderMinimumSize = "";
		String folderMaximumSize = "";
		try {
			if (args.length > 0) {
				defaultFolderLocation = args[0];
				defaultPortLocation = args[1];
				folderMinimumSize = args[2];
				folderMaximumSize = args[3];
				File file = new File(defaultFolderLocation);
				if (file.isDirectory()) {
					proceed = true;
				} else {
					System.out.println("Not a directory");
				}
			} else {
				System.out.println("Input parameters are incorrect");
				System.out.println("Input must be in the form of PirateFileFlusher.jar Default_Folder_Location Default_Port_Address FolderMinimumSize FolderMaximumSize");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		
		if(proceed == true)
		{
		logger.info("Default folder location is " + defaultFolderLocation);
		parser.setDefaultLocationValue(defaultFolderLocation, defaultPortLocation,folderMinimumSize,folderMaximumSize);
		TextParser.getInstance().checkFileFlusherProperties();
		
		ServerSocket listener = new ServerSocket(Integer.parseInt(defaultPortLocation));
		try {
			logger.info("Watcher initiated successfully");
			new Watcher().start();
			new FileUpdater().start();;
			while (true) {
				try {
					Thread.sleep(1000);
					new NetworkCommunication(listener.accept());
				} catch (InterruptedException e) {
					logger.error(e);
				}
			}
		} catch (IOException e) {
			logger.error("Accept failed.");
			System.exit(1);
		}
		}
	}
}
