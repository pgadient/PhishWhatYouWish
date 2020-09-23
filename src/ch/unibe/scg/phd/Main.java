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
	private static Logger _LOG = LoggerFactory.getLogger(Main.class);
	private static boolean _FEATURE_DEBUG_OUTPUT = true;
	private static boolean _FEATURE_ENABLE_ADBLOCK = true;
	private static boolean _FEATURE_HEADLESS = false;
	private static String _PARAM_DEFAULT_BASE_URL = "https://www.google.ch/";
	private static Configuration _configuration;

	public static void main(String[] args) {
		//TODO: tab support
		//TODO: multi-user support
		//TODO: webdriver.exe remains running after exit (at least on Windows x64)
		//TODO: browser height bug fix
		//TODO: textbox alignment bug fix
		//TODO: forward / backward key support
		if (args.length == 4) {
			_FEATURE_HEADLESS = Boolean.parseBoolean(args[0]);
			_FEATURE_ENABLE_ADBLOCK = Boolean.parseBoolean(args[1]);
			_FEATURE_DEBUG_OUTPUT = Boolean.parseBoolean(args[2]);
			_PARAM_DEFAULT_BASE_URL = args[3];
		}
		
		initLogger();
		SeleniumLog.initWorkerThread();		
		
		if (args.length != 4) {
			_LOG.info("Wrong argument count. Note: URL needs http/https protocol prefix.");
			_LOG.info("Using default configuration. See examples below.");
			_LOG.info("java -jar PhD.jar [headless] [adblock] [debug output] [landing page URL]");
			_LOG.info("java -jar PhD.jar true true false \"https://www.google.com\"");
		}
		
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
		_LOG.info(featureState.toString());
	}

	private static void initLogger() {
		ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
		builder.setConfigurationName("defaultConfiguration");
		
		if (_FEATURE_DEBUG_OUTPUT) {
			builder.setStatusLevel(Level.INFO);
		} else {
			builder.setStatusLevel(Level.WARN);
		}
		
		AppenderComponentBuilder appenderBuilder = builder.newAppender("Stdout", "CONSOLE").addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
		appenderBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern", "%d %-10t %-5level %msg%n%throwable"));
		builder.add(appenderBuilder);
		builder.add(builder.newRootLogger(Level.DEBUG).add(builder.newAppenderRef("Stdout")));
		
		_configuration = builder.build();
		Configurator.initialize(_configuration);
		
//		reinitLogger();
	}
	
	public static void reinitLogger() {
		Configurator.reconfigure(_configuration);
		Configurator.setRootLevel(Level.INFO);
	}
	
}
