package ch.unibe.scg.phd.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.CharBuffer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.glassfish.grizzly.websockets.WebSocket;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import ch.unibe.scg.phd.communication.ServerWebSocket;
import ch.unibe.scg.phd.communication.requests.ScreenshotRequest;
import ch.unibe.scg.phd.data.deprecated_overlays.Clickable;
import ch.unibe.scg.phd.data.deprecated_overlays.Textbox;
import ch.unibe.scg.phd.io.Log;
import ch.unibe.scg.phd.properties.Properties;
import ch.unibe.scg.phd.utils.FileUtil;
import ch.unibe.scg.phd.utils.HtmlUtil;

public class BrowserController {
	
	private String _url = "";
	private final Object _lock = new Object();
    public final String[] _faviconRel = {"icon", "shortcut icon"};
    private HtmlParser _parser;
    private Sleeper _sleeper;
    private WebDriver _driver;
    final private String _baseUrl;
    private Log _log = new Log(BrowserController.class);
    private Dimension _uiSpacing;
    private CircularFifoQueue<ScreenshotRequest> _screenshotRequestBuffer = new CircularFifoQueue<>(5);
    private int _clientWindowWidth = 0;
    private int _clientWindowHeight = 0;
    
	public BrowserController() {
		this(Properties.PARAM_DEFAULT_BASE_URL, true);
	}
    
    public BrowserController(String baseUrl) {
    	this(baseUrl, true);
    }
    
    public BrowserController(String baseUrl, boolean headless) {
    	// stripping leading and trailing dashes from URL
    	while (baseUrl.startsWith("/")) {
            baseUrl = baseUrl.substring(1);
        }
        while (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        
    	_baseUrl = baseUrl;
    	_sleeper = new Sleeper();
    	initScreenshotReplyManager();
    	
    	// Firefox set up
    	System.setProperty(Properties.FIREFOX_DRIVER, System.getProperty("user.dir") + Properties.PATH_DRIVERS + Properties.FIREFOX_DRIVER_WIN64);
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        if (Properties.FEATURE_ENABLE_ADBLOCK){
            firefoxProfile.addExtension(new File(System.getProperty("user.dir") + Properties.PATH_BROWSER_EXTENSIONS + Properties.FIREFOX_EXTENSION_ADBLOCKPLUS));
        }
        firefoxOptions.setProfile(firefoxProfile);
        if (headless) {
            FirefoxBinary firefoxBinary = new FirefoxBinary();
            firefoxBinary.addCommandLineOptions("--headless");
            firefoxOptions.setBinary(firefoxBinary);
        }
        
        _driver = new FirefoxDriver(firefoxOptions);
        _parser = new HtmlParser(_driver);
        _uiSpacing = getBrowserUiSpacing();
        System.out.println("UI spacing is: " + _uiSpacing.width + "x" + _uiSpacing.height);
        _driver.manage().window().maximize();
        this.openURL(_baseUrl);
    }
	
    public void navigateForward() {
    	_driver.navigate().forward();
    }
    
    public void navigateBack() {
    	_driver.navigate().back();
    }
	
    /**
     * Connects to a given url
     * @param baseURL the host to connect to
     * @param path the path at given host to connect to
     */
    public void openURL (String baseURL) {
        _driver.get(_baseUrl);
    }

    public void performMouseClick(int x, int y) {
    	Actions action = new Actions(_driver);
        WebElement element = _driver.findElement(By.tagName("html"));
        Dimension size = element.getSize();
        int height = HtmlUtil.getHeight(size, _driver);
        action.moveToElement(element, x - size.width / 2, y - (height / 2)).perform();
        action.click().perform();
    }
    
    public void performContextMouseClick(int x, int y) {
    	Actions action = new Actions(_driver);
        WebElement element = _driver.findElement(By.tagName("html"));
        Dimension size = element.getSize();
        int height = HtmlUtil.getHeight(size, _driver);
        action.moveToElement(element, x - size.width / 2, y - (height / 2)).perform();
        action.contextClick().perform();
    }
    
    public void performMouseDown(int x, int y) {
    	Actions action = new Actions(_driver);
        WebElement element = _driver.findElement(By.tagName("html"));
        Dimension size = element.getSize();
        int height = HtmlUtil.getHeight(size, _driver);
        action.moveToElement(element, x - size.width / 2, y - (height / 2)).perform();
        action.clickAndHold().perform();
    }
    
    public void performMouseUp(int x, int y) {
    	Actions action = new Actions(_driver);
        WebElement element = _driver.findElement(By.tagName("html"));
        Dimension size = element.getSize();
        int height = HtmlUtil.getHeight(size, _driver);
        action.moveToElement(element, x - size.width / 2, y - (height / 2)).perform();
        action.release().perform();
    }
    
    public void performMouseMove(int x, int y) {
    	Actions action = new Actions(_driver);
    	action.moveByOffset(x, y).perform();
    }
    
    public void performKeyDown(int keyCode) {
    	Actions action = new Actions(_driver);
    	switch (keyCode) {
    	case 8:
    		action.sendKeys(Keys.BACK_SPACE).perform();
    		break;
    	case 9:
    		action.sendKeys(Keys.TAB).perform();
    		break;
    	case 13:
    		action.sendKeys(Keys.ENTER).perform();
    		break;
    	case 27:
    		action.sendKeys(Keys.ESCAPE).perform();
    		break;
    	case 33:
    		action.sendKeys(Keys.PAGE_UP).perform();
    		break;
    	case 34:
    		action.sendKeys(Keys.PAGE_DOWN).perform();
    		break;
    	case 35:
    		action.sendKeys(Keys.END).perform();
    		break;
    	case 36:
    		action.sendKeys(Keys.HOME).perform();
    		break;
    	case 37:
    		action.sendKeys(Keys.ARROW_LEFT).perform();
    		break;
    	case 38:
    		action.sendKeys(Keys.ARROW_UP).perform();
    		break;
    	case 39:
    		action.sendKeys(Keys.ARROW_RIGHT).perform();
    		break;
    	case 40:
    		action.sendKeys(Keys.ARROW_DOWN).perform();
    		break;
    	case 45:
    		action.sendKeys(Keys.INSERT).perform();
    		break;
    	case 46:
    		action.sendKeys(Keys.DELETE).perform();
    		break;
    	}
    }
    
    public void performKeyPress(int charCode) {
    	Actions action = new Actions(_driver);
    	action.sendKeys(CharBuffer.wrap(new char[]{(char) charCode})).perform();
    }

    public void closeTab() {
        _driver.close();
    }

    public void closeBrowser() {
        _driver.quit();
    }
    
    /**
     * Wait up to the moment that the Browser has finished loading a webpage,
     * Wait for document.readyState to equal "complete"
     * @param driver a WebDriver to controll the browser
     */
    public void awaitBrowser(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(30));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        //Wait for browser to react
        _sleeper.sleep(Properties.PARAM_AWAIT_BROWSER_SLEEP);
        wait.until(driver1 -> js.executeScript("return document.readyState").equals("complete"));
    }

