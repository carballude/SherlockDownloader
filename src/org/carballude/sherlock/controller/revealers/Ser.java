package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class Ser implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String htmlSource = HTML.HTMLSource(link);
		if(!htmlSource.contains("obj.url = '"))
			throw new InvalidLinkException();
		return htmlSource.split("obj.url = '")[1].split("'")[0];
	}

}
