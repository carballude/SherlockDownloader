package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class OndaCero implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String address = "";
		String chunk = link.split("www.ondacero.es/OndaCero/play/")[1];
		chunk = chunk.substring(2, chunk.length());
		chunk = chunk.substring(chunk.indexOf('_') + 1, chunk.length());
		String htmlSource = HTML.HTMLSource("http://www.ondacero.es/OndaCero/playermultimedia/M_" + chunk);
		if (htmlSource.contains("mms://")) {
			address = "mms://" + htmlSource.split("mms://")[1].split("\"")[0];
		} else {
			throw new InvalidLinkException();
		}
		return address;
	}

}
