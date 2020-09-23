package ch.unibe.scg.phd.properties;

public class Configuration {

	public static final String PATH_STATIC_HTML = "/ch/unibe/scg/phd/staticHtml/";
	public static final String PATH_STATIC_HTML_INDEX = "index.html";
	public static final String PATH_DRIVERS = "/ch/unibe/scg/phd/webdrivers/";
	public static final String PATH_BROWSER_EXTENSIONS = "/ch/unibe/scg/phd/extensions/";
	
	public static final String FIREFOX_DRIVER = "webdriver.gecko.driver";
	public static final String FIREFOX_DRIVER_WIN64 = "geckodriver-v0.27.0-win64.exe";
	public static final String FIREFOX_DRIVER_WIN32 = "geckodriver-v0.27.0-win32.exe";
	public static final String FIREFOX_DRIVER_LIN64 = "geckodriver-v0.27.0-linux64";
	public static final String FIREFOX_DRIVER_LIN32 = "geckodriver-v0.27.0-linux32";
	public static final String FIREFOX_DRIVER_MACOS = "geckodriver-v0.27.0-macos";
	public static final String FIREFOX_EXTENSION_ADBLOCKPLUS = "{d10d0bf8-f5b5-c8b4-a8b2-2b9879e08c5d}.xpi";
	
	public static final long PARAM_AWAIT_BROWSER_SLEEP = 400;
	public static final long PARAM_KEY_STROKE_SLEEP = 100;
	public static final long PARAM_MOUSE_CLICK_SLEEP = 500;
	
    public static final String PARAM_SCREENSHOT_BASE_PATH = "./screenshots/";
    public static final String PARAM_FAVICONS_BASE_PATH = "./favicons/";
    public static final String PARAM_FAVICONS_BASE_PATH_DEFAULT = "./favicons/default/";
    public static final String PARAM_FAVICON_NAME = "favicon.png";
	
}
