package org.carballude.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class HTML {

	public static String HTMLSource(String address) throws MalformedURLException, IOException {
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
	
}
