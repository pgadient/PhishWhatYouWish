package ch.unibe.scg.phd.communication;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unibe.scg.phd.utils.FileUtil;

public class ServerHttp {
	
	private static Logger _LOG = LoggerFactory.getLogger(ServerHttp.class);
    
	public static void start() {
		// https://javaee.github.io/grizzly/httpserverframework.html
		HttpServer server = HttpServer.createSimpleServer(FileUtil.getFullyQualifiedHttpServerRoot(), 8080);
		for (NetworkListener each : server.getListeners()) {
			// disable static resource caching to allow HTML file changes while server is running
			_LOG.info("Static HTTP listener found: " + each.getName());
			each.getFileCache().setEnabled(false);
		}
		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					server.start();
					_LOG.info("HTTP server online for static content.");
					while (true) {
						Thread.currentThread().join();
						_LOG.error("Thread finished join of himself (should never happen).");
					}
				} catch (Exception e) {
					System.err.println(e);
				}
			}};
		Thread t = new Thread(r);
		t.start();
	}
	
	
}
