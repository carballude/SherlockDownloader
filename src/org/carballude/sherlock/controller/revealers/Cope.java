package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class Cope implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String htmlSource = HTML.HTMLSource(link);
		if(!htmlSource.contains("value=\"fluURL="))
			throw new InvalidLinkException();
		link = htmlSource.split("value=\"fluURL=")[1].split("&")[0];
		htmlSource = HTML.HTMLSource(link);
		if(!htmlSource.contains("http:"))
			throw new InvalidLinkException();
		return "http:"+htmlSource.split("http:")[1];
	}

}
