package org.carballude.sherlock.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.sherlock.controller.revealers.Antena3;
import org.carballude.sherlock.controller.revealers.BarcelonaTV;
import org.carballude.sherlock.controller.revealers.CanalPlus;
import org.carballude.sherlock.controller.revealers.CanalSurALaCarta;
import org.carballude.sherlock.controller.revealers.Cope;
import org.carballude.sherlock.controller.revealers.Cuatro;
import org.carballude.sherlock.controller.revealers.EuskalIrratiTelebista;
import org.carballude.sherlock.controller.revealers.GoEar;
import org.carballude.sherlock.controller.revealers.Intereconomia;
import org.carballude.sherlock.controller.revealers.Megavideo;
import org.carballude.sherlock.controller.revealers.MySpace;
import org.carballude.sherlock.controller.revealers.OndaCero;
import org.carballude.sherlock.controller.revealers.PlanetaUrbe;
import org.carballude.sherlock.controller.revealers.RTVCastillaMancha;
import org.carballude.sherlock.controller.revealers.RadiotelevisioValenciana;
import org.carballude.sherlock.controller.revealers.Ser;
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
		return url.contains("www.telecinco.es/") || url.contains("www.mitele.telecinco.es");
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
		return url.contains("www.ondacero.es/OndaCero/play/") || url.contains("www.ondacero.es/audios-online/");
	}

	private boolean isCanalSurALaCarta(String url) {
		return url.contains("www.canalsuralacarta.es");
	}

	private boolean isRadiotelevisioValenciana(String url) {
		return url.contains("www.rtvv.es/va/");
	}
	
	private boolean isTelevisioCatalunya(String url) {
		return url.contains("www.tv3.cat/") || url.contains("www.3xl.cat/");
	}
	
	private boolean isMySpace(String url){
		return url.contains("www.myspace.com/");
	}
	
	private boolean isIntereconomia(String url){
		return url.contains("www.intereconomia.com/");
	}
	
	private boolean isEuskalIrratiTelebista(String url){
		return url.contains("www.eitb.com");
	}
	
	private boolean isCuatro(String url){
		return url.contains("www.cuatro.com/");
	}
	
	private boolean isPlanetaUrbe(String url){
		return url.contains("www.planetaurbe.tv");
	}
	
	private boolean isRTVCastillaMancha(String url){
		return url.contains("www.rtvcm.es");
	}
	
	private boolean isCope(String url){
		return url.contains("www.cope.es");
	}
	
	private boolean isSer(String url){
		return url.contains("www.cadenaser.com");
	}
	
	private boolean isCanalPlus(String url){
		return url.contains("canalplus.es");
	}
	
	public String getLink(String link) throws InvalidLinkException {
		String downloadLink = null;
		if (!(link.startsWith("http://") || link.startsWith("https://"))) {
			link = "http://" + link;
		}
		try {
			if (isTVELink(link))
				downloadLink = new TVE().revealLink(link);
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
			else if(isIntereconomia(link))
				downloadLink = new Intereconomia().revealLink(link);
			else if (isEuskalIrratiTelebista(link))
				downloadLink = new EuskalIrratiTelebista().revealLink(link);
			else if(isCuatro(link))
				downloadLink = new Cuatro().revealLink(link);
			else if(isPlanetaUrbe(link))
				downloadLink = new PlanetaUrbe().revealLink(link);
			else if(isRTVCastillaMancha(link))
				downloadLink = new RTVCastillaMancha().revealLink(link);
			else if(isCope(link))
				downloadLink = new Cope().revealLink(link);
			else if(isSer(link))
				downloadLink=new Ser().revealLink(link);
			else if(isCanalPlus(link))
				downloadLink = new CanalPlus().revealLink(link);
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
