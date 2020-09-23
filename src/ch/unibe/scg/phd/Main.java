package ch.unibe.scg.phd;

import ch.unibe.scg.phd.communication.ServerHttp;
import ch.unibe.scg.phd.communication.ServerWebSocket;
import ch.unibe.scg.phd.core.BrowserController;
import ch.unibe.scg.phd.io.Log;
import ch.unibe.scg.phd.utils.FileUtil;

public class Main {
	
	public static boolean _FEATURE_DEBUG_OUTPUT = true;
	public static boolean _FEATURE_ENABLE_ADBLOCK = true;
	public static boolean _FEATURE_HEADLESS = false;
	public static String _PARAM_DEFAULT_BASE_URL = "https://www.google.ch/";

	public static void main(String[] args) {
		//TODO: tab support
		//TODO: multi-user support
		//TODO: webdriver.exe remains running after exit (at least on Windows x64)
		//TODO: browser height bug fix
		//TODO: textbox alignment bug fix
		//TODO: forward / backward key support
		//TODO: reinit with certain key
		if (args.length != 4) {
			System.out.println("Main: Wrong argument count. Note: URL needs http/https protocol prefix.");
			System.out.println("Main: Using default configuration. See examples below.");
			System.out.println("Main: java -jar PhD.jar [headless] [adblock] [debug output] [landing page URL]");
			System.out.println("Main: java -jar PhD.jar true true false \"https://www.google.com\"");
		} else {
			_FEATURE_HEADLESS = Boolean.getBoolean(args[0]);
			_FEATURE_ENABLE_ADBLOCK = Boolean.getBoolean(args[1]);
			_FEATURE_DEBUG_OUTPUT = Boolean.getBoolean(args[2]);
			_PARAM_DEFAULT_BASE_URL = args[3];
		}
		
		Log.setDebug(_FEATURE_DEBUG_OUTPUT);
		printCurrentConfiguration();
		FileUtil.collectSystemInfo();
		FileUtil.prepareJarTempFolder();
		BrowserController controller = new BrowserController(_PARAM_DEFAULT_BASE_URL, _FEATURE_HEADLESS, _FEATURE_ENABLE_ADBLOCK, _FEATURE_DEBUG_OUTPUT);
		
		ServerHttp.start();
		ServerWebSocket.start(controller);
	}
	
	private static void printCurrentConfiguration() {
		StringBuilder featureState = new StringBuilder();
		if (_FEATURE_HEADLESS) {
			featureState.append("headless: on, ");
		} else {
			featureState.append("headless: off, ");
		}
		
		if (_FEATURE_ENABLE_ADBLOCK) {
			featureState.append("adblock: on, ");
		} else {
			featureState.append("adblock: off, ");
		}
		
		if (_FEATURE_DEBUG_OUTPUT) {
			featureState.append("debug: on, ");
		} else {
			featureState.append("debug: off, ");
		}
		
		featureState.append("baseUrl: " + _PARAM_DEFAULT_BASE_URL);
		System.out.println("Main: " + featureState.toString());
	}

}
