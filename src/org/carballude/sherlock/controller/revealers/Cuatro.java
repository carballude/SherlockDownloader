package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class Cuatro implements Revealer {
	
	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String htmlSource = HTML.HTMLSource(link);
		if (htmlSource.contains("flashvars.xref = \"")) {
			String xmlAddress = "http://www.cuatro.com/cuavideo/info.xml?xref="
					+ htmlSource.split("flashvars.xref = \"")[1].split("\"")[0];
			htmlSource = HTML.HTMLSource(xmlAddress);
			if (htmlSource.contains("http://ondemand.cuatro")) {
				return "http://ondemand.cuatro"
						+ htmlSource.split("http://ondemand.cuatro")[1]
								.split("]")[0];
			} else
				throw new InvalidLinkException();
		}
		throw new InvalidLinkException();
	}

}
