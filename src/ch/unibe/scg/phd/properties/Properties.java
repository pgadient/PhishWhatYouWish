package ch.unibe.scg.phd.properties;

public class Properties {

	public static final String PATH_STATIC_HTML = "/src/ch/unibe/scg/phd/staticHtml/";
	public static final String PATH_STATIC_HTML_INDEX = "index.html";
	public static final String PATH_DRIVERS = "/src/ch/unibe/scg/phd/webdrivers/";
	public static final String PATH_BROWSER_EXTENSIONS = "/src/ch/unibe/scg/phd/extensions/";
	
	public static final String FIREFOX_DRIVER = "webdriver.gecko.driver";
	public static final String FIREFOX_DRIVER_WIN64 = "geckodriver-v0.27.0-win64.exe";
	public static final String FIREFOX_EXTENSION_ADBLOCKPLUS = "{d10d0bf8-f5b5-c8b4-a8b2-2b9879e08c5d}.xpi";
	
	public static final boolean FEATURE_DEBUG_OUTPUT = true;
	public static final boolean FEATURE_ENABLE_ADBLOCK = true;
	public static final boolean FEATURE_HEADLESS = true;
	
	public static final String PARAM_DEFAULT_BASE_URL = "https://www.google.ch/";
	public static final String PARAM_PHISHING_URL = "http://localhost/";
	public static final long PARAM_AWAIT_BROWSER_SLEEP = 400;
	public static final long PARAM_KEY_STROKE_SLEEP = 100;
	public static final long PARAM_MOUSE_CLICK_SLEEP = 500;
	
    public static final String PARAM_SCREENSHOT_BASE_PATH = "./screenshots/";
    public static final String PARAM_FAVICONS_BASE_PATH = "./favicons/";
    public static final String PARAM_FAVICONS_BASE_PATH_DEFAULT = "./favicons/default/";
    public static final String PARAM_FAVICON_NAME = "favicon.png";
	
//	# Define the file appender
//	log4j.appender.FileAppender=org.apache.log4j.RollingFileAppender
//	log4j.appender.FileAppender.File=output.log
//	log4j.appender.FileAppender.layout = org.apache.log4j.PatternLayout
//	log4j.appender.FileAppender.layout.ConversionPattern=%-4r [%t] %-5p %c %x - %m%n
//
//	# Define the console appender
//	log4j.appender.Console=org.apache.log4j.ConsoleAppender
//	log4j.appender.Console.layout = org.apache.log4j.PatternLayout
//	log4j.appender.Console.layout.ConversionPattern=%-4r [%t] %-5p %c %x - %m%n
//
//	log4j.category.ch.baesi.bachelorthesis.phish.Phisher = ALL, FileAppender
//	# Direct all messages there
//	log4j.rootLogger = ALL, Console
	
}
