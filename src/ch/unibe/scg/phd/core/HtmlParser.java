package ch.unibe.scg.phd.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import ch.unibe.scg.phd.data.deprecated_overlays.Clickable;
import ch.unibe.scg.phd.data.deprecated_overlays.Textbox;
import ch.unibe.scg.phd.data.web.ClickableInputTypes;
import ch.unibe.scg.phd.data.web.Style;
import ch.unibe.scg.phd.data.web.TextInputTypes;
import ch.unibe.scg.phd.io.Log;

public class HtmlParser {
	
    private Log _log = new Log(HtmlParser.class);
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

    public List<Textbox> getTextboxes() {
        List<Textbox> result = new ArrayList<>();
        List<WebElement> elements = filterTextInputElements();
        for (WebElement element: elements) {
            //FIXME sometimes on wikipedia, the location is wrong by paddingLeft
            Point location = element.getLocation();
            Dimension dimension = element.getSize();
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
    	String style = element.getCssValue(Style.FONT_STYLE);
    	String weight = element.getCssValue(Style.FONT_WEIGHT);
    	String size = element.getCssValue(Style.FONT_SIZE);
        String family = element.getCssValue(Style.FONT_FAMILY);
        String color = element.getCssValue(Style.FONT_COLOR);
        

//        font-style
//        font-variant
//        font-weight
//        font-size
//        line-height
//        font-family

        
        
//        StringBuilder builder = new StringBuilder();
//        builder.append(style).append(" ");
//        builder.append(style).append(" ");
        
        
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
                // Failed to find Background color, returning white as fallback
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

        _log.info(String.format("Found [%d] Clickables", inputElements.size()));
        return elements;
    }
}
