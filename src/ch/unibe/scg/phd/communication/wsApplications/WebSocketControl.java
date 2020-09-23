package ch.unibe.scg.phd.communication.wsApplications;

import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unibe.scg.phd.core.BrowserController;

public class WebSocketControl extends WebSocketApplication {
	
	private static Logger _LOG = LoggerFactory.getLogger(WebSocketControl.class);

	BrowserController _controller;
	WebSocket _currentSocket; // only supports one client at this time
	
	public WebSocketControl(BrowserController controller) {
		_controller = controller;
	}
	
	@Override
	public void onConnect(WebSocket socket) {
	   _LOG.info("Client connected to control channel.");
	   _currentSocket = socket;
	}
	
	@Override
	public void onMessage(WebSocket socket, String text) {
		String originalMessage = text.substring(2);
		int keyCode;
		int x, y;
		String[] dimensions;
		
		switch (text.charAt(0)) {
		case 'C': // mouse-click left
			dimensions = originalMessage.split(",");
			x = Integer.valueOf(dimensions[0]);
			y = Integer.valueOf(dimensions[1]);
			_LOG.info("Received mouse click: " + x + "," + y);
			_controller.performMouseClick(x, y);
			break;
		case 'K': // key press (key press of normal keys)
			int charCode = Integer.valueOf(originalMessage);
			_LOG.info("Received key press code: " + charCode);
			_controller.performKeyPress(charCode);
			break;
		case 'L': // key down (key down of control keys)
			keyCode = Integer.valueOf(originalMessage);
			_LOG.info("Received key down code: " + keyCode);
			_controller.performKeyDown(keyCode);
			break;
		case 'M': // mouse move (too slow)
			dimensions = originalMessage.split(",");
			x = Integer.valueOf(dimensions[0]);
			y = Integer.valueOf(dimensions[1]);
			_LOG.info("Received mouse move: " + x + "," + y);
			_controller.performMouseMove(x, y);
			break;
		case 'D': // context click (right mouse button) (does show in client)
			dimensions = originalMessage.split(",");
			x = Integer.valueOf(dimensions[0]);
			y = Integer.valueOf(dimensions[1]);
			_LOG.info("Received context mouse click: " + x + "," + y);
			_controller.performContextMouseClick(x, y);
			break;
		case 'X': // mouse down
			dimensions = originalMessage.split(",");
			x = Integer.valueOf(dimensions[0]);
			y = Integer.valueOf(dimensions[1]);
			_LOG.info("Received mouse down: " + x + "," + y);
			_controller.performMouseDown(x, y);
			break;
		case 'Y': // mouse up
			dimensions = originalMessage.split(",");
			x = Integer.valueOf(dimensions[0]);
			y = Integer.valueOf(dimensions[1]);
			_LOG.info("Received mouse up: " + x + "," + y);
			_controller.performMouseUp(x, y);
			break;
		case 'P': // copy paste input
			_LOG.info("Received copy paste event.");
			_controller.performStringInput(originalMessage);
			break;
		case 'R': // restart browser
			_LOG.info("Received restart event.");
			_controller.restartHeadlessBrowser();
			break;
		}
		
	}

	@Override
	public void onMessage(WebSocket socket, byte[] bytes) { }
	
	public void sendControlMessage(String message) {
		_currentSocket.send(message);
	}
}
