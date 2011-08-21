package org.carballude.sherlock.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.sherlock.model.LinkRevealedEvent;

public class LinkRevealer implements Runnable {

	private String originalUrl;

	public LinkRevealer(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	private String getId(String link) throws InvalidLinkException {
		String[] aux = link.split("/");
		String id = aux[aux.length - 1].split("\\.")[0];
		for (int i = 0; i < id.length(); i++)
			if (!Character.isDigit(id.charAt(i)))
				throw new InvalidLinkException();
		return id;
	}

	private String getAssetId(String source) {
		return source.split("assetDataId::")[1].substring(0, 6);
	}

	private boolean xmlHasFileAddress(String source) {
		return source.contains("<file>") && source.contains("</file>");
	}

	private boolean xmlHasAnAssetXmlAddress(String source) {
		return source.contains("<file/>") && source.contains("assetDataId::");
	}

	private String getFileFromXmlAddress(String source) {
		if (!xmlHasFileAddress(source))
			throw new IllegalArgumentException();
		return source.split("<file>")[1].split("</file>")[0];
	}

	private String getAssetXmlDefaultLocation(String source) {
		return source.split("defaultLocation=\"")[1].split("\"")[0];
	}

	private String generateAssetDownloadLocation(String format, String relativeLocation) {
		return "http://www.rtve.es/resources/TE_NGVA/" + format + "/" + relativeLocation.split("/" + format + "/")[1];
	}

	private String generateAssetMp3Url(String xmlDefaultLocation) {
		return "http://www.rtve.es/resources/TE_NGVA/mp3/" + xmlDefaultLocation.split("/mp3/")[1];
	}

	private String generateVideoXmlUrl(String id) {
		return "http://www.rtve.es/swf/data/es/videos/video/" + id.charAt(id.length() - 1) + "/" + id.charAt(id.length() - 2) + "/" + id.charAt(id.length() - 3) + "/" + id.charAt(id.length() - 4)
				+ "/" + id + ".xml";
	}

	private String generateAudioXmlUrl(String id) {
		return "http://www.rtve.es/swf/data/es/audios/audio/" + id.charAt(id.length() - 1) + "/" + id.charAt(id.length() - 2) + "/" + id.charAt(id.length() - 3) + "/" + id.charAt(id.length() - 4)
				+ "/" + id + ".xml";
	}

	private String generateAssetDataVideoIdXmlUrl(String id) {
		return "http://www.rtve.es/scd/CONTENTS/ASSET_DATA_VIDEO/" + id.charAt(5) + "/" + id.charAt(4) + "/" + id.charAt(3) + "/" + id.charAt(2) + "/ASSET_DATA_VIDEO-" + id + ".xml";
	}

	private String generateAssetDataAudioIdXmlUrl(String id) {
		return "http://www.rtve.es/scd/CONTENTS/ASSET_DATA_AUDIO/" + id.charAt(5) + "/" + id.charAt(4) + "/" + id.charAt(3) + "/" + id.charAt(2) + "/ASSET_DATA_AUDIO-" + id + ".xml";
	}

	private String lookForFileTagInXml(String source) {
		return getFileFromXmlAddress(source);
	}

	private String lookForFileWithAnAssetDataVideoXmlAddress(String source) throws MalformedURLException, IOException {
		String assetRelativeLocation = getAssetXmlDefaultLocation(HTMLSource(generateAssetDataVideoIdXmlUrl(getAssetId(source))));
		if (assetRelativeLocation.contains("/flv/"))
			return generateAssetDownloadLocation("flv", assetRelativeLocation);
		if (assetRelativeLocation.contains("/mp4/"))
			return generateAssetDownloadLocation("mp4", assetRelativeLocation);
		throw new IOException();
	}

	private String lookForFileWithAnAssetDataAudioXmlAddress(String source) throws MalformedURLException, IOException {
		return generateAssetMp3Url(getAssetXmlDefaultLocation(HTMLSource(generateAssetDataAudioIdXmlUrl(getAssetId(source)))));
	}

	private boolean isTVELink(String url) {
		return url.contains("www.rtve.es");
	}

	private boolean isTVEALaCarta(String url) {
		return url.contains("www.rtve.es") && url.contains("/alacarta/");
	}

	private boolean isRTVELink(String url) {
		return url.contains("www.rtve.es") && url.contains("/mediateca/audios/");
	}

	private boolean isTele5Link(String url) {
		return url.contains("www.telecinco.es/") && url.contains("VideoViewer.shtml?videoURL=");
	}

	private boolean isMiTele5Link(String url) {
		return url.contains("www.mitele.telecinco.es");
	}

	private boolean isGoEarLink(String url) {
		return url.contains("www.goear.com");
	}

	private boolean isAntena3Link(String url) {
		return url.contains("www.antena3.com/videos/");
	}

	private boolean isMegavideoLink(String url) {
		return url.contains("www.megavideo.com") && url.contains("v=");
	}

	private boolean isBTVLink(String url) {
		return url.contains("www.btv.cat/alacarta/");
	}

	private boolean isOndaCeroLink(String url) {
		return url.contains("www.ondacero.es/OndaCero/play/");
	}

	private boolean isCanalSurALaCarta(String url) {
		return url.contains("www.canalsuralacarta.es");
	}

	private boolean isRadiotelevisioValenciana(String url) {
		return url.contains("www.rtvv.es/va/");
	}

	private String getTVELink(String rtveLink) throws InvalidLinkException, MalformedURLException, IOException {
		String id = getId(rtveLink);
		String url = generateVideoXmlUrl(id);
		String source = "";
		try {
			source = HTMLSource(url);
		} catch (IOException e) {
			if (source.isEmpty()) {
				source = HTMLSource(rtveLink);
				if (source.contains("<div id=\"video"))
					return getTVEALaCarta("http://www.rtve.es/alacarta/#" + HTMLSource(rtveLink).split("<div id=\"video")[1].split("\"")[0]);
				if (source.contains("<div id=\"vid"))
					return getTVEALaCarta("http://www.rtve.es/alacarta/#" + HTMLSource(rtveLink).split("<div id=\"vid")[1].split("\"")[0]);
			}

		}
		if (xmlHasFileAddress(source))
			return lookForFileTagInXml(source);
		if (xmlHasAnAssetXmlAddress(source))
			return lookForFileWithAnAssetDataVideoXmlAddress(source);
		throw new InvalidLinkException();
	}

	private String getTVEALaCarta(String link) throws MalformedURLException, IOException, InvalidLinkException {
		String chunks[];
		if (link.contains("#"))
			chunks = link.split("#");
		else
			chunks = link.split("/");
		String url = generateVideoXmlUrl(chunks[chunks.length - 1]);
		String source = HTMLSource(url);
		if (xmlHasFileAddress(source))
			return lookForFileTagInXml(source);
		if (xmlHasAnAssetXmlAddress(source))
			return lookForFileWithAnAssetDataVideoXmlAddress(source);
		throw new InvalidLinkException();
	}

	private String getRTVELink(String rtveLink) throws InvalidLinkException, MalformedURLException, IOException {
		String id = getId(rtveLink);
		String url = generateAudioXmlUrl(id);
		String source = HTMLSource(url);
		if (xmlHasFileAddress(source))
			return lookForFileTagInXml(source);
		if (xmlHasAnAssetXmlAddress(source))
			return lookForFileWithAnAssetDataAudioXmlAddress(source);
		return null;
	}

	private String getMiTele5Link(String tele5Link) throws InvalidLinkException, MalformedURLException, IOException {
		String source = HTMLSource(tele5Link);
		String id = source.split("'http://level3/")[1].split("\\.")[0];
		return "http://www.mitele.telecinco.es/deliverty/demo/resources/flv/" + id.charAt(id.length() - 1) + "/" + id.charAt(id.length() - 2) + "/" + id + ".flv";
	}

	private String getTele5Link(String tele5Link) throws InvalidLinkException, MalformedURLException, IOException {
		String token = tele5Link.split("=")[1];
		String link = "http://www.mitele.telecinco.es/services/tk.php?provider=level3&protohash=/CDN/videos/" + token.substring(token.length() - 3) + "/" + token + ".mp4";
		return HTMLSource(link);
	}

	private String getGoEarLink(String goEarLink) throws MalformedURLException, IOException {
		String source = HTMLSource("http://www.goear.com/tracker758.php?f=" + goEarLink.split("/")[4]);
		return source.split("song path=\"")[1].split("\"")[0];
	}

	private String getAntena3Link(String antena3Link) throws MalformedURLException, IOException, InvalidLinkException {
		String address = "";
		try {
			String htmlSource = HTMLSource(antena3Link);
			if (htmlSource.contains("addVariable")) {
				String xmlUrl = "http://www.antena3.com" + htmlSource.split("addVariable\\(\"xml\",\"")[1].split("\"")[0];
				String xmlSource = HTMLSource(xmlUrl);
				String prefix = xmlSource.split("<urlHttpVideo><!\\[CDATA\\[")[1].split("\\]\\]></urlHttpVideo>")[0];
				address = prefix + xmlSource.split("<archivo><!\\[CDATA\\[")[1].split("\\]\\]></archivo>")[0];
			} else if (htmlSource.contains("player_capitulo.xml='")) {
				String xmlSource = HTMLSource("http://www.antena3.com" + htmlSource.split("player_capitulo.xml='")[1].split("'")[0]);
				address = "http://desprogresiva.antena3.com/" + xmlSource.split("<archivo><!\\[CDATA\\[")[1].split("\\]")[0];
			}
			address = address.replace("001.mp4", "000.mp4");
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new InvalidLinkException();
		}
		return address;
	}

	private String getMegavideoLink(String megavideoLink) throws MalformedURLException, IOException, InvalidLinkException {
		String address = "";
		String code = megavideoLink.split("v=")[1].substring(0, 8);
		String xmlSource = HTMLSource("http://www.megavideo.com/xml/videolink.php?v=" + code);
		if (xmlSource.contains("hd_url=\""))
			address = xmlSource.split("hd_url=\"")[1].split("\"")[0];
		else
			throw new InvalidLinkException();
		return address.replaceAll("%3A", ":").replaceAll("%2F", "/");
	}

	private String getBTVLink(String BTVLink) throws MalformedURLException, IOException, InvalidLinkException {
		String address = "";
		String htmlSource = HTMLSource(BTVLink);
		if (htmlSource.contains("videoBTV.playlist.add(\""))
			address = htmlSource.split("videoBTV.playlist.add\\(\"")[1].split("\"")[0];
		else
			throw new InvalidLinkException();
		return address;
	}

	private String getOndaCeroLink(String ondaCeroLink) throws MalformedURLException, IOException, InvalidLinkException {
		String address = "";
		String chunk = ondaCeroLink.split("www.ondacero.es/OndaCero/play/")[1];
		chunk = chunk.substring(2, chunk.length());
		chunk = chunk.substring(chunk.indexOf('_') + 1, chunk.length());
		String htmlSource = HTMLSource("http://www.ondacero.es/OndaCero/playermultimedia/M_" + chunk);
		if (htmlSource.contains("mms://")) {
			address = "mms://" + htmlSource.split("mms://")[1].split("\"")[0];
		} else {
			throw new InvalidLinkException();
		}
		return address;
	}

	private String getCanalSurALaCarta(String canalSurALaCartaLink) throws MalformedURLException, IOException, InvalidLinkException {
		String address = "";
		String htmlSource = HTMLSource(canalSurALaCartaLink);
		if (htmlSource.contains("_url_xml_datos=")) {
			String xmlSource = HTMLSource(htmlSource.split("_url_xml_datos=")[1].split("\"")[0]);
			if (xmlSource.contains("<url>") && xmlSource.contains("</url>")) {
				address = xmlSource.split("<url>")[1].split("</url>")[0];
			} else
				throw new InvalidLinkException();
		} else {
			throw new InvalidLinkException();
		}
		return address;
	}

	private String getRadiotelevisioValenciana(String rtvvLink) throws MalformedURLException, IOException, InvalidLinkException {
		String address = "";
		String htmlSource = HTMLSource(rtvvLink);
		if (htmlSource.contains("file: \"")) {
			String xmlSource = HTMLSource("http://www.rtvv.es" + htmlSource.split("file: \"")[1].split("\"")[0]);
			if (xmlSource.contains("<media:content url=\""))
				address = xmlSource.split("<media:content url=\"")[1].split("\"")[0];
			else
				throw new InvalidLinkException();
		} else if (htmlSource.contains("this.element.jPlayer(\"setFile\"")) {
			address = "http://www.rtvv.es" + htmlSource.split("this.element.jPlayer\\(\"setFile\"\\, \"")[1].split("\"")[0];
		} else
			throw new InvalidLinkException();
		return address;
	}

	public String getLink(String link) throws InvalidLinkException {
		String downloadLink = null;
		if (!(link.startsWith("http://") || link.startsWith("https://"))) {
			link = "http://" + link;
		}
		try {
			if (isTVEALaCarta(link))
				downloadLink = getTVEALaCarta(link);
			else if (isRTVELink(link))
				downloadLink = getRTVELink(link);
			else if (isTVELink(link))
				downloadLink = getTVELink(link);
			else if (isMiTele5Link(link))
				downloadLink = getMiTele5Link(link);
			else if (isTele5Link(link))
				downloadLink = getTele5Link(link);
			else if (isGoEarLink(link))
				downloadLink = getGoEarLink(link);
			else if (isAntena3Link(link))
				downloadLink = getAntena3Link(link);
			else if (isMegavideoLink(link))
				downloadLink = getMegavideoLink(link);
			else if (isBTVLink(link))
				downloadLink = getBTVLink(link);
			else if (isOndaCeroLink(link))
				downloadLink = getOndaCeroLink(link);
			else if (isCanalSurALaCarta(link))
				downloadLink = getCanalSurALaCarta(link);
			else if (isRadiotelevisioValenciana(link))
				downloadLink = getRadiotelevisioValenciana(link);
			else
				throw new InvalidLinkException();
		} catch (IOException e) {
			throw new InvalidLinkException();
		}
		return downloadLink;
	}

	public String HTMLSource(String address) throws MalformedURLException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new URL(address).openStream()));
		String source = "";
		StringBuffer buffer = new StringBuffer();
		for (String line = ""; (line = br.readLine()) != null; buffer.append(line + '\n'))
			;
		source = buffer.toString();
		String line = br.readLine();
		while (line != null) {
			line = br.readLine();
			source = source + line + "\n";
		}
		return source;
	}

	@Override
	public void run() {
		try {
			Manager.getInstance().fireLinkRevealed(new LinkRevealedEvent(this, getLink(originalUrl)));
		} catch (InvalidLinkException e) {
			Manager.getInstance().fireLinkRevealed(new LinkRevealedEvent(this, null));
		}
	}

}
