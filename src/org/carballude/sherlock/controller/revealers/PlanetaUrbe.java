package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class PlanetaUrbe implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String[] chunks = link.split("/");
		if (chunks.length >= 3) {
			String id = chunks[chunks.length - 2];
			link = "http://media.kickstatic.com/kickapps/images/smilfiles/videos/"
					+ id + "_http.smil";
			String htmlSource = HTML.HTMLSource(link);
			if (htmlSource.contains("src=\""))
				return htmlSource.split("base=\"")[1].split("\"")[0]
						+ htmlSource.split("src=\"")[1].split("\"")[0];
		}
		throw new InvalidLinkException();
	}

}
