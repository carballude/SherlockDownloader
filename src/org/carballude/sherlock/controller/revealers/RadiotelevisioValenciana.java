package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class RadiotelevisioValenciana implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException, IOException, InvalidLinkException {
		String address = "";
		String htmlSource = HTML.HTMLSource(link);
		if (htmlSource.contains("file: \"")) {
			String xmlSource = HTML.HTMLSource("http://www.rtvv.es" + htmlSource.split("file: \"")[1].split("\"")[0]);
			if (xmlSource.contains("<media:content url=\""))
				address = xmlSource.split("<media:content url=\"")[1].split("\"")[0];
			else
				throw new InvalidLinkException();
		} else if (htmlSource.contains("this.element.jPlayer(\"setFile\"")) {
			address = "http://www.rtvv.es" + htmlSource.split("this.element.jPlayer\\(\"setFile\"\\, \"")[1].split("\"")[0];
		} else
			throw new InvalidLinkException();
		return address;
	}

}
