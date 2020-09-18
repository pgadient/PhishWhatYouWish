package ch.unibe.scg.phd.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.CharBuffer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ch.unibe.scg.phd.communication.ServerWebSocket;
import ch.unibe.scg.phd.data.deprecated_overlays.Clickable;
import ch.unibe.scg.phd.data.deprecated_overlays.Textbox;
import ch.unibe.scg.phd.data.web.ClickableInputTypes;
import ch.unibe.scg.phd.data.web.Style;
import ch.unibe.scg.phd.data.web.TextInputTypes;
import ch.unibe.scg.phd.io.Log;
import ch.unibe.scg.phd.properties.Properties;
import ch.unibe.scg.phd.utils.FileUtil;
import ch.unibe.scg.phd.utils.HtmlUtil;
import ch.unibe.scg.phd.utils.HttpUtil;
import ch.unibe.scg.phd.utils.StringUtil;

public class BrowserController {
	
    public final String[] _faviconRel = {"icon", "shortcut icon"};

    private Sleeper _sleeper;
    private WebDriver _driver;
    private String _baseURL;
    private Log _log = new Log(BrowserController.class);
    private boolean _hasnewtab;
    private Dimension _uiSpacing;

	public BrowserController() {
		this(Properties.PARAM_DEFAULT_BASE_URL, true);
	}
    
    public BrowserController(String baseURL) {
    	this(baseURL, true);
    }
    
    public BrowserController(String baseURL, boolean headless) {
    	_sleeper = new Sleeper();
    	System.setProperty(Properties.FIREFOX_DRIVER, System.getProperty("user.dir") + Properties.PATH_DRIVERS + Properties.FIREFOX_DRIVER_WIN64);
        setBaseURL(baseURL);
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
        _uiSpacing = getBrowserUiSpacing();
        System.out.println("UI spacing is: " + _uiSpacing.width + "x" + _uiSpacing.height);
    }
	
	public void initBrowsingInstance() {
        _driver.manage().window().maximize();
        this.openPath("/");
        _hasnewtab = false;
	}
	
    /**
     * @param baseURL the host to be used
     */
    public void setBaseURL(String baseURL) {
        while (baseURL.startsWith("/")) {
            baseURL = baseURL.substring(1);
        }
        while (baseURL.endsWith("/")) {
            baseURL = baseURL.substring(0, baseURL.length() - 1);
        }

        _baseURL = baseURL;
    }

