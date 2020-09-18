package ch.unibe.scg.phd.utils;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import ch.unibe.scg.phd.core.BrowserController;
import ch.unibe.scg.phd.data.exceptions.AttributeNotFoundException;
import ch.unibe.scg.phd.io.Log;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class HtmlUtil {
    private static final Log _LOG = new Log(HtmlUtil.class);
    private static final String _HTTP = "http://";
    private static final String _HTTPS = "https://";

    /**
     * Get the URL to the favicon of the original page
     * @param source the markup of the original page
     * @param currentUrl the URL of the original page
     * @return a URL to the FAVICON of the original page
     * @throws MalformedURLException If the currentUrl is invalid
     */
    public static String getLinkToFavicon(BrowserController controller, String source, String currentUrl) throws MalformedURLException {
        String tempLink = getLinkToFaviconAsInDom(controller, source);
        String host = HttpUtil.getHost(currentUrl);
        if (!(tempLink.startsWith(_HTTP) || tempLink.startsWith(_HTTPS))) {
            if (tempLink.startsWith("/")) {
                tempLink = host + tempLink;
            } else {
                tempLink = host + "/" + tempLink;
            }
            if (currentUrl.startsWith(_HTTP)) {
                tempLink = _HTTP + tempLink;
            }
            if (currentUrl.startsWith(_HTTPS)) {
                tempLink = _HTTPS + tempLink;
            }
        }
        return tempLink;
    }

    private static String getLinkToFaviconAsInDom(BrowserController controller, String source) {
        String fallback = "favicon.ico";
        try {
            String tagName = "link";
            String linkAttributeName = "href";
            String filterAttributeName = "rel";
            List<HtmlElement> tags = getElements(source, tagName);
            for (HtmlElement tag : tags) {
                String attribute = "";
                if (tag.hasAttribute(filterAttributeName)) {
                    attribute = tag.getAttributeValue(filterAttributeName);
                    if ((attribute.equals(controller._faviconRel[0]) || attribute.equals(controller._faviconRel[1])) && tag.hasAttribute(linkAttributeName)) {
                        return tag.getAttributeValue(linkAttributeName);
                    }
                }
            }
            return fallback;
        } catch (AttributeNotFoundException e) {
            _LOG.error(String.format("Failed to get href to Favicon, continue with fallback: [%s]", fallback));
            return fallback;
        }
    }

    private static List<HtmlElement> getElements(String source, String tag) {
        String remainingSoure = source;
        List<HtmlElement> tags = new ArrayList<>();
        boolean finished = false;
        do {
            String[] results = getFirstElement(remainingSoure, tag);
            if (results[0].equals("")) {
                finished = true;
            } else {
                tags.add(new HtmlElement(results[0]));
                remainingSoure = results[1];
            }
        } while (!finished);
        return tags;
    }

    private static String[] getFirstElement(String source, String tag) {
        int start = source.indexOf("<" + tag);
        int end;
        if (start < 0) {
            return new String[]{"", source};
        }
        //remove everything infront of the opening tag
        String answer = source.substring(start);
        //remove everything after closing tag if closing tag exists
        if (answer.contains("</" + tag + ">")) {
            end = answer.indexOf("</" + tag + ">") + tag.length() + 3;
            answer = answer.substring(0, end);
        }
        //remove everything after opening tag if closing tag doesn't exist
        else {
            end = answer.indexOf(">") + 1;
            if (end < 0) {
                return new String[]{"", source};
            }
            answer = answer.substring(0, end);
        }
        //return the complete tag and the source string without the extracted tag
        return new String[]{answer, source.replace(answer, "")};
    }

    /**
     * Determines the visible height of a {@link org.openqa.selenium.WebElement}.
     * This is the height of the WebElement if you only look at the part that is inside the browsers viewport
     * @param size the dimension of the WebElement
     * @param driver the WebDriver to controll the browser
     * @return the minimum of the height of the browser and the height of the WebElement
     */
    public static int getHeight(Dimension size, WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        int result = Integer.parseInt(((Long)js.executeScript("return document.documentElement.clientHeight")).toString());
        return Math.min(size.height, result);
    }
}
