package ch.unibe.scg.phd.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FileUtils;

import ch.unibe.scg.phd.communication.ServerWebSocket;
import ch.unibe.scg.phd.data.overlays.Clickable;
import ch.unibe.scg.phd.data.overlays.TextBox;
import ch.unibe.scg.phd.properties.Configuration;
import ch.unibe.scg.phd.utils.FileUtil;
import ch.unibe.scg.phd.utils.HtmlUtil;

public class BrowserController {
	
	private static Logger _LOG = LoggerFactory.getLogger(BrowserController.class);
	private HtmlParser _parser;
    private Sleeper _sleeper;	   
    private WebDriver _driver;
	private Dimension _uiSpacing;
    private FirefoxOptions _ffOptions;
    //private ChromeOptions _crOptions;
    private ConcurrentHashMap<Integer, String> _websiteMappings = new ConcurrentHashMap<Integer, String>();
	private String _url = "";
	final private String _baseUrl;
    public final String[] _faviconRel = {"icon", "shortcut icon"};
    private int _clientWindowWidth = 0;
    private int _clientWindowHeight = 0;
    private boolean _isExecuting = false;
    private boolean _initialCall = true;
    private SecureRandom _random = new SecureRandom();
    
    
    public BrowserController(String baseUrl, boolean headless, boolean adblock, boolean debug) {
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
    	
    	//Firefox set up
    	System.setProperty(Configuration.FIREFOX_DRIVER, FileUtil.getFullyQualifiedDriverPath(FileUtil.getAppropriateDriver("firefox")));
        _ffOptions = new FirefoxOptions();
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        if (adblock){
            firefoxProfile.addExtension(new File(FileUtil.getFullyQualifiedExtensionsPath(Configuration.FIREFOX_EXTENSION_ADBLOCKPLUS)));
        }
        _ffOptions.setProfile(firefoxProfile);
        if (headless) {
            FirefoxBinary firefoxBinary = new FirefoxBinary();
            firefoxBinary.addCommandLineOptions("--headless");
            _ffOptions.setBinary(firefoxBinary);
            firefoxBinary.addCommandLineOptions("--log-level=1");
        }
        
        //Chrome set up
        /*
		system.setProperty(Configuration.CHROME_DRIVER, FileUtil.getFullyQualifiedDriverPath(FileUtil.getAppropriateDriver("chrome")));
		_crOptions = new ChromeOptions();
		_crOptions.addArguments("--no-sandbox");
        */
        
        createFaviconFolder();
        //updateIndexHtml();
        initBrowser();
    }
    
