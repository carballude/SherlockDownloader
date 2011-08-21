package org.carballude.sherlock.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TransferencesPanel extends JPanel {

	private static final long serialVersionUID = 354423746072556510L;

	private JPanel controlPanel;
	private JButton pauseButton;
	private JScrollPane scrollPane;
	private JTable transferencesTable;

	public TransferencesPanel() {
		setLayout(new BorderLayout());
		add(getControlPanel(),BorderLayout.NORTH);
		add(getScrollPane(), BorderLayout.CENTER);
	}
	
	private JPanel getControlPanel(){
		if(controlPanel==null){
			controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			controlPanel.add(getPauseButton());
		}
		return controlPanel;
	}
	
	private JButton getPauseButton() {
		if(pauseButton==null){
			pauseButton=new JButton("Pausa");
		}
		return pauseButton;
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null)
			scrollPane = new JScrollPane(getTransferencesTable());
		return scrollPane;
	}

	private JTable getTransferencesTable() {
		if (transferencesTable == null) {
			transferencesTable = new JTable(new TransferencesTableModel());
			ProgressRenderer renderer = new ProgressRenderer(0, 100);
			renderer.setStringPainted(true);
			transferencesTable.setDefaultRenderer(JProgressBar.class, renderer);
		}
		return transferencesTable;
	}

}
