package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class Tele5 implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String token = link.split("=")[1];
		String downloadLink = "http://www.mitele.telecinco.es/services/tk.php?provider=level3&protohash=/CDN/videos/" + token.substring(token.length() - 3) + "/" + token + ".mp4";
		return HTML.HTMLSource(downloadLink);
	}

}
