package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class BarcelonaTV implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String address = "";
		String htmlSource = HTML.HTMLSource(link);
		if (htmlSource.contains("videoBTV.playlist.add(\""))
			address = htmlSource.split("videoBTV.playlist.add\\(\"")[1].split("\"")[0];
		else
			throw new InvalidLinkException();
		return address;
	}

}
