package org.carballude.sherlock.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.carballude.sherlock.controller.IGUI;
import org.carballude.sherlock.controller.LinkRevealedListener;
import org.carballude.sherlock.controller.Manager;
import org.carballude.sherlock.model.LinkRevealedEvent;

public class NewMainWindow extends JFrame implements IGUI, LinkRevealedListener {

	private static final long serialVersionUID = 6069323521031955141L;

	private JStatusBar statusBar;
	private TransferencesPanel transferencesPanel;
	private NewMainWindowMenuBar newMainWindowMenuBar;

	public NewMainWindow() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(640, 480);
		setTitle("Sherlock Downloader");
		setJMenuBar(getNewMainWindowMenuBar());
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getTransferencesPanel(), BorderLayout.CENTER);
		getContentPane().add(getStatusBar(), BorderLayout.SOUTH);
		setVisible(true);
	}
	
	public NewMainWindow subscribeToEvents(){
		Manager.getInstance().addLinkRevealedListener(this);
		return this;
	}

	private NewMainWindowMenuBar getNewMainWindowMenuBar() {
		if (newMainWindowMenuBar == null) {
			newMainWindowMenuBar = new NewMainWindowMenuBar(this);
		}
		return newMainWindowMenuBar;
	}

	private TransferencesPanel getTransferencesPanel() {
		if (transferencesPanel == null)
			transferencesPanel = new TransferencesPanel();
		return transferencesPanel;
	}

	private JStatusBar getStatusBar() {
		if (statusBar == null)
			statusBar = new JStatusBar();
		return statusBar;
	}	

	@Override
	public void showUpdateAvailable() {
		JOptionPane.showMessageDialog(this, "ÁHay una actualizaci—n disponible!\nDesc‡rgala de http://www.carballude.es/blog/?p=1019","Actualizaci—n disponible",JOptionPane.INFORMATION_MESSAGE);		
	}

	@Override
	public void linkRevealed(final LinkRevealedEvent event) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				if(event.getUrl()==null)
					statusBar.setStatusText("No se ha podido encontrar el v’deo :(");
				else
					statusBar.setStatusText("V’deo a–adido a la lista de descargas");
			}
		});
	}

}
