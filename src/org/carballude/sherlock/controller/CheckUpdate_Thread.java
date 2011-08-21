package org.carballude.sherlock.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class CheckUpdate_Thread implements Runnable {

	public String HTMLSource(String address) throws MalformedURLException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new URL(address).openStream()));
		String source = "";
		StringBuffer buffer = new StringBuffer();
		for (String line = ""; (line = br.readLine()) != null; buffer.append(line + '\n'))
			;
		source = buffer.toString();
		String line = br.readLine();
		while (line != null) {
			line = br.readLine();
			source = source + line + "\n";
		}
		return source;
	}

	@Override
	public void run() {
		try {
			int version = Integer.parseInt(HTMLSource("http://www.carballude.es/creaciones/sherlockversion.txt").trim());
			if (version > Manager.VERSION) {
				Manager.getInstance().showUpdateAvailable();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
