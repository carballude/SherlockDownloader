package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class OndaCero implements Revealer {
	
	private String audiosOnlineMethod(String link) throws MalformedURLException, IOException, InvalidLinkException{
		System.out.println("1");
		String htmlSource = HTML.HTMLSource(link);
		if(!htmlSource.contains("player_live.addVariable(\"xml\",\""))
			throw new InvalidLinkException();
		link = "http://www.ondacero.es" + htmlSource.split("player_live.addVariable\\(\"xml\",\"")[1].split("\"")[0];
		System.out.println(link);
		htmlSource = HTML.HTMLSource(link);
		if(!htmlSource.contains("<archivo><![CDATA["))
			throw new InvalidLinkException();
		return "http://www.ondacero.es/"+ htmlSource.split("<archivo><!\\[CDATA\\[")[1].split("]")[0];
	}

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
		if(link.contains("www.ondacero.es/audios-online"))
			return audiosOnlineMethod(link);
		String address = "";
		String chunk = link.split("www.ondacero.es/OndaCero/play/")[1];
		chunk = chunk.substring(2, chunk.length());
		chunk = chunk.substring(chunk.indexOf('_') + 1, chunk.length());
		String htmlSource = HTML.HTMLSource("http://www.ondacero.es/OndaCero/playermultimedia/M_" + chunk);
		if (htmlSource.contains("mms://")) {
			address = "mms://" + htmlSource.split("mms://")[1].split("\"")[0];
		} else {
			throw new InvalidLinkException();
		}
		return address;
	}

}
