package ch.unibe.scg.phd.communication;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketEngine;

import ch.unibe.scg.phd.communication.wsApplications.WebSocketControl;
import ch.unibe.scg.phd.communication.wsApplications.WebSocketScreenshots;
import ch.unibe.scg.phd.core.BrowserController;
import ch.unibe.scg.phd.io.Log;

public class ServerWebSocket {

	 private static Log _LOG = new Log(ServerHttp.class);
	 private static WebSocketControl _CONTROL_APPLICATION = null;
	 private static WebSocketScreenshots _SCREENSHOT_APPLICATION = null;

		public static void start(BrowserController controller) {
			
			final HttpServer server = HttpServer.createSimpleServer(null, 8081);
			final WebSocketAddOn addon = new WebSocketAddOn();
			for (NetworkListener listener : server.getListeners()) {
			    listener.registerAddOn(addon);
			}
			
			_CONTROL_APPLICATION = new WebSocketControl(controller);
			WebSocketEngine.getEngine().register("", "/control", _CONTROL_APPLICATION);
			
			_SCREENSHOT_APPLICATION = new WebSocketScreenshots(controller);
			WebSocketEngine.getEngine().register("", "/screenshots", _SCREENSHOT_APPLICATION);
			
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						server.start();
						_LOG.info("WebSocket server online.");
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
		
		public static void sendControlMessage(String message) {
			_CONTROL_APPLICATION.sendControlMessage(message);
		}
}
