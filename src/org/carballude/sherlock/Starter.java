package org.carballude.sherlock;

import org.carballude.sherlock.controller.LinkRevealer;
import org.carballude.sherlock.controller.Manager;
import org.carballude.sherlock.controller.excepions.InvalidLinkException;

public class Starter {

	public static void main(String[] args) {
		if (args.length == 0)
			Manager.getInstance().startsGUI();
		else if (args.length == 2 && args[0].compareTo("--no-gui") == 0)
			try {
				System.out.println(new LinkRevealer(args[1]).getLink(args[1]));
			} catch (InvalidLinkException e) {
				System.err.println("No se ha encontrado el archivo :(");
				System.exit(1);
			}
		else {
			System.out.println("Sherlock Downloader - " + Manager.VERSION);
			System.out.println("Finding video and audio from: TVE1, TVE2, RNE1, RNE5, Tele5, BTV, CanalSur A La Carta, RTVV, TV3, EITB, Intereconomia, MegaVideo, OndaCero and GoEar");
			System.out.println();
			System.out.println("Usage: java -jar <filename.jar>");
			System.out.println("\tOption: --no-gui");
			System.out.println("\t\t Requires argument <link>");
			System.exit(1);
		}
	}
}
