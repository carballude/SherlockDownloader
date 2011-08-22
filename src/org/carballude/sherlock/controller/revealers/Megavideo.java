package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class Megavideo implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String address = "";
		String code = link.split("v=")[1].substring(0, 8);
		String xmlSource = HTML.HTMLSource("http://www.megavideo.com/xml/videolink.php?v=" + code);
		if (xmlSource.contains("hd_url=\""))
			address = xmlSource.split("hd_url=\"")[1].split("\"")[0];
		else
			throw new InvalidLinkException();
		return address.replaceAll("%3A", ":").replaceAll("%2F", "/");
	}

}
