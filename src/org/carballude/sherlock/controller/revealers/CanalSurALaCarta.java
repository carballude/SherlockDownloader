package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class CanalSurALaCarta implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String address = "";
		String htmlSource = HTML.HTMLSource(link);
		if (htmlSource.contains("_url_xml_datos=")) {
			String xmlSource = HTML.HTMLSource(htmlSource.split("_url_xml_datos=")[1].split("\"")[0]);
			if (xmlSource.contains("<url>http://ondemand.rtva") && xmlSource.contains("</url>")) {
				address = xmlSource.split("<url>http://ondemand.rtva")[1].split("</url>")[0];
			} else
				throw new InvalidLinkException();
		} else {
			throw new InvalidLinkException();
		}
		return address;
	}

}
