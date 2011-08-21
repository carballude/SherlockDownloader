package org.carballude.sherlock.model;

import java.util.EventObject;

public class LinkRevealedEvent extends EventObject {

	private String url;
	private Download download;

	public LinkRevealedEvent(Object source, String url) {
		super(source);
		this.url = url;
	}
	
	public void setDownload(Download download){
		this.download = download;
	}
	
	public Download getDownload(){
		return download;
	}

	public String getUrl() {
		return url;
	}

	private static final long serialVersionUID = -6321071323627391305L;

}
