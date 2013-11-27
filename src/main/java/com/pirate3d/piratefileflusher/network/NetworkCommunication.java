package com.pirate3d.piratefileflusher.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.apache.log4j.Logger;
import com.pirate3d.piratefileflusher.filehandler.MigrationCalculator;

public class NetworkCommunication extends Thread {
	private Socket sSocket;

	static Logger logger = Logger.getLogger(NetworkCommunication.class);
	public NetworkCommunication(Socket socket) throws IOException {
		this.sSocket = socket;
		start();
	}

	public void run() {
		MigrationCalculator migrationCalculator = new MigrationCalculator();
		BufferedReader in = null;
		String input = null;
		try {
			in = new BufferedReader(new InputStreamReader(sSocket.getInputStream()));
			while ((input = in.readLine()) != null) {
				Thread.sleep(500);
				String[] arr = input.split(",");
				migrationCalculator.updateFileInformation(arr[0],Double.parseDouble(arr[1]));
			}
			in.close();
			sSocket.close();
		} catch (IOException e) {
			logger.error(e);
		} catch (InterruptedException e) {
			logger.error(e);;
		}
	}
}
