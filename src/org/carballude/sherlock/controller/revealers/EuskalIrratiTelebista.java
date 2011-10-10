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
		else if(source.contains("insertar_player_video(")){
			String info = source.split("insertar_player_video\\(")[1].split("\\)")[0];
			String urlInfo = "http://www.eitb.com/es/get/multimedia/video_json/id/" + info.split(",")[0] + "/size/" + (Integer.parseInt(info.split(",")[1].trim())>400?"grande":"dest_2") + "/f_mod/" + info.split(",")[4] + "/";
			String aux = HTML.HTMLSource(urlInfo);
			if(aux.contains("FILE_MP4")) {
				String url = aux.split("FILE_MP4\":\"")[1].split("\"")[0];				
				return removeSlashes(url);
			} else if (aux.contains("FILE_URL")){
				String url = aux.split("FILE_URL\":\"")[1].split("\"")[0];				
				return removeSlashes(url);				
			}
		}
		throw new InvalidLinkException();
	}
	
	private String removeSlashes(String url){
		String removed = "";
		for(int i = 0; i<url.length();i++)
			if(url.charAt(i)!='\\')
			removed+=url.charAt(i);
		return removed;
	}

	private String downloadAudio(String link) throws MalformedURLException, IOException, InvalidLinkException {
		String source = HTML.HTMLSource(link);
		if(source.contains("<a id=\"descargaMp3\""))
			return "http://www.eitb.com"+source.split("<a id=\"descargaMp3\"")[1].split("href=\"")[1].split("\"")[0];		
		throw new InvalidLinkException();
	}

}
