package org.carballude.sherlock.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.carballude.sherlock.model.Download;
import org.carballude.sherlock.model.LinkRevealedEvent;
import org.carballude.sherlock.view.MainWindow;
import org.carballude.sherlock.view.NewMainWindow;

public class Manager {

	private static Manager INSTANCE = null;

	private ArrayList<LinkRevealedListener> linkRevealedListeners;

	private ArrayList<Download> downloads;
	
	public static final int VERSION = 201112030;
	
	private IGUI gui;

	private Manager() {
		linkRevealedListeners = new ArrayList<LinkRevealedListener>();
		downloads = new ArrayList<Download>();
	}

	public void addDownload(Download download) {
		if (!downloads.contains(download))
			downloads.add(download);
	}

	public void startsGUI() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				//gui = new NewMainWindow().subscribeToEvents(); //
				gui = new MainWindow().subscribeToEvents();
				new Thread(new CheckUpdate_Thread()).start();
				//new NewMainWindow();
			}
		});		
	}
	
	public void showUpdateAvailable(){
		gui.showUpdateAvailable();
	}

	public void addLinkRevealedListener(LinkRevealedListener listener) {
		if (!linkRevealedListeners.contains(listener))
			linkRevealedListeners.add(listener);
	}
	
	public ArrayList<Download> getDownloads(){
		return downloads;
	}

	void fireLinkRevealed(LinkRevealedEvent event) {
		Download newDownload = null;
		if (event.getUrl() != null && gui instanceof NewMainWindow)
			try {
				newDownload = new Download(new URL(event.getUrl()));
				event.setDownload(newDownload);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		for (LinkRevealedListener listener : linkRevealedListeners)
			listener.linkRevealed(event);
	}

	public static synchronized Manager getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Manager();
		return INSTANCE;
	}

}
