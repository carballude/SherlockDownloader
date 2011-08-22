package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class GoEar implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String source = HTML.HTMLSource("http://www.goear.com/tracker758.php?f=" + link.split("/")[4]);
		return source.split("song path=\"")[1].split("\"")[0];
	}

}
