package ch.unibe.scg.phd.utils;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import ch.unibe.scg.phd.core.BrowserController;
import ch.unibe.scg.phd.io.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    private static final Log _LOG = new Log(FileUtil.class);

    public static byte[] getFileContent(InputStream in) {
        try {
            return IOUtils.toByteArray(in);
        } catch (IOException e) {
            return null;
        }
    }
    
    public static byte[] getFileContent(String path) {
    	try {
			return Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return new byte[0];
    }

    public static byte[] takeScreenshot(WebDriver driver) {
    	return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public static String getFavicon(BrowserController controller, WebDriver driver) {
        try {
            String source = driver.getPageSource();
            return HtmlUtil.getLinkToFavicon(controller, source, driver.getCurrentUrl());
        } catch (MalformedURLException e) {
            _LOG.error("Failed to get favicon, a MalformedURLException occured");
            e.printStackTrace();
        }
		return "";
    }

}
