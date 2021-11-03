package ch.unibe.scg.phd.properties;

public class Configuration {

	// Login string for selfservice.unibe.ch
	// https://www.selfservice.studis.unibe.ch/Shibboleth.sso/Login?SAMLDS=1&target=https%3A%2F%2Fwww.selfservice.studis.unibe.ch%2Flogin.html%3Ftx_sraguser_pi1%5Baction%5D%3Dauth%26amp%3Btx_sraguser_pi1%5Bcontroller%5D%3DUser&entityID=https%3A%2F%2Faai-idp.unibe.ch%2Fidp%2Fshibboleth
	
	public static final String PATH_STATIC_HTML = "/ch/unibe/scg/phd/staticHtml/";
	public static final String PATH_STATIC_HTML_INDEX = "index.html";
	public static final String PATH_DRIVERS = "/ch/unibe/scg/phd/webdrivers/";
	public static final String PATH_BROWSER_EXTENSIONS = "/ch/unibe/scg/phd/extensions/";
	
	public static final String FIREFOX_DRIVER = "webdriver.gecko.driver";
	public static final String FIREFOX_DRIVER_WIN64 = "geckodriver-v0.27.0-win64.exe";
	public static final String FIREFOX_DRIVER_WIN32 = "geckodriver-v0.27.0-win32.exe";
	public static final String FIREFOX_DRIVER_LIN64 = "geckodriver-v0.27.0-linux64";
	public static final String FIREFOX_DRIVER_LIN32 = "geckodriver-v0.27.0-linux32";
	public static final String FIREFOX_DRIVER_MACOS = "geckodriver-v0.27.0-macos"; // is 32-bit
	public static final String FIREFOX_EXTENSION_ADBLOCKPLUS = "{d10d0bf8-f5b5-c8b4-a8b2-2b9879e08c5d}.xpi";
	
	public static final String CHROME_DRIVER = "webdriver.chrome.driver";
	public static final String CHROME_DRIVER_WIN64 = "chromedriver-v85.0.4183.87-win32.exe";
	public static final String CHROME_DRIVER_WIN32 = "chromedriver-v85.0.4183.87-win32.exe";
	public static final String CHROME_DRIVER_LIN64 = "chromedriver-v85.0.4183.87-linux64";
	public static final String CHROME_DRIVER_LIN32 = "chromedriver-v85.0.4183.87-linux64"; // won't work
	public static final String CHROME_DRIVER_MACOS = "chromedriver-v85.0.4183.87-macos"; // is 64-bit
	public static final String CHROME_EXTENSION_ADBLOCKPLUS = "ogdlpmhglpejoiomcodnpjnfgcpmgale.crx";
	
	public static final long PARAM_AWAIT_BROWSER_SLEEP = 400;
	public static final long PARAM_KEY_STROKE_SLEEP = 100;
	public static final long PARAM_MOUSE_CLICK_SLEEP = 500;
	public static final int PARAM_NUMBER_SCREENSHOT_THREADS = 1;
	
    public static final String PARAM_SCREENSHOT_BASE_PATH = "./screenshots/";
    public static final String PARAM_FAVICONS_BASE_PATH = "./favicons/";
    public static final String PARAM_FAVICONS_BASE_PATH_DEFAULT = "./favicons/default/";
    public static final String PARAM_FAVICON_NAME = "favicon.png";
    
	// additional parameters required to due imperfect text box detection routine
	public static final int TEXTBOX_X_OFFSET = 0; // 4 for SCG website
	public static final int TEXTBOX_WIDTH_OFFSET = 0; // 20 for QRS login website, 15 for QRS login website
	public static final int TEXTBOX_HEIGHT_OFFSET = 0; // -4 for SCG website / -6 for easy chair
	
}
