package org.carballude.sherlock.controller;

import java.util.EventListener;

import org.carballude.sherlock.model.LinkRevealedEvent;

public interface LinkRevealedListener extends EventListener {

	void linkRevealed(LinkRevealedEvent event);

}
