package org.carballude.sherlock.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.carballude.sherlock.controller.IGUI;
import org.carballude.sherlock.controller.LinkRevealedListener;
import org.carballude.sherlock.controller.LinkRevealer;
import org.carballude.sherlock.controller.Manager;
import org.carballude.sherlock.model.LinkRevealedEvent;

public class MainWindow extends JFrame implements LinkRevealedListener, IGUI {

	private static final long serialVersionUID = -8132022985151674113L;
	private JPanel panelNorth;
	private JPanel panelCenter;
	private JPanel panelSouth;

	private JStatusBar statusBar;

	private JButton buttonShowLink;

	private JTextField textOriginalUrl;
	private JTextField textRevealedUrl;

	public MainWindow() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Sherlock Downloader");
		setSize(800, 150);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getPanelNorth(), BorderLayout.NORTH);
		getContentPane().add(getPanelCenter(), BorderLayout.CENTER);
		getContentPane().add(getPanelSouth(), BorderLayout.SOUTH);
		setVisible(true);
	}

	public MainWindow subscribeToEvents() {
		Manager.getInstance().addLinkRevealedListener(this);
		return this;
	}

	private JPanel getPanelNorth() {
		if (panelNorth == null) {
			panelNorth = new JPanel(new BorderLayout());
			panelNorth.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
			panelNorth.add(new JLabel("Url original: "), BorderLayout.WEST);
			panelNorth.add(getTextOriginalUrl(), BorderLayout.CENTER);
		}
		return panelNorth;
	}

	private JPanel getPanelCenter() {
		if (panelCenter == null) {
			panelCenter = new JPanel(new FlowLayout(FlowLayout.CENTER));
			panelCenter.add(getShowLinkButton());
		}
		return panelCenter;
	}

	private JPanel getPanelSouth() {
		if (panelSouth == null) {
			panelSouth = new JPanel(new BorderLayout());
			panelSouth.add(new JLabel("Url revelada: "), BorderLayout.WEST);
			panelSouth.add(getTextRevealedUrl(), BorderLayout.CENTER);
			panelSouth.add(getStatusBar(), BorderLayout.SOUTH);
		}
		return panelSouth;
	}

	private JStatusBar getStatusBar() {
		if (statusBar == null) {
			statusBar = new JStatusBar();
			statusBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0),
					BorderFactory.createLoweredBevelBorder()));
			statusBar.setStatusText("Listo :)");
		}
		return statusBar;
	}

	private JButton getShowLinkButton() {
		if (buttonShowLink == null) {
			buttonShowLink = new JButton("Mostrar enlace");
			buttonShowLink.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					statusBar.setStatusText("Buscando...");
					new Thread(new LinkRevealer(textOriginalUrl.getText())).start();
				}
			});
		}
		return buttonShowLink;
	}

	private JTextField getTextOriginalUrl() {
		if (textOriginalUrl == null) {
			textOriginalUrl = new JTextField();
		}
		return textOriginalUrl;
	}

	private JTextField getTextRevealedUrl() {
		if (textRevealedUrl == null) {
			textRevealedUrl = new JTextField();
			textRevealedUrl.setEditable(false);
		}
		return textRevealedUrl;
	}

	@Override
	public void linkRevealed(final LinkRevealedEvent event) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (event.getUrl() != null) {
					statusBar.setStatusText("Dirección encontrada y copiada al portapapeles :)");
					textRevealedUrl.setText(event.getUrl());
					StringSelection data = new StringSelection(event.getUrl());
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(data, data);
				} else {
					statusBar.setStatusText("No se ha podido encontrar el enlace :(");
					textRevealedUrl.setText("");
				}
			}
		});

	}

	public void showUpdateAvailable() {
		final JButton downloadButton = new JButton("Descargar");
		downloadButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					java.awt.Desktop.getDesktop().browse(URI.create("http://www.carballude.es/blog/?p=1019"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		final JComponent[] components = new JComponent[]{new JLabel("Hay una actualizaci—n disponible que puede obtener desde:"),new JLabel("http://www.carballude.es/blog/?p=1019"),new JButton("Descargar")};
		JOptionPane.showMessageDialog(this, components, "Actualizaci—n disponible", JOptionPane.INFORMATION_MESSAGE);
	}
}
