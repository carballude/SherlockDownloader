package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class TVE implements Revealer {
	
	private boolean isTVEALaCarta(String url) {
		return url.contains("www.rtve.es") && url.contains("/alacarta/");
	}
	
	private boolean isRTVELink(String url) {
		return url.contains("www.rtve.es") && url.contains("/mediateca/audios/");
	}

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {				
		if(isTVEALaCarta(link))
		 return getTVEALaCarta(link);
		if(isRTVELink(link))
			return getRTVELink(link);
		String id = getId(link);
		String url = generateVideoXmlUrl(id);
		String source = "";
		try {
			source = HTML.HTMLSource(url);
		} catch (IOException e) {
			if (source.isEmpty()) {
				source = HTML.HTMLSource(link);
				if (source.contains("<div id=\"video"))
					return getTVEALaCarta("http://www.rtve.es/alacarta/#" + HTML.HTMLSource(link).split("<div id=\"video")[1].split("\"")[0]);
				if (source.contains("<div id=\"vid"))
					return getTVEALaCarta("http://www.rtve.es/alacarta/#" + HTML.HTMLSource(link).split("<div id=\"vid")[1].split("\"")[0]);
			}

		}
		if (xmlHasFileAddress(source))
			return lookForFileTagInXml(source);
		if (xmlHasAnAssetXmlAddress(source))
			return lookForFileWithAnAssetDataVideoXmlAddress(source);
		throw new InvalidLinkException();
	}
	
	private String getRTVELink(String rtveLink) throws InvalidLinkException, MalformedURLException, IOException {
		String id = getId(rtveLink);
		String url = generateAudioXmlUrl(id);
		String source = HTML.HTMLSource(url);
		if (xmlHasFileAddress(source))
			return lookForFileTagInXml(source);
		if (xmlHasAnAssetXmlAddress(source))
			return lookForFileWithAnAssetDataAudioXmlAddress(source);
		return null;
	}
	
	private String lookForFileWithAnAssetDataAudioXmlAddress(String source) throws MalformedURLException, IOException {
		return generateAssetMp3Url(getAssetXmlDefaultLocation(HTML.HTMLSource(generateAssetDataAudioIdXmlUrl(getAssetId(source)))));
	}
	
	private String generateAssetDataAudioIdXmlUrl(String id) {
		return "http://www.rtve.es/scd/CONTENTS/ASSET_DATA_AUDIO/" + id.charAt(5) + "/" + id.charAt(4) + "/" + id.charAt(3) + "/" + id.charAt(2) + "/ASSET_DATA_AUDIO-" + id + ".xml";
	}
	
	private String generateAssetMp3Url(String xmlDefaultLocation) {
		return "http://www.rtve.es/resources/TE_NGVA/mp3/" + xmlDefaultLocation.split("/mp3/")[1];
	}
	
	private String generateAudioXmlUrl(String id) {
		return "http://www.rtve.es/swf/data/es/audios/audio/" + id.charAt(id.length() - 1) + "/" + id.charAt(id.length() - 2) + "/" + id.charAt(id.length() - 3) + "/" + id.charAt(id.length() - 4)
				+ "/" + id + ".xml";
	}
	
	private String getTVEALaCarta(String link) throws MalformedURLException, IOException, InvalidLinkException {
		if(link.contains("/alacarta/audios/")){
			String source = HTML.HTMLSource(link);
			return source.split("<link rel=\"audio_src\" href=\"")[1].split("\"")[0];
		}
		String chunks[];
		if (link.contains("#"))
			chunks = link.split("#");
		else
			chunks = link.split("/");
		String url = generateVideoXmlUrl(chunks[chunks.length - 1]);
		String source = HTML.HTMLSource(url);
		if (xmlHasFileAddress(source))
			return lookForFileTagInXml(source);
		if (xmlHasAnAssetXmlAddress(source))
			return lookForFileWithAnAssetDataVideoXmlAddress(source);
		throw new InvalidLinkException();
	}
	
	private String getId(String link) throws InvalidLinkException {
		String[] aux = link.split("/");
		String id = aux[aux.length - 1].split("\\.")[0];
		for (int i = 0; i < id.length(); i++)
			if (!Character.isDigit(id.charAt(i)))
				throw new InvalidLinkException();
		return id;
	}
	
	private String generateVideoXmlUrl(String id) {
		return "http://www.rtve.es/swf/data/es/videos/video/" + id.charAt(id.length() - 1) + "/" + id.charAt(id.length() - 2) + "/" + id.charAt(id.length() - 3) + "/" + id.charAt(id.length() - 4)
				+ "/" + id + ".xml";
	}
	
	private boolean xmlHasFileAddress(String source) {
		return source.contains("<file>") && source.contains("</file>");
	}
	
	private String lookForFileTagInXml(String source) {
		return getFileFromXmlAddress(source);
	}
	
	private String getFileFromXmlAddress(String source) {
		if (!xmlHasFileAddress(source))
			throw new IllegalArgumentException();
		return source.split("<file>")[1].split("</file>")[0];
	}
	
	private boolean xmlHasAnAssetXmlAddress(String source) {
		return source.contains("<file/>") && source.contains("assetDataId::");
	}
	
	private String lookForFileWithAnAssetDataVideoXmlAddress(String source) throws MalformedURLException, IOException {
		String assetRelativeLocation = getAssetXmlDefaultLocation(HTML.HTMLSource(generateAssetDataVideoIdXmlUrl(getAssetId(source))));
		if (assetRelativeLocation.contains("/flv/"))
			return generateAssetDownloadLocation("flv", assetRelativeLocation);
		if (assetRelativeLocation.contains("/mp4/"))
			return generateAssetDownloadLocation("mp4", assetRelativeLocation);
		throw new IOException();
	}
	
	private String getAssetXmlDefaultLocation(String source) {
		return source.split("defaultLocation=\"")[1].split("\"")[0];
	}
	
	private String getAssetId(String source) {
		return source.split("assetDataId::")[1].substring(0, 6);
	}
	
	private String generateAssetDownloadLocation(String format, String relativeLocation) {
		return "http://www.rtve.es/resources/TE_NGVA/" + format + "/" + relativeLocation.split("/" + format + "/")[1];
	}
	
	private String generateAssetDataVideoIdXmlUrl(String id) {
		return "http://www.rtve.es/scd/CONTENTS/ASSET_DATA_VIDEO/" + id.charAt(5) + "/" + id.charAt(4) + "/" + id.charAt(3) + "/" + id.charAt(2) + "/ASSET_DATA_VIDEO-" + id + ".xml";
	}

}
