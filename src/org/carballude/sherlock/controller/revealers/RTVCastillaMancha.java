package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class RTVCastillaMancha implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		if(!link.contains("detail.php?id="))
			throw new InvalidLinkException();
		String htmlSource = HTML.HTMLSource(link);
		if(!htmlSource.contains("ShowPreviewMM("))
			throw new InvalidLinkException();
		link = "http://www.rtvcm.es/mm.php?id="+htmlSource.split("ShowPreviewMM\\(")[1].split("\\)")[0];
		htmlSource = HTML.HTMLSource(link);
		if(!htmlSource.contains("param name=\"src\" value=\""))
			throw new InvalidLinkException();
		return htmlSource.split("param name=\"src\" value=\"")[1].split("\"")[0];
	}

}
