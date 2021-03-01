package ch.unibe.scg.phd.communication;

import org.glassfish.grizzly.http.server.ErrorPageGenerator;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unibe.scg.phd.properties.Configuration;
import ch.unibe.scg.phd.utils.FileUtil;

public class ServerHttp {
	
	private static Logger _LOG = LoggerFactory.getLogger(ServerHttp.class);
//	private static volatile boolean _finishSpinner = true;
    
	public static void start() {
		// https://javaee.github.io/grizzly/httpserverframework.html
		HttpServer server = HttpServer.createSimpleServer(FileUtil.getFullyQualifiedHttpServerRoot(), 8080);
		for (NetworkListener each : server.getListeners()) {
			// disable static resource caching to allow HTML file changes while server is running
			_LOG.info("Static HTTP listener found: " + each.getName());
			each.getFileCache().setEnabled(false);
			each.setDefaultErrorPageGenerator(new ErrorPageGenerator() {
				@Override
				public String generate(Request request, int status, String reasonPhrase, String description, Throwable exception) {
					return new String(FileUtil.getFileContent(FileUtil.getFullyQualifiedHttpServerRoot() + Configuration.PATH_STATIC_HTML_INDEX));
				}});
		}
		
//		// handler /delay for creating delays
//		server.getServerConfiguration().addHttpHandler(
//			    new HttpHandler() {
//			        public void service(Request request, Response response) throws Exception {
//			        	String content = "Hello World!";
//			            response.setContentType("text/plain");
//			            response.setContentLength(content.length());
//			            long currentTime = System.currentTimeMillis();
//			            while (true) {
//			                Thread.sleep(1000);
//				            response.getWriter().write(content);
//				            if (System.currentTimeMillis() - currentTime > 5000) {
//				            	break;
//				            }
//			            }
//			        }
//			    },
//				"/delay");
		
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
	
//	public static void disableSpinner() {
//		_finishSpinner = false;
//	}
}
