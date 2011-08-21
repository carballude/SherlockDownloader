package org.carballude.sherlock.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.carballude.sherlock.controller.LinkRevealer;

public class NewMainWindowMenuBar extends JMenuBar {

	private static final long serialVersionUID = -1410754288434129125L;

	private NewMainWindow parent;

	private JMenu fileMenu;
	private JMenuItem addDownloadMenuItem;
	private JMenuItem exitMenuItem;
	private JMenu helpMenu;
	private JMenuItem aboutMenuItem;

	public NewMainWindowMenuBar(NewMainWindow parent) {
		this.parent = parent;
		add(getFileMenu());
		add(getMenuHelp());
	}

	private JMenuItem getAddDownloadMenuItem() {
		if (addDownloadMenuItem == null) {
			addDownloadMenuItem = new JMenuItem("Descargar enlace...", 'd');
			addDownloadMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String link = JOptionPane.showInputDialog(parent, "Direcci—n del enlace:", "Descargar enlace", JOptionPane.OK_CANCEL_OPTION);
					if (link != null && !link.isEmpty())
						new Thread(new LinkRevealer(link)).start();
				}
			});
		}
		return addDownloadMenuItem;
	}

	private JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new JMenuItem("Salir", 's');
			exitMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return exitMenuItem;
	}

	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu("Sherlock Downloader");
			fileMenu.setMnemonic('s');
			fileMenu.add(getAddDownloadMenuItem());
			fileMenu.addSeparator();
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	private JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new JMenuItem("Acerca de...", 'a');
		}
		return aboutMenuItem;
	}

	private JMenu getMenuHelp() {
		if (helpMenu == null) {
			helpMenu = new JMenu("Ayuda");
			helpMenu.setMnemonic('a');
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}

}
