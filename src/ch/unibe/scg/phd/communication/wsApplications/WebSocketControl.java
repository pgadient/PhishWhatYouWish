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
	boolean _firstConnect = false;
	
	public WebSocketControl(BrowserController controller) {
		_controller = controller;
	}
	
	@Override
	public void onConnect(WebSocket socket) {
	   _LOG.info("Client connected to control channel.");
	   _currentSocket = socket;
	   _firstConnect = true;
	}
	
	@Override
	public void onMessage(WebSocket socket, String text) {
		String originalMessage = text.substring(2);
		int keyCode;
		int x, y;
		String[] dimensions;
		
		switch (text.charAt(0)) {
		case 'I': // client needs metadata
			_controller.updateTitleAndUrlAndFavicon(false);
			break;
		case 'B': // client needs clickables
			_controller.injectNativeControls();
			break;
		case 'C': // mouse-click left
			dimensions = originalMessage.split(",");
			x = Integer.valueOf(dimensions[0]);
			y = Integer.valueOf(dimensions[1]);
			_LOG.warn("Received mouse click: " + x + "," + y);
			_controller.performMouseClick(x, y);
			break;
		case 'K': // key press (key press of normal keys)
			int charCode = Integer.valueOf(originalMessage);
			_LOG.warn("Received key press code: " + charCode);
			_controller.performKeyPress(charCode);
			break;
		case 'L': // key down (key down of control keys)
			keyCode = Integer.valueOf(originalMessage);
			_LOG.warn("Received key down code: " + keyCode);
			_controller.performKeyDown(keyCode);
			break;
		case 'M': // mouse move (too slow)
			dimensions = originalMessage.split(",");
			x = Integer.valueOf(dimensions[0]);
			y = Integer.valueOf(dimensions[1]);
			_LOG.warn("Received mouse move: " + x + "," + y);
			_controller.performMouseMove(x, y);
			break;
		case 'D': // context click (right mouse button) (does show in client)
			dimensions = originalMessage.split(",");
			x = Integer.valueOf(dimensions[0]);
			y = Integer.valueOf(dimensions[1]);
			_LOG.warn("Received context mouse click: " + x + "," + y);
			_controller.performContextMouseClick(x, y);
			break;
		case 'X': // mouse down
			dimensions = originalMessage.split(",");
			x = Integer.valueOf(dimensions[0]);
			y = Integer.valueOf(dimensions[1]);
			_LOG.warn("Received mouse down: " + x + "," + y);
			_controller.performMouseDown(x, y);
			break;
		case 'Y': // mouse up
			dimensions = originalMessage.split(",");
			x = Integer.valueOf(dimensions[0]);
			y = Integer.valueOf(dimensions[1]);
			_LOG.warn("Received mouse up: " + x + "," + y);
			_controller.performMouseUp(x, y);
			break;
		case 'P': // copy paste input
			_LOG.warn("Received copy paste event.");
			_controller.performStringInput(originalMessage);
			break;
		case 'R': // restart browser
			_LOG.warn("Received restart event.");
			_controller.restartHeadlessBrowser();
			break;
		case 'V': // received back button event
			_LOG.warn("Received back event.");
			_controller.navigateBack();
			break;
		case 'W': // received forward button event
			_LOG.warn("Received forward event.");
			_controller.navigateForward();
			break;
		case 'A':
			_controller.dimensionCheck();
			break;
		case 'F':	
			int faviconId = Integer.valueOf(originalMessage);
			_controller.navigateTo(faviconId);
			break;
		}
	}

	@Override
	public void onMessage(WebSocket socket, byte[] bytes) { }
	
	public void sendControlMessage(String message) {
		_currentSocket.send(message);
	}
	
	public boolean getFirstConnectState() {
		return _firstConnect;
	}
	
	public void setFirstConnectState(boolean newState) {
		_firstConnect = newState;
	}
}