    public void createFaviconFolder() {
    	File faviconFolder = new File(FileUtil.getFullyQualifiedHttpServerRoot() + "favicons");
    	if(faviconFolder.exists()) {
        	try {
				FileUtils.deleteDirectory(faviconFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        faviconFolder.mkdirs();
    }
	
    public void initBrowser() {
    	_driver = new FirefoxDriver(_ffOptions);
    	//_driver = new ChromeDriver(_crOptions);
    	_parser = new HtmlParser(_driver);
        _uiSpacing = getBrowserUiSpacing();
        _LOG.info("UI spacing is: " + _uiSpacing.width + "x" + _uiSpacing.height);
        _driver.manage().window().maximize();
        this.openURL(_baseUrl);
        updateIndexHtml(prepareNextFavicon());
    }
    
    public void restartHeadlessBrowser() {
    	_driver.quit();
    	initBrowser();
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
    
    public void performStringInput(String input) {
    	Actions action = new Actions(_driver);
    	action.sendKeys(input).perform();
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
        _sleeper.sleep(Configuration.PARAM_AWAIT_BROWSER_SLEEP);
        wait.until(driver1 -> js.executeScript("return document.readyState").equals("complete"));
    }

    @SuppressWarnings("unchecked")
	private Dimension getBrowserUiSpacing() {
        JavascriptExecutor js = (JavascriptExecutor) _driver;
        List<Long> windowSize = (ArrayList<Long>) js.executeScript("return [window.outerWidth - window.innerWidth, window.outerHeight - window.innerHeight];");
        return new Dimension(windowSize.get(0).intValue(), windowSize.get(1).intValue());
    }

    /**
     * lets the browser mimic the width and height of the client
     * @param clientWidth the width that the client has, and that the browser should take
     * @param clientHeight the height that the client has, and that the browser should take
     */
     void setBrowserToViewportDimension(boolean windowSizeChanged, int clientWidth, int clientHeight) {
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
            if (path == null || path.equals("") || path.equals("/")) {
                return "";
            }
            String query = url.getQuery();
            if (query == null) {
                return path;
            }
            return path + "?" + query;
        } catch (MalformedURLException e) {
            _LOG.warn("Failed to create URL Object from current url string " + current);
            return "";
        }
    }

    public void updateTitleAndUrlAndFavicon(boolean needsReset) {
    	
    	// Avoid issues with multithreading.
    	while(_isExecuting) {
    		try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	_isExecuting = true;
    	
		if (needsReset) {
			updateIndexHtml(prepareNextFavicon());
			ServerWebSocket.sendControlMessage("R:ResetCmd");
			ServerWebSocket.sendControlMessage("P:" + getPagePath());
		} else {
    		ServerWebSocket.sendControlMessage("T:" + getTitle());
    		//String faviconUrl = FileUtil.getFavicon(this, _driver);
        	//prepareFavicon(faviconUrl);
    	    //updateIndexHtml();
		}
		_isExecuting = false;
    }
    
    public int prepareNextFavicon() {
    	String faviconUrl = FileUtil.getFavicon(this, _driver);
    	int _faviconID = _random.nextInt(42000);
    	this._websiteMappings.put(_faviconID, faviconUrl);
    	String localIcon = _faviconID + ".ico";
    	InputStream in;
		try {
			in = new URL(faviconUrl).openStream();
			Files.copy(in, Paths.get(FileUtil.getFullyQualifiedHttpServerRoot(), localIcon), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return _faviconID;
    }
       
    private void updateIndexHtml(int currentFaviconValue)  {
    	File file = new File(FileUtil.getFullyQualifiedHttpServerRoot() + "index.html");
    	char[] buffer = new char[50000];
    	try {
			FileReader fr = new FileReader(file);
			try {
				fr.read(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	String fileContent = new String(buffer);
    	String newFileContent = "";
    	Pattern p = Pattern.compile("<link rel=\"shortcut icon\" type=\"image/jpg\" href=\"http://localhost:8080/" + "\\d+\\" + ".ico\"/>");
    	if(_initialCall) {
    		newFileContent = p.matcher(fileContent).replaceFirst("<link rel=\"shortcut icon\" type=\"image/jpg\" href=\"http://localhost:8080/" + currentFaviconValue + ".ico\"/>");
    		_initialCall = false;
    	} else {
    		newFileContent = p.matcher(fileContent).replaceFirst("<link rel=\"shortcut icon\" type=\"image/jpg\" href=\"http://localhost:8080/" + currentFaviconValue + ".ico\"/>");
    	}
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(newFileContent);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
    }
        
    private void initScreenshotReplyManager() {
    	for (int i = 0; i < Configuration.PARAM_NUMBER_SCREENSHOT_THREADS; i++) {
    		spawnScreenshotThread();
		}
    	
    	_LOG.warn("Spawned " + Configuration.PARAM_NUMBER_SCREENSHOT_THREADS + " screenshot threads.");
    }
    
    private void spawnScreenshotThread() {
    	Runnable r = new Runnable() {
			@Override
			public void run() {
				while (true) {
					Dimension d = null;
					
					// ServerWebSocket might not be ready already.
					try {
						d = ServerWebSocket.getClientDimension();
						Thread.sleep(50);

						if (d != null) {
							int width = d.getWidth();
							int height = d.getHeight();

							boolean uiChangeOccurred = ((width != _clientWindowWidth) || (height != _clientWindowHeight));
							if (uiChangeOccurred) {
								_clientWindowWidth = width;
								_clientWindowHeight = height;
								setBrowserToViewportDimension(uiChangeOccurred, width, height);
							}
							
							boolean urlChangeOccurred = urlHasChanged();
							//_LOG.warn("url/ui: " + urlChangeOccurred + " / " + uiChangeOccurred);
							if (urlChangeOccurred) {
								updateTitleAndUrlAndFavicon(true);
								injectNativeControls();
							}
	
							// Google Chrome webdriver needs scrolling for full capture
							// unavailable full page screenshots are a won't fix issue: 
							// https://bugs.chromium.org/p/chromedriver/issues/detail?id=294
//							byte[] image = Shutterbug.shootPage(_driver, Capture.FULL, true).getBytes();
							byte[] image = FileUtil.takeScreenshot(_driver);
							
							ServerWebSocket.sendImage(image);
						}
					} catch (Exception e) {}
				}
		}};
		Thread t = new Thread(r);
		t.start();
    }
    
    public void injectNativeControls() {
    	Runnable rClickables = new Runnable() {
			@Override
			public void run() {
//				ServerWebSocket.sendControlMessage("Y:busy");
				
				List<Clickable> clickables = _parser.getClickables();
		    	for (Clickable c : clickables) {
		    		Point l = c.getLocation(); // location
		    		Dimension d = c.getDimension(); // dimension
					ServerWebSocket.sendControlMessage("C:" + l.x + "," + l.y + "|" + d.width + "," + d.height);
				}
		    	
		    	List<TextBox> textboxes = _parser.getTextboxes();
		    	for (TextBox t : textboxes) {
		    		Point l = t.getLocation();
		    		Dimension d = t.getDimension();
		    		StringBuilder tConfig = new StringBuilder();
		    		tConfig.append(l.x).append(",").append(l.y).append("||");
		    		tConfig.append(d.width).append(",").append(d.height).append("||");
		    		tConfig.append(t.getType()).append("||");
		    		tConfig.append(t.getBackground()).append("||");
		    		tConfig.append(t.getPlaceholder()).append("||");
		    		tConfig.append(t.getText()).append("||");
		    		tConfig.append(t.getBorderLeft()).append("|!|").append(t.getBorderBottom()).append("|!|").append(t.getBorderRight()).append("|!|").append(t.getBorderTop()).append("||");
		    		tConfig.append(t.getPaddingLeft()).append("|!|").append(t.getPaddingBottom()).append("|!|").append(t.getPaddingRight()).append("|!|").append(t.getPaddingTop()).append("||");
		    		tConfig.append(t.getFontColor()).append("|!|").append(t.getFontFamily()).append("|!|").append(t.getFontSize()).append("|!|").append(t.getFontStyle()).append("|!|").append(t.getFontWeight());
					ServerWebSocket.sendControlMessage("I:" + tConfig.toString());
				}
		    	
//		    	ServerWebSocket.sendControlMessage("Z:endbusy");
			}};
    	Thread tClickables = new Thread(rClickables);
    	tClickables.start();
    }

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
    
    public List<TextBox> getDrawableTextboxes() {
    	return _parser.getTextboxes();
    }
    
    public void dimensionCheck() {
    	Dimension d = null;
    	try {
    		d = ServerWebSocket.getClientDimension();
    		Thread.sleep(50);

    		if (d != null) {
    			int width = d.getWidth();
    			int height = d.getHeight();

    			boolean uiChangeOccurred = true;
    			if (uiChangeOccurred) {
    				_clientWindowWidth = width;
    				_clientWindowHeight = height;
    				setBrowserToViewportDimension(uiChangeOccurred, width, height);
    			}
    		}
    	} catch (Exception e) {}
    }

	public void navigateTo(int faviconId) {
		this.openURL(_websiteMappings.get(faviconId));
	}
}
