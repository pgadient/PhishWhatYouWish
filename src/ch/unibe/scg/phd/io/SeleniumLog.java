package ch.unibe.scg.phd.io;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unibe.scg.phd.Main;

public class SeleniumLog {
	
	private static Logger _LOG = LoggerFactory.getLogger(SeleniumLog.class);
	private static ByteArrayOutputStream _BAOS = new ByteArrayOutputStream();
	private static boolean _NEEDS_REINIT = true; 
	
	public static void initWorkerThread() {
		System.setErr(new PrintStream(_BAOS));
		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				while (true) {
					BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(_BAOS.toByteArray())));
					_BAOS.reset();
					try {
						while (br.ready()) {
							String currentLine = br.readLine();
							if (!currentLine.equals("")) {
//								if (_NEEDS_REINIT) {
//									Main.reinitLogger();
//									_NEEDS_REINIT = false;
//								}
//								String currentLineLower = currentLine.toLowerCase();
//								if (currentLineLower.contains("trace")) {
//									_LOG.trace(currentLine);
//								} else if (currentLineLower.contains("debug")) {
//									_LOG.debug(currentLine);
//								} else if (currentLineLower.contains("info")) {
//									_LOG.info(currentLine);
//								} else if (currentLineLower.contains("warn")) {
//									_LOG.warn(currentLine);
//								} else if (currentLineLower.contains("error")) {
									_LOG.error(currentLine);
//								} else {
//									_LOG.info(currentLine);
//								}
							}
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
		}};
		Thread t = new Thread(r);
		t.start();
	}
	
}
