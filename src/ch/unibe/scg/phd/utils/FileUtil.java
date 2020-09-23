package ch.unibe.scg.phd.utils;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import ch.unibe.scg.phd.core.BrowserController;
import ch.unibe.scg.phd.io.Log;
import ch.unibe.scg.phd.properties.Configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    private static final Log _LOG = new Log(FileUtil.class);
    
	/**
	 * Returns if the application is started from a runnable JAR or via class files.
	 * 
	 * @return a boolean that states if the application was started as JAR.
	 */
	private static boolean isStartedAsJAR() {
		FileUtil fu = new FileUtil();
		String className = fu.getClass().getName().replace('.', '/');
		String classJar = fu.getClass().getResource("/" + className + ".class").toString();
		if (classJar.startsWith("file:")) {
			return false;
		} else {
			return true;
		}
	}
    
    public static String getFullyQualifiedFilePath(String relativeFilePath) {
    	String url = null;
    	if (isStartedAsJAR()) {
    		if (relativeFilePath.startsWith("/")) {
    			relativeFilePath = relativeFilePath.substring(1);
    		}
				url = "jar:file:/" + getJAR_Path() + "!/" + relativeFilePath;
    	} else {
				url = System.getProperty("user.dir") + "/src" + relativeFilePath;
    	}
    	
    	return url;
    }
    
    /**
	 * Returns the JAR path, when started as runnable JAR.
	 * 
	 * @return a String that represents the complete JAR path.
	 */
	private static String getJAR_Path() {
		String path = null;
		URL url = ClassLoader.getSystemResource("ch/unibe/scg/phd/staticHtml/index.html");
		
		try {
			path = URLDecoder.decode(url.toString(), "UTF-8");
			if (isStartedAsJAR()) {
				path = path.substring(10, path.length() - 40);
			} else {
				// application started from source (and not from JAR)
				path = "";
			}
		} catch (UnsupportedEncodingException e) {}
		
		return path;
	}
    
    private static void copyFile(String src, String dst, String fileName) {
    	 try {
    		 InputStream is = FileUtil.class.getResourceAsStream(src + fileName);
	    	 FileOutputStream fout = new FileOutputStream(System.getProperty("user.dir") + dst + fileName);
	    	 
	    	 byte[] b = new byte[1024];
	    	 int numberOfBytes = 0;
	    	 
	    	 while ((numberOfBytes = is.read(b)) != -1) {
	    		 fout.write(b, 0, numberOfBytes);
	    	 }
	    	 
	    	 is.close();
	    	 fout.close(); 
    	 } catch(IOException e) {
    		 e.printStackTrace();
    	 }
    }

    public static void prepareJarTempFolder() {
    	if (isStartedAsJAR()) {
    		File f1 = new File(System.getProperty("user.dir") + "/PhishingOnDemand/extensions");
	    	File f2 = new File(System.getProperty("user.dir") + "/PhishingOnDemand/webdrivers");
	    	File f3 = new File(System.getProperty("user.dir") + "/PhishingOnDemand/wwwroot");
	    	f1.mkdirs();
	    	f2.mkdirs();
	    	f3.mkdirs();
	    	System.out.println(System.getProperty("FileUtil: Created temp folders."));
	    	copyFile(Configuration.PATH_DRIVERS, "/PhishingOnDemand/webdrivers/", Configuration.FIREFOX_DRIVER_WIN64);
	    	copyFile(Configuration.PATH_DRIVERS, "/PhishingOnDemand/webdrivers/", Configuration.FIREFOX_DRIVER_WIN32);
	    	copyFile(Configuration.PATH_DRIVERS, "/PhishingOnDemand/webdrivers/", Configuration.FIREFOX_DRIVER_LIN64);
	    	copyFile(Configuration.PATH_DRIVERS, "/PhishingOnDemand/webdrivers/", Configuration.FIREFOX_DRIVER_LIN32);
	    	copyFile(Configuration.PATH_DRIVERS, "/PhishingOnDemand/webdrivers/", Configuration.FIREFOX_DRIVER_MACOS);
	    	System.out.println(System.getProperty("FileUtil: Extracted webdriver files."));
	    	copyFile(Configuration.PATH_BROWSER_EXTENSIONS, "/PhishingOnDemand/extensions/", Configuration.FIREFOX_EXTENSION_ADBLOCKPLUS);
	    	System.out.println(System.getProperty("FileUtil: Extracted browser extensions."));
	    	copyFile(Configuration.PATH_STATIC_HTML, "/PhishingOnDemand/wwwroot/", Configuration.PATH_STATIC_HTML_INDEX);
	    	System.out.println(System.getProperty("FileUtil: Extracted static HTML content."));
    	}
    }
    
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

    public static String getFullyQualifiedDriverPath(String fileName) {
    	if (isStartedAsJAR()) {
    		return System.getProperty("user.dir") + "/PhishingOnDemand/webdrivers/" + fileName;
    	} else {
    		return getFullyQualifiedFilePath(Configuration.PATH_DRIVERS + fileName);
    	}
    }
    
    public static String getFullyQualifiedExtensionsPath(String fileName) {
    	if (isStartedAsJAR()) {
    		return System.getProperty("user.dir") + "/PhishingOnDemand/extensions/" + fileName;
    	} else {
    		return getFullyQualifiedFilePath(Configuration.PATH_BROWSER_EXTENSIONS + fileName);
    	}
    }
    
    public static String getFullyQualifiedHttpServerRoot() {
    	if (isStartedAsJAR()) {
    		return System.getProperty("user.dir") + "/PhishingOnDemand/wwwroot/";
    	} else {
    		return System.getProperty("user.dir") + "/src" + Configuration.PATH_STATIC_HTML;
    	}
    }
}
