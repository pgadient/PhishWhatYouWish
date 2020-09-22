package ch.unibe.scg.phd;

import ch.unibe.scg.phd.communication.ServerHttp;
import ch.unibe.scg.phd.communication.ServerWebSocket;
import ch.unibe.scg.phd.core.BrowserController;

public class Main {

	public static void main(String[] args) {
		//TODO: tab support
		//TODO: multi-user support
		
		BrowserController controller = new BrowserController("https://www.google.com/", false);
		
		ServerHttp.start();
		ServerWebSocket.start(controller);
	}
	
	

}
