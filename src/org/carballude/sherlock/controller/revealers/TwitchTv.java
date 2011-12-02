package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class TwitchTv implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String[] chunks = link.split("/");
		String source = HTML.HTMLSource("http://api.justin.tv/api/broadcast/show/"+ chunks[chunks.length-1] +".xml?onsite=true");
		if(!source.contains("<video_file_url>"))
			throw new InvalidLinkException();
		return source.split("<video_file_url>")[1].split("<")[0];
	}

}