    @SuppressWarnings("unchecked")
	private Dimension getBrowserUiSpacing() {
        JavascriptExecutor js = (JavascriptExecutor) _driver;
        List<Long> windowSize = (ArrayList<Long>) js.executeScript("return [window.outerWidth - window.innerWidth, window.outerHeight - window.innerHeight];");
        return new Dimension(windowSize.get(0).intValue(), windowSize.get(1).intValue());
        //_driver.manage().window().setSize(new Dimension(width + windowSize.get(0).intValue(), height + windowSize.get(1).intValue()));
    }

    /**
     * lets the browser mimic the width and height of the client
     * @param clientWidth the width that the client has, and that the browser should take
     * @param clientHeight the height that the client has, and that the browser should take
     */
    private void setBrowserToViewportDimension(boolean windowSizeChanged, int clientWidth, int clientHeight) {
    	int newWidth = clientWidth + _uiSpacing.width;
    	int newHeight = clientHeight + _uiSpacing.height;
    	if (windowSizeChanged) {
	    	_driver.manage().window().setSize(new Dimension(newWidth, newHeight));
    	}
    	
    	JavascriptExecutor js = (JavascriptExecutor) _driver;
		Wait<WebDriver> wait = new WebDriverWait(_driver, Duration.ofMillis(3000));
		wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
		        return String.valueOf(js.executeScript("return document.readyState")).equals("complete");
		    }
		});
    	
        int inflatedHeight = Integer.parseInt(js.executeScript("return document.documentElement.scrollHeight;").toString());
        inflatedHeight = inflatedHeight + _uiSpacing.height;
        _driver.manage().window().setSize(new Dimension(newWidth, inflatedHeight));
    }
    
    public String getTitle() {
        String title = _driver.getTitle();
        return title == null ? "" : title;
    }
    
    /**
     * Gets the path on the current page including the query params if present
     * e.g. if the current url is
     * "http://abc.ch/path/to/resource?q=query", then
     * the method will return "/path/to/resource?q=query"
     * @return the path with an optional query
     */
    private String getPagePath() {
        String current = _driver.getCurrentUrl();
        try {
            URL url = new URL(current);
            String path = url.getPath();
            if (path == null) {
                return "/";
            }
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            String query = url.getQuery();
            if (query == null) {
                return "/" + path;
            }
            return path + "?" + query;
        } catch (MalformedURLException e) {
            _log.error(String.format("Failed to create URL Object from current url string [%s]", current));
            return "/";
        }
    }

    public void updateTitleAndUrl() {
		String title = getTitle();
		String path = getPagePath();
		
		ServerWebSocket.sendControlMessage("T:" + title);
		ServerWebSocket.sendControlMessage("P:" + path);
		ServerWebSocket.sendControlMessage("S:ScrollUpCmd");
    }
    
    public void enqueueScreenshotReply(ScreenshotRequest req) {
    	_screenshotRequestBuffer.add(req);
    	synchronized(_lock) {
    		_lock.notifyAll();
    	}
    }
    
    private void initScreenshotReplyManager() {
    	Runnable r = new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						ScreenshotRequest req = _screenshotRequestBuffer.remove();
						int width = req.getWidth();
						int height = req.getHeight();
						WebSocket socket = req.getScreenshotSocket();
						
						boolean urlChangeOccurred = urlHasChanged();
						boolean uiChangeOccurred = ((width != _clientWindowWidth) || (height != _clientWindowHeight));
						//System.out.println("url/ui: " + urlChangeOccurred + " / " + uiChangeOccurred);
						if (urlChangeOccurred || uiChangeOccurred) {
							_clientWindowWidth = width;
							_clientWindowHeight = height;
							setBrowserToViewportDimension(uiChangeOccurred, width, height);
							updateTitleAndUrl();
							injectNativeControls();
						}

						byte[] image = FileUtil.takeScreenshot(_driver);
						socket.send(image);
						
						if (_screenshotRequestBuffer.size() > 0) {
							System.out.println("Currently queued screenshots: " + _screenshotRequestBuffer.size());
						}
						
					} catch (Exception e) {
						try {
							synchronized(_lock) {
								_lock.wait();
							}
						} catch (InterruptedException e1) {}
					}
				}
				
		}};
		Thread t = new Thread(r);
		t.start();
    }
    
    public void injectNativeControls() {
    	Runnable r = new Runnable() {
			@Override
			public void run() {
				List<Clickable> clickables = _parser.getClickables();
		    	for (Clickable c : clickables) {
		    		Point l = c.getLocation(); // location
		    		Dimension d = c.getDimension(); // dimension
					ServerWebSocket.sendControlMessage("C:" + l.x + "," + l.y + "|" + d.width + "," + d.height);;
				}
			}};
    	Thread t = new Thread(r);
    	t.start();
    }

    /**
     * downloads the original favicon and saves it in the favicons folder
     * as identifier_step.png
     * @param identifier the id of the user, used in the name of the favicon
     * @param step the step of the user, used in the name of the favicon
     * @throws IOException if the download fails
     */
    public void getFavicon(int identifier, int step) throws IOException {
        FileUtil.getFavicon(this, _driver, identifier, step);
    }

