package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class CanalPlus implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String htmlSource = HTML.HTMLSource(link);
		if(!htmlSource.contains("http://vmedia.canalplus.es"))
			throw new InvalidLinkException();
		return "http://vmedia.canalplus.es"+htmlSource.split("http://vmedia.canalplus.es")[1].split("\"")[0];		
	}

}
