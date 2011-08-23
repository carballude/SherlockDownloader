package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;
import org.carballude.utilities.HTML;

public class MySpace implements Revealer {

	@Override
	public String revealLink(String link) throws MalformedURLException,
			IOException, InvalidLinkException {
        String id = link.toString().substring(link.toString().lastIndexOf('-') + 1);
        try {
                String xmlCode = HTML.HTMLSource("http://www.myspace.com/music/services/player?action=getSong&songId=" + id);                
                return xmlCode.split("<rtmp>")[1].split("</rtmp>")[0];
        } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
        throw new InvalidLinkException();
	}

}
