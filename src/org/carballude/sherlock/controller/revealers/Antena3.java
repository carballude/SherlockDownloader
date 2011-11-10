package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class Antena3 implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String address = "";
		try {
			String htmlSource = HTML.HTMLSource(link);
			if (htmlSource.contains("addVariable")) {
				String xmlUrl = "http://www.antena3.com" + htmlSource.split("addVariable\\(\"xml\",\"")[1].split("\"")[0];
				String xmlSource = HTML.HTMLSource(xmlUrl);
				String prefix = xmlSource.split("<urlHttpVideo><!\\[CDATA\\[")[1].split("\\]\\]></urlHttpVideo>")[0];
				address = prefix + xmlSource.split("<archivo><!\\[CDATA\\[")[1].split("\\]\\]></archivo>")[0];
			} else if (htmlSource.contains("player_capitulo.xml='")) {
				String xmlSource = HTML.HTMLSource("http://www.antena3.com" + htmlSource.split("player_capitulo.xml='")[1].split("'")[0]);
				address = "http://desprogresiva.antena3.com/" + xmlSource.split("<archivo><!\\[CDATA\\[")[1].split("\\]")[0];
			}
			//address = address.replace("001.mp4", "000.mp4"); <-- This does not work anymore :(
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new InvalidLinkException();
		}
		return address;
	}

}
