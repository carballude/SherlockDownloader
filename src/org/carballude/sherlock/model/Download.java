package org.carballude.sherlock.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Observable;

public class Download extends Observable implements Runnable {

	public enum Status {
		Downloading, Paused, Completed, Cancelled, Error, NotStarted
	}

	private static final int MAX_BUFFER_SIZE = 1024;

	private URL url;
	private Status status;
	private int size;
	private int bytesDownloaded;

	public Download(URL url) {
		this.url = url;
		this.status = Status.NotStarted;
		this.size = -1;
		download();
	}

	public Status getStatus() {
		return status;
	}

	public URL getUrl() {
		return url;
	}

	public String getFileName() {
		String[] chunks = url.toString().split("/");
		return chunks[chunks.length - 1];
	}

	public String getSize() {
		if (size < 1024)
			return size + " Bytes";
		DecimalFormat df1 = new DecimalFormat("####.00");
		if (size < 1048576)
			return df1.format(size / (double) 1024) + "KB";
		if (size < 1073741824)
			return df1.format(size / (double) (1024 * 1024)) + "MB";
		return df1.format(size / (double) (1024 * 1024 * 1024)) + "GB";
	}

	public float getProgress() {
		return (bytesDownloaded * (float)100) / size;
	}

	public String getDownloaded() {
		if (bytesDownloaded < 1024)
			return bytesDownloaded + " Bytes";
		DecimalFormat df1 = new DecimalFormat("####.00");
		if (bytesDownloaded < 1048576)
			return df1.format(bytesDownloaded / (double) 1024) + "KB";
		if (bytesDownloaded < 1073741824)
			return df1.format(bytesDownloaded / (double) (1024 * 1024)) + "MB";
		return df1.format(bytesDownloaded / (double) (1024 * 1024 * 1024)) + "GB";
	}

	private void error() {
		status = Status.Error;
		stateChanged();
	}

	private void stateChanged() {
		setChanged();
		notifyObservers();
	}

	private void download() {
		if (status != Status.Downloading) {
			Thread thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		RandomAccessFile file = null;
		InputStream stream = null;
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Range", "bytes=" + bytesDownloaded + "-");
			connection.connect();
			if (connection.getResponseCode() / 100 != 2)
				error();
			int contentLength = connection.getContentLength();
			if (contentLength < 1)
				error();
			if (size == -1)
				size = contentLength;
			file = new RandomAccessFile(getFileName(), "rw");
			file.seek(bytesDownloaded);
			status = Status.Downloading;
			stateChanged();
			stream = connection.getInputStream();
			while (status == Status.Downloading) {
				byte buffer[];
				if (size - bytesDownloaded > MAX_BUFFER_SIZE) {
					buffer = new byte[MAX_BUFFER_SIZE];
				} else {
					buffer = new byte[size - bytesDownloaded];
				}
				int read = stream.read(buffer);
				if (read == -1)
					break;
				file.write(buffer, 0, read);
				bytesDownloaded += read;
				stateChanged();
			}
			if (status == Status.Downloading) {
				status = Status.Completed;
				stateChanged();
			}
		} catch (IOException e) {
			error();
		} finally {
			// Close file.
			if (file != null) {
				try {
					file.close();
				} catch (Exception e) {
				}
			}
			// Close connection to server.
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
				}
			}
		}
	}

}
