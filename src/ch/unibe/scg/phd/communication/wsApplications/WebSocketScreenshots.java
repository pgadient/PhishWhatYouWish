package ch.unibe.scg.phd.communication.wsApplications;

import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;

import ch.unibe.scg.phd.core.BrowserController;
import ch.unibe.scg.phd.phishing.IdManager;

public class WebSocketScreenshots extends WebSocketApplication {

	BrowserController _controller;
	
	public WebSocketScreenshots(BrowserController controller) {
		_controller = controller;
	}
	
	@Override
	public void onConnect(WebSocket socket) {
		IdManager.addEntry(IdManager.getUniqueId(), socket); 
		System.out.println("Client connected to image channel.");
	}
	
	@Override
	public void onMessage(WebSocket socket, String text) {
		String[] dimensions = text.split("x");
		int width = Integer.valueOf(dimensions[0]);
		int height = Integer.valueOf(dimensions[1]);
		//System.out.println("WebSocketScreenshots: Changing dimension of web browser (" + width + "x" + height + ").");
		byte[] image = _controller.generateScreenshot(width, height);
		_controller.updateTitleAndUrl();
		this.sendImage(socket, image);
	}

	@Override
	public void onMessage(WebSocket socket, byte[] bytes) {
		//System.out.println("Screenshots onMessage Binary!");
	}
	
	public void sendImage(WebSocket socket, byte[] image) {
		socket.send(image);
	}
	
}
