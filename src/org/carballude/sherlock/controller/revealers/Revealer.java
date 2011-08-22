package org.carballude.sherlock.controller.revealers;

import java.io.IOException;
import java.net.MalformedURLException;

import org.carballude.sherlock.controller.excepions.InvalidLinkException;

public interface Revealer {

	String revealLink(String link) throws MalformedURLException, IOException, InvalidLinkException;
	
}