//    public void switchToTab(String windowHandle) {
//        if (!StringUtil.isEmpty(windowHandle) && !_driver.getWindowHandle().equals(windowHandle)) {
//            _driver.switchTo().window(windowHandle);
//        }
//    }

//    public String getCurrentTab() {
//        return _driver.getWindowHandle();
//    }

//    public String getCurrentUrl(String windowHandle) {
//        String handle = _driver.getWindowHandle();
//        _driver.switchTo().window(windowHandle);
//        String url = _driver.getCurrentUrl();
//        _driver.switchTo().window(handle);
//        return url;
//    }

//    public void openURL(String url) {
//        try {
//            String host = HttpUtil.getHost(url);
//            String protocol = url.substring(0, url.indexOf(host));
//            String path = url.substring(url.lastIndexOf(host) + host.length());
//            this.openURL(protocol + host, path);
//        } catch (MalformedURLException e) {
//            _log.error(String.format("Can't connect to [%s], this URL is Malformed", url));
//        }
//    }
 
    public void sleep(long millis) {
    	_sleeper.sleep(millis);
    }
    
    private boolean urlHasChanged() {
    	String newUrl = _driver.getCurrentUrl();
    	if (newUrl.equals(_url)) {
    		return false;
    	} else {
			_url = newUrl; 
    		return true;
    	}
    }
    
    public List<Textbox> getDrawableTextboxes() {
    	return _parser.getTextboxes();
    }
}
