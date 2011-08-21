package org.carballude.sherlock.view;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class JStatusBar extends JPanel {

	private static final long serialVersionUID = -8851176123532211846L;

	private JLabel statusLabel;

	public JStatusBar() {
		setBorder(BorderFactory.createLoweredBevelBorder());
		setLayout(new BorderLayout());
		add(getStatusLabel(), BorderLayout.CENTER);
	}

	private JLabel getStatusLabel() {
		if (statusLabel == null)
			statusLabel = new JLabel("Ready :)");
		return statusLabel;
	}

	public void setStatusText(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				statusLabel.setText(text);
			}
		});
	}

}
