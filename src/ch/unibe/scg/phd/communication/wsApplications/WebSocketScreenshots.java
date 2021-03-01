package ch.unibe.scg.phd.communication.wsApplications;

import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.openqa.selenium.Dimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import ch.unibe.scg.phd.core.IdManager;

public class WebSocketScreenshots extends WebSocketApplication {
	
	private static Logger _LOG = LoggerFactory.getLogger(WebSocketScreenshots.class);

	private WebSocket _socket;
	private Dimension _dimension = null;
	
	@Override
	public void onConnect(WebSocket socket) {
//		IdManager.addEntry(IdManager.getUniqueId(), socket);
		_socket = socket;
		_LOG.info("Client connected to image channel.");
	}
	
	@Override
	public void onMessage(WebSocket socket, String text) {
		String[] dimensions = text.split("x");
		_dimension = new Dimension(Integer.valueOf(dimensions[0]), Integer.valueOf(dimensions[1]));
	}

	@Override
	public void onMessage(WebSocket socket, byte[] bytes) { }
	
	public void sendImage(byte[] image) {
		_socket.send(image);
	}
	
	public Dimension getCurrentClientDimension() {
		return _dimension;
	}
	
}
