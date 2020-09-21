package ch.unibe.scg.phd.communication.requests;

import org.glassfish.grizzly.websockets.WebSocket;

public class ScreenshotRequest {
	
	private int _width;
	private int _height;
	private WebSocket _screenshotSocket;
	
	
	public ScreenshotRequest(int width, int height, WebSocket screenshotSocket) {
		_width = width;
		_height = height;
		_screenshotSocket = screenshotSocket;
	}
	
	public int getWidth() {
		return _width;
	}
	
	public int getHeight() {
		return _height;
	}
	
	public WebSocket getScreenshotSocket() {
		return _screenshotSocket;
	}
	
}
