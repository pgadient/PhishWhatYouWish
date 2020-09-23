package ch.unibe.scg.phd.core;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unibe.scg.phd.data.overlays.Clickable;
import ch.unibe.scg.phd.data.overlays.TextBox;
import ch.unibe.scg.phd.data.web.ClickableInputTypes;
import ch.unibe.scg.phd.data.web.TextInputTypes;
import ch.unibe.scg.phd.properties.WebStyles;

public class HtmlParser {
	
	private static Logger _LOG = LoggerFactory.getLogger(HtmlParser.class);
    
    private WebDriver _driver;
	
	public HtmlParser(WebDriver driver) {
		_driver = driver;
	}
	
	public List<Clickable> getClickables() {
        List<Clickable> result = new ArrayList<>();
        List<WebElement> elements = filterClickableElements();
        for (WebElement element: elements) {
            Point location = element.getLocation();
            Dimension dimension = element.getSize();
            Clickable currentClickable = new Clickable(location, dimension);
            result.add(currentClickable);
        }
        return result;
    }

    public List<TextBox> getTextboxes() {
        List<TextBox> result = new ArrayList<>();
        List<WebElement> elements = filterTextInputElements();
        for (WebElement element: elements) {
            Point location = element.getLocation();
            Dimension dimension = element.getSize();

            TextBox t = new TextBox(location, dimension);
            t.setFontStyle(element.getCssValue(WebStyles.FONT_STYLE));
            t.setFontWeight(element.getCssValue(WebStyles.FONT_WEIGHT));
            t.setFontSize(element.getCssValue(WebStyles.FONT_SIZE));
            t.setFontFamily(element.getCssValue(WebStyles.FONT_FAMILY));
            t.setFontColor(element.getCssValue(WebStyles.FONT_COLOR));
            
            t.setBorderLeft(element.getCssValue(WebStyles.BORDER_LEFT_WIDTH));
            t.setBorderRight(element.getCssValue(WebStyles.BORDER_RIGHT_WIDTH));
            t.setBorderTop(element.getCssValue(WebStyles.BORDER_TOP_WIDTH));
            t.setBorderBottom(element.getCssValue(WebStyles.BORDER_BOTTOM_WIDTH));
            
            t.setPaddingLeft(element.getCssValue(WebStyles.PADDING_LEFT));
            t.setPaddingRight(element.getCssValue(WebStyles.PADDING_RIGHT));
            t.setPaddingTop(element.getCssValue(WebStyles.PADDING_TOP));
            t.setPaddingBottom(element.getCssValue(WebStyles.PADDING_BOTTOM));
            
            t.setBackground(getBackgroundColor(element));
            t.setType(element.getAttribute("type"));
            
            String text = element.getAttribute("value");
            if (text == null) {
                text = "";
            }
            t.setText(text);
            
            String placeholder = element.getAttribute("placeholder");
            if (placeholder == null) {
                placeholder = "";
            }
            t.setPlaceholder(placeholder);
            
            result.add(t);
        }
        return result;
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
                // failed to find background color, returning white as fallback
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

        _LOG.debug("Found " + inputElements.size() + " clickables");
        return elements;
    }
}
