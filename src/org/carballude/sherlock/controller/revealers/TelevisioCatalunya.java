package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class TelevisioCatalunya implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		String videoID = getVideoID(link);
		String url = "http://www.tv3.cat/su/tvc/tvcConditionalAccess.jsp?ID=" + videoID + "&QUALITY=H&FORMAT=MP4";
		String xml = HTML.HTMLSource(url);
		if(xml.contains("<media videoname=")){
			String video = xml.split("<media videoname=")[1].split(">")[1].split("<")[0];
			return "http://mp4-medium-dwn.media.tv3.cat/"+video.split(":")[2].split("\\?")[0];
		}
		throw new InvalidLinkException();
	}
	
	private String getVideoID(String link) throws InvalidLinkException {
		if(link.contains("www.tv3.cat/videos/"))
			return link.split("www.tv3.cat/videos/")[1].split("/")[0];
		else if(link.contains("www.tv3.cat/3alacarta/")){
			String[] aux = link.split("/");
			return aux[aux.length-1];
		} else if(link.contains("www.3xl.cat/videos/"))
			return link.split("www.3xl.cat/videos/")[1].split("/")[0];
		throw new InvalidLinkException();
	}

}
