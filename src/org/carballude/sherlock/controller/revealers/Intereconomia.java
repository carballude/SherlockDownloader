package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class Intereconomia implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String source = HTML.HTMLSource(link);
		if(source.contains("\"playervideo\"") && source.contains("playlist")){
			return source.split("playlist")[1].split("url: '")[1].split("\'")[0];
		}
		throw new InvalidLinkException();
	}

}
