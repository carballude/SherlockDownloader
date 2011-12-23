package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class ElRellano implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String source = HTML.HTMLSource(link);
		if (!source.contains("id=\"videoplayer\""))
			throw new InvalidLinkException();
		source = source.split("id=\"videoplayer\"")[1];
		if (!source.contains("href=\""))
			throw new InvalidLinkException();
		return "http://www.elrellano.com"
				+ source.split("href=\"")[1].split("\"")[0];
	}

}
