package org.carballude.sherlock.view;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

import org.carballude.sherlock.controller.LinkRevealedListener;
import org.carballude.sherlock.controller.Manager;
import org.carballude.sherlock.model.Download;
import org.carballude.sherlock.model.LinkRevealedEvent;

public class TransferencesTableModel extends AbstractTableModel implements LinkRevealedListener, Observer {

	private static final long serialVersionUID = -5234111736850555506L;

	private String[] columnNames = { "Archivo", "Estado", "Porcentaje", "Descargado", "Tama–o" };
	@SuppressWarnings("rawtypes")
	private static final Class[] columnClasses = { String.class, String.class, JProgressBar.class, String.class, String.class };
	private ArrayList<Download> downloads;

	public TransferencesTableModel() {
		downloads = new ArrayList<Download>();
		Manager.getInstance().addLinkRevealedListener(this);
	}

	@Override
	public int getRowCount() {
		return downloads.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return downloads.get(rowIndex).getFileName();
		case 1:
			return downloads.get(rowIndex).getStatus();
		case 2:
			return downloads.get(rowIndex).getProgress();
		case 3:
			return downloads.get(rowIndex).getDownloaded();
		case 4:
			return downloads.get(rowIndex).getSize();
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Class getColumnClass(int col) {
		return columnClasses[col];
	}

	@Override
	public void linkRevealed(LinkRevealedEvent event) {
		if (event.getUrl() != null) {
			downloads.add(event.getDownload());
			event.getDownload().addObserver(this);
			fireTableRowsInserted(downloads.size() - 1, downloads.size() - 1);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		int row = downloads.indexOf(arg0);
		fireTableRowsUpdated(row, row);
	}

}
