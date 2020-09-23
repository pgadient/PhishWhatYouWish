package ch.unibe.scg.phd;

import ch.unibe.scg.phd.communication.ServerHttp;
import ch.unibe.scg.phd.communication.ServerWebSocket;
import ch.unibe.scg.phd.core.BrowserController;
import ch.unibe.scg.phd.utils.FileUtil;

public class Main {

	public static void main(String[] args) {
		//TODO: tab support
		//TODO: multi-user support
		
		//System.out.println(FileUtil.getFilePath("/ch/unibe/scg/phd/webdrivers/geckodriver-v0.27.0-win64.exe"));
		FileUtil.prepareJarTempFolder();
		BrowserController controller = new BrowserController("https://www.google.com/", false);
		
		ServerHttp.start();
		ServerWebSocket.start(controller);
	}
	
	

}
