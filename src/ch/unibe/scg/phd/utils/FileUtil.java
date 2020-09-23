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
    
    private static String getOsType() {
    	String name = System.getProperty("os.name");
    	String bitness = System.getProperty("os.arch");
    	
    	if (name.toLowerCase().contains("windows")) {
    		// Snippet from Stack Overflow:
    		// https://stackoverflow.com/questions/4748673/how-can-i-check-the-bitness-of-my-os-using-java-j2se-not-os-arch#5940770
    		String arch = System.getenv("PROCESSOR_ARCHITECTURE");
        	String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
        	int realBitness = arch != null && arch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64") ? 64 : 32;
        	if (realBitness == 64) {
        		System.out.println("FileUtil: Detected Windows x64 OS.");
        		return "Windows64";
        	} else {
        		System.out.println("FileUtil: Detected Windows x86 OS.");
        		return "Windows32";
        	}
    	} else if (name.toLowerCase().contains("nux")) {
    		if (bitness.contains("64")) {
    			System.out.println("FileUtil: Detected Linux x64 OS.");
        		return "Linux64";
        	} else {
        		System.out.println("FileUtil: Detected Linux x86 OS.");
        		return "Linux32";
        	}
    	} else {
			System.out.println("FileUtil: Detected macOS.");
    		return "macOS";
    	}
    }
    
    
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
    	boolean jarRun = isStartedAsJAR();
    	
    	if (jarRun) {
    		System.out.println("FileUtil: Application started from JAR file.");
    	} else {
    		System.out.println("FileUtil: Application started from source code.");
    	}
    	
    	if (jarRun) {
    		File f1 = new File(System.getProperty("user.dir") + "/PhishingOnDemand/extensions");
	    	File f2 = new File(System.getProperty("user.dir") + "/PhishingOnDemand/webdrivers");
	    	File f3 = new File(System.getProperty("user.dir") + "/PhishingOnDemand/wwwroot");
	    	f1.mkdirs();
	    	f2.mkdirs();
	    	f3.mkdirs();
	    	System.out.println("FileUtil: Created temp folders.");
	    	copyFile(Configuration.PATH_DRIVERS, "/PhishingOnDemand/webdrivers/", Configuration.FIREFOX_DRIVER_WIN64);
	    	copyFile(Configuration.PATH_DRIVERS, "/PhishingOnDemand/webdrivers/", Configuration.FIREFOX_DRIVER_WIN32);
	    	copyFile(Configuration.PATH_DRIVERS, "/PhishingOnDemand/webdrivers/", Configuration.FIREFOX_DRIVER_LIN64);
	    	copyFile(Configuration.PATH_DRIVERS, "/PhishingOnDemand/webdrivers/", Configuration.FIREFOX_DRIVER_LIN32);
	    	copyFile(Configuration.PATH_DRIVERS, "/PhishingOnDemand/webdrivers/", Configuration.FIREFOX_DRIVER_MACOS);
	    	System.out.println("FileUtil: Extracted webdriver files.");
	    	copyFile(Configuration.PATH_BROWSER_EXTENSIONS, "/PhishingOnDemand/extensions/", Configuration.FIREFOX_EXTENSION_ADBLOCKPLUS);
	    	System.out.println("FileUtil: Extracted browser extensions.");
	    	copyFile(Configuration.PATH_STATIC_HTML, "/PhishingOnDemand/wwwroot/", Configuration.PATH_STATIC_HTML_INDEX);
	    	System.out.println("FileUtil: Extracted static HTML content.");
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
    
    public static String getAppropriateDriver() {
    	String osName = getOsType();
    	switch (osName) {
    	case "Windows64":
    		return Configuration.FIREFOX_DRIVER_WIN64;
    	case "Windows32":
    		return Configuration.FIREFOX_DRIVER_WIN32;
    	case "Linux64":
    		return Configuration.FIREFOX_DRIVER_LIN64;
    	case "Linux32":
    		return Configuration.FIREFOX_DRIVER_LIN32;
    	case "macOS":
    		return Configuration.FIREFOX_DRIVER_MACOS;
    	default:
    		// we assume that linux distributions with flawed identifiers exist
    		return Configuration.FIREFOX_DRIVER_LIN32;
    	}
    	
    }
}
