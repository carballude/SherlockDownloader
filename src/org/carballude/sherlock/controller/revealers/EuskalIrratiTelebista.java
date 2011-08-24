package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class EuskalIrratiTelebista implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {		
		if(link.contains("bideoak/") || link.contains("videos/"))
			return downloadVideo(link);
		if(link.contains("audioak/") || link.contains("audios/"))
			return downloadAudio(link);
		throw new InvalidLinkException();
	}

	private String downloadVideo(String link) throws MalformedURLException, IOException, InvalidLinkException {
		String source = HTML.HTMLSource(link);		
		if(source.contains("<a id=\"descargaMp4\""))
			return "http://www.eitb.com"+source.split("<a id=\"descargaMp4\"")[1].split("href=\"")[1].split("\"")[0];		
		throw new InvalidLinkException();
	}

	private String downloadAudio(String link) throws MalformedURLException, IOException, InvalidLinkException {
		String source = HTML.HTMLSource(link);
		if(source.contains("<a id=\"descargaMp3\""))
			return "http://www.eitb.com"+source.split("<a id=\"descargaMp3\"")[1].split("href=\"")[1].split("\"")[0];		
		throw new InvalidLinkException();
	}

}
