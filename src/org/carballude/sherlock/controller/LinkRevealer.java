package org.carballude.sherlock.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.sherlock.controller.revealers.Antena3;
import org.carballude.sherlock.controller.revealers.BarcelonaTV;
import org.carballude.sherlock.controller.revealers.CanalSurALaCarta;
import org.carballude.sherlock.controller.revealers.GoEar;
import org.carballude.sherlock.controller.revealers.Megavideo;
import org.carballude.sherlock.controller.revealers.MiTele5;
import org.carballude.sherlock.controller.revealers.MySpace;
import org.carballude.sherlock.controller.revealers.OndaCero;
import org.carballude.sherlock.controller.revealers.RadiotelevisioValenciana;
import org.carballude.sherlock.controller.revealers.TVE;
import org.carballude.sherlock.controller.revealers.Tele5;
import org.carballude.sherlock.controller.revealers.TelevisioCatalunya;
import org.carballude.sherlock.model.LinkRevealedEvent;

public class LinkRevealer implements Runnable {

	private String originalUrl;

	public LinkRevealer(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	private boolean isTVELink(String url) {
		return url.contains("www.rtve.es");
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
	
	private boolean isTelevisioCatalunya(String url) {
		return url.contains("www.tv3.cat/");
	}
	
	private boolean isMySpace(String url){
		return url.contains("www.myspace.com/");
	}
	
	public String getLink(String link) throws InvalidLinkException {
		String downloadLink = null;
		if (!(link.startsWith("http://") || link.startsWith("https://"))) {
			link = "http://" + link;
		}
		try {
			if (isTVELink(link))
				downloadLink = new TVE().revealLink(link);
			else if (isMiTele5Link(link))
				downloadLink = new MiTele5().revealLink(link);
			else if (isTele5Link(link))
				downloadLink = new Tele5().revealLink(link);
			else if (isGoEarLink(link))
				downloadLink = new GoEar().revealLink(link);
			else if (isAntena3Link(link))
				downloadLink = new Antena3().revealLink(link);
			else if (isMegavideoLink(link))
				downloadLink = new Megavideo().revealLink(link);
			else if (isBTVLink(link))
				downloadLink = new BarcelonaTV().revealLink(link);
			else if (isOndaCeroLink(link))
				downloadLink = new OndaCero().revealLink(link);
			else if (isCanalSurALaCarta(link))
				downloadLink = new CanalSurALaCarta().revealLink(link);
			else if (isRadiotelevisioValenciana(link))
				downloadLink = new RadiotelevisioValenciana().revealLink(link);
			else if (isTelevisioCatalunya(link))
				downloadLink = new TelevisioCatalunya().revealLink(link);
			else if(isMySpace(link))
				downloadLink = new MySpace().revealLink(link);
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