    public String getBaseURL() {
        return _baseURL;
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
    public void openURL (String baseURL, String path) {
        setBaseURL(baseURL);
        openPath(path);
    }

    /**
     * open the path at the current host
     * @param path the path to connect to
     */
    public void openPath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        _driver.get(_baseURL + path);
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
    private void setBrowserToViewportDimension(int clientWidth, int clientHeight) {
    	_driver.manage().window().setSize(new Dimension(clientWidth + _uiSpacing.width, clientHeight + _uiSpacing.height));
        //setDimension(clientWidth, clientHeight);
    }

    private void updateBrowserHeight(int clientHeight) {
        int width = _driver.manage().window().getSize().width;
        WebDriverWait wait = new WebDriverWait(_driver, Duration.ofMillis(30));
        JavascriptExecutor js = (JavascriptExecutor) _driver;
        //Wait for html and body element to be present
        ExpectedCondition<Boolean> condition = ExpectedConditions.and(ExpectedConditions.presenceOfElementLocated(By.tagName("html")), ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        wait.until(condition);
        int height = Math.max(Integer.parseInt(js.executeScript("var body = document.body,\n" +
                "    html = document.documentElement;\n" +
                "\n" +
                "return Math.max( body.scrollHeight, body.offsetHeight, \n" +
                "                       html.clientHeight, html.scrollHeight, html.offsetHeight );").toString()), clientHeight);
        setBrowserToViewportDimension(width, height);
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
    public String getPagePath() {
        String current = _driver.getCurrentUrl();
        try {
            URL url = new URL(current);
            String path = url.getPath();
            if (path == null) {
                return "";
            }
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            String query = url.getQuery();
            if (query == null) {
                return path;
            }
            return path + "?" + query;
        } catch (MalformedURLException e) {
            _log.error(String.format("Failed to create URL Object from current url string [%s]", current));
            return "";
        }
    }

    public void updateTitleAndUrl() {
    	String title = getTitle();
    	String path = getPagePath();
    	ServerWebSocket.sendControlMessage("T:" + title);
    	ServerWebSocket.sendControlMessage("P:" + path);
    }
    
    public byte[] generateScreenshot(int width, int height) {
        setBrowserToViewportDimension(width, height);
        updateBrowserHeight(height);
        updateTitleAndUrl();
        return FileUtil.takeScreenshot(_driver);
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

    public boolean hasNewTab() {
        return _hasnewtab;
    }

    public void switchToTab(String windowHandle) {
        if (!StringUtil.isEmpty(windowHandle) && !_driver.getWindowHandle().equals(windowHandle)) {
            _driver.switchTo().window(windowHandle);
        }
    }

    public String getCurrentTab() {
        return _driver.getWindowHandle();
    }

    public String getCurrentUrl(String windowHandle) {
        String handle = _driver.getWindowHandle();
        _driver.switchTo().window(windowHandle);
        String url = _driver.getCurrentUrl();
        _driver.switchTo().window(handle);
        return url;
    }

    public void openURL(String url) {
        try {
            String host = HttpUtil.getHost(url);
            String protocol = url.substring(0, url.indexOf(host));
            String path = url.substring(url.lastIndexOf(host) + host.length());
            this.openURL(protocol + host, path);
        } catch (MalformedURLException e) {
            _log.error(String.format("Can't connect to [%s], this URL is Malformed", url));
        }
    }

    public List<Clickable> getClickables() {
        List<Clickable> result = new ArrayList<>();
        _log.info("Searching for Clickable Elements");
        List<WebElement> elements = filterClickableElements();
        for (WebElement element: elements) {
            Point location = element.getLocation();
            Dimension dimension = element.getSize();
            _log.info(String.format("[%s] is at Point [%d, %d] with dimension [%d, %d]", element.getTagName(), location.x, location.y, dimension.width, dimension.height));

            Clickable currentClickable = new Clickable(location, dimension);
            result.add(currentClickable);
        }
        return result;
    }

    public List<Textbox> getTextboxes() {
        List<Textbox> result = new ArrayList<>();
        _log.info("Searching for Textboxes");
        List<WebElement> elements = filterTextInputElements();
        for (WebElement element: elements) {
            //FIXME sometimes on wikipedia, the location is wrong by paddingLeft
            Point location = element.getLocation();
            Dimension dimension = element.getSize();
            _log.info(String.format("[%s : %s] is at Point [%d, %d] with dimension [%d, %d]", element.getTagName(), element.getAttribute("type"), location.x, location.y, dimension.width, dimension.height));
            String type = element.getAttribute("type");
            String text = element.getAttribute("value");
            if (text == null) {
                text = "";
            }
            String placeholder = element.getAttribute("placeholder");
            if (placeholder == null) {
                placeholder = "";
            }

            String background = getBackgroundColor(element);
            Map<String, String> padding = getPadding(element);
            Map<String, String> border = getBorder(element);
            Map<String, String> font = getFont(element);

            Textbox currentTextbox = new Textbox(location, dimension, type, padding, placeholder, background, text, border, font);
            result.add(currentTextbox);
        }
        return result;
    }

    private Map<String, String> getFont(WebElement element) {
        String family = element.getCssValue(Style.FONT_FAMILY);
        String size = element.getCssValue(Style.FONT_SIZE);
        String style = element.getCssValue(Style.FONT_STYLE);
        String weight = element.getCssValue(Style.FONT_WEIGHT);
        String color = element.getCssValue(Style.FONT_COLOR);
        Map<String, String> font = new HashMap<>();
        font.put(Style.FONT_FAMILY, family);
        font.put(Style.FONT_SIZE, size);
        font.put(Style.FONT_STYLE, style);
        font.put(Style.FONT_WEIGHT, weight);
        font.put(Style.FONT_COLOR, color);
        return font;
    }

    private Map<String, String> getBorder(WebElement element) {
        String borderLeft = element.getCssValue(Style.BORDER_LEFT_WIDTH);
        String borderRight = element.getCssValue(Style.BORDER_RIGHT_WIDTH);
        String borderTop = element.getCssValue(Style.BORDER_TOP_WIDTH);
        String borderBottom = element.getCssValue(Style.BORDER_BOTTOM_WIDTH);
        Map<String, String> border = new HashMap<>();
        border.put(Style.BORDER_LEFT, borderLeft);
        border.put(Style.BORDER_RIGHT, borderRight);
        border.put(Style.BORDER_TOP, borderTop);
        border.put(Style.BORDER_BOTTOM, borderBottom);
        return border;
    }

    private Map<String, String> getPadding(WebElement element) {
        String paddingLeft = element.getCssValue(Style.PADDING_LEFT);
        String paddingRight = element.getCssValue(Style.PADDING_RIGHT);
        String paddingTop = element.getCssValue(Style.PADDING_TOP);
        String paddingBottom = element.getCssValue(Style.PADDING_BOTTOM);
        Map<String, String> padding = new HashMap<>();
        padding.put(Style.PADDING_LEFT, paddingLeft);
        padding.put(Style.PADDING_RIGHT, paddingRight);
        padding.put(Style.PADDING_TOP, paddingTop);
        padding.put(Style.PADDING_BOTTOM, paddingBottom);
        return padding;
    }

    private String getBackgroundColor(WebElement element) {
        String fallbackColor = "rgba(255, 255, 255, 1)";
        String emptyColor = "rgba(0, 0, 0, 0)";
        String background = fallbackColor;
        WebElement currentElement = element;
        do {
            try {
                background = currentElement.getCssValue("background-color");
                currentElement = currentElement.findElement(By.xpath("./.."));
            } catch (NoSuchElementException e) {
                _log.error("Failed to find Background color, returning white as fallback");
                background = fallbackColor;
            }
        } while (background.endsWith("0)") && !background.equals(emptyColor));
        if (background.equals(emptyColor)) {
            background = fallbackColor;
        }
        if (background.matches("rgba\\([\\d]{1,3}, [\\d]{1,3}, [\\d]{1,3}, 0\\.[\\d]+\\)")) {
            background = background.replaceAll("0\\.[\\d]+\\)", "1)");
        }
        return background;
    }

    private List<WebElement> filterTextInputElements() {
        List<WebElement> elements = _driver.findElements(By.tagName("input"));
        elements = elements
                .stream()
                .filter((WebElement element) -> {
                    String type = element.getAttribute("type");
                    for (TextInputTypes current : TextInputTypes.values()) {
                        if (current.equalsType(type)) {
                            Dimension size = element.getSize();
                            return element.isDisplayed() && size.width > 0 && size.height > 0;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toCollection(ArrayList::new));
        _log.info(String.format("Found [%d] Textboxes", elements.size()));
        return elements;
    }

    //TODO similar to filterInputElements
    private List<WebElement>filterClickableElements() {
        List<WebElement> inputElements = _driver.findElements(By.tagName("input"));
        inputElements = inputElements
                .stream()
                .filter((WebElement element) -> {
                    String type = element.getAttribute("type");
                    for (ClickableInputTypes current : ClickableInputTypes.values()) {
                        if (current.equalsType(type)) {
                            Dimension size = element.getSize();
                            return element.isDisplayed() && size.width > 0 && size.height > 0;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        List<WebElement> elements = _driver.findElements(By.tagName("a"))
                .stream()
                .filter((WebElement element) -> {
                    Dimension size = element.getSize();
                    return element.isDisplayed() && size.width > 0 && size.height > 0;
                })
                .collect(Collectors.toCollection(ArrayList::new));
        List<WebElement> buttons = _driver.findElements(By.tagName("button"))
                .stream()
                .filter((WebElement element) -> {
                    Dimension size = element.getSize();
                    return element.isDisplayed() && size.width > 0 && size.height > 0;
                })
                .collect(Collectors.toCollection(ArrayList::new));
        elements.addAll(buttons);
        elements.addAll(inputElements);

        _log.info(String.format("Found [%d] Clickables", inputElements.size()));
        return elements;
    }
    
    public boolean canFakeHost(String url) {
        return !url.startsWith(_baseURL); // && !url.startsWith(config.getString(StringProperties.AURELIA_BASE));
    }

    public boolean opensInNewTab(String header) {
        return Boolean.parseBoolean(header);
    }
    
    public void sleep(long millis) {
    	_sleeper.sleep(millis);
    }
}
