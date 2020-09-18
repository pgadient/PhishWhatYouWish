package ch.unibe.scg.phd.communication;

import org.glassfish.grizzly.http.server.HttpServer;

import ch.unibe.scg.phd.io.Log;
import ch.unibe.scg.phd.properties.Properties;

public class ServerHttp {
	
    private static Log _LOG = new Log(ServerHttp.class);
    
	public static void start() {
		HttpServer server = HttpServer.createSimpleServer(System.getProperty("user.dir") + Properties.PATH_STATIC_HTML, 8080);
		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					server.start();
					_LOG.info("HTTP server online for static content.");
					while (true) {
						Thread.currentThread().join();
						_LOG.info("Thread finished join of himself (should never happen).");
					}
				} catch (Exception e) {
					System.err.println(e);
				}
			}};
		Thread t = new Thread(r);
		t.start();
	}
	
	
}
