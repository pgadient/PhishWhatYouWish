package ch.unibe.scg.phd;

import ch.unibe.scg.phd.communication.ServerHttp;
import ch.unibe.scg.phd.communication.ServerWebSocket;
import ch.unibe.scg.phd.core.BrowserController;

public class Main {

	public static void main(String[] args) {
		//TODO: Favicon support
		//TODO: tab support
		//TODO: multi-user support
		//TODO: rendering textboxes/buttons/links/... locally
		
		BrowserController controller = new BrowserController("https://www.google.com/", false);
		
		ServerHttp.start();
		ServerWebSocket.start(controller);
	}
	
	

}
