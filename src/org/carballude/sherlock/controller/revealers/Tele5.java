package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class Tele5 implements Revealer {
	
	private String level3Method(String htmlSource) throws MalformedURLException, IOException {
		String id = htmlSource.split("'http://level3/")[1].split("\\.")[0];
		return "http://www.mitele.telecinco.es/deliverty/demo/resources/flv/" + id.charAt(id.length() - 1) + "/" + id.charAt(id.length() - 2) + "/" + id + ".flv";
	}
	
	private String videoViewerMethod(String link) throws MalformedURLException, IOException {
		String token = link.split("=")[1];
		String downloadLink = "http://www.mitele.telecinco.es/services/tk.php?provider=level3&protohash=/CDN/videos/" + token.substring(token.length() - 3) + "/" + token + ".mp4";
		return HTML.HTMLSource(downloadLink);
	}
	
	private String MDSMethod(String htmlSource) throws MalformedURLException, IOException, InvalidLinkException{
		String id = htmlSource.split("MDS.embedObj\\(video, \"")[1].split("\"")[0];
		String link = "http://www.telecinco.es/mdsvideo/sources.json?contentId="+id;
		htmlSource = HTML.HTMLSource(link);
		System.out.println(htmlSource);
		if(htmlSource.contains("\"src\":\""))
			return htmlSource.split("\"src\":\"")[1].split("\"")[0].replaceAll("\\\\/", "/");
		throw new InvalidLinkException();
	}

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		if(link.contains("VideoViewer.shtml?videoURL="))
			return videoViewerMethod(link);
		String htmlSource = HTML.HTMLSource(link);
		if(htmlSource.contains("MDS.embedObj(video,"))
			return MDSMethod(htmlSource);
		if(htmlSource.contains("'http://level3/"))
			return level3Method(htmlSource);
		throw new InvalidLinkException();
	}

}
