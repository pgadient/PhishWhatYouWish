package ch.unibe.scg.phd.utils;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import ch.unibe.scg.phd.core.BrowserController;
import ch.unibe.scg.phd.io.Log;
import ch.unibe.scg.phd.properties.Properties;
import net.sf.image4j.codec.ico.ICODecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileUtil {
	public static File temp;
	
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
//        boolean hasnewtab = prepareBrowser(data, controller);
//        String currentHandle = driver.getWindowHandle();
//        String screenshotHandle;
//        if (hasnewtab) {
//            screenshotHandle = data.getWindowHandle();
//        } else {
//            screenshotHandle = currentHandle;
//        }
    	return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        //driver.switchTo().window(currentHandle);
//        return hasnewtab;
    }

//    private static boolean prepareBrowser(ImageData data, BrowserController controller) {
//        controller.switchToTab(data.getWindowHandle());
//        //TODO to avoid recaptcha we could try to set a reasonable http header instead of seleniums header
//        Command command = new EmptyCommand();
//        for (CommandTypes commandType : CommandTypes.values()) {
//            if (commandType.equalsName(data.getType())) {
//                try {
//                    List<Key> keys = new ArrayList<>();
//                    for (String key : data.getKeys()) {
//                        keys.add(new Key(key));
//                    }
//                    Object[] args = new Object[3];
//                    args[0] = keys;
//                    args[1] = data.getxCoord();
//                    args[2] = data.getyCoord();
//                    command = commandType.getInstance(args);
//                } catch (IllegalAccessException | InstantiationException e) {
//                    _LOG.error(String.format("Failed to get an Instance of [%s]", commandType), e);
//                    command = new EmptyCommand();
//                }
//                break;
//            }
//        }
//        _LOG.info(String.format("Executing Command [%s]", command));
//        return controller.executeCommand(command, data.getClientWidth(), data.getClientHeight());
//    }

//    /**
//     *  Screenshot viewPort Only
//     */
//    private static byte[] takeScreenshotViewPort(WebDriver driver) {
        //driver.switchTo().window(windowHandle);
//        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
//        try {
//            _LOG.info("PNG generated");
//            FileUtils.copyFile(src, new File(Properties.PARAM_SCREENSHOT_BASE_PATH + identifier + "_" + step + "_" + driver.getWindowHandle() + ".png"));
//            _LOG.info("PNG saved");
//        } catch (IOException e) {
//            _LOG.error("Failed to safe screenshot", e);
//        }
//    }

    public static void getFavicon(BrowserController controller, WebDriver driver, int identifier, int step) throws IOException{
        try {
            String source = driver.getPageSource();
            String externalPath = HtmlUtil.getLinkToFavicon(controller, source, driver.getCurrentUrl());
            FileUtil.downloadFavicon(externalPath, identifier, step);
        } catch (MalformedURLException e) {
            _LOG.error("Failed to get favicon, a MalformedURLException occured");
            e.printStackTrace();
        }
    }

    private static void downloadFavicon(String path, int identifier, int step) throws IOException{
        try {
            String format = getImageFormat(path);
            BufferedImage image;
            String localPath = identifier + "_" + step + ".png";
            //TODO set different header to avoid 403
            URL url = new URL(path);
            if (format.equals("ico")) {
                List<BufferedImage> images = ICODecoder.read(url.openStream());
                if (images.size() > 0) {
                    image = images.get(0);
                } else {
                    image = null;
                }
            } else {
                image = ImageIO.read(url);
            }
            if (image == null) {
                throw new IOException("after reading image image is null");
            }
            ImageIO.write(image, "PNG", new File(Properties.PARAM_FAVICONS_BASE_PATH + localPath));
        } catch (IOException e) {
            _LOG.error("Downloading Favicon Failed with an exception");
            throw e;
        }

    }

    private static String getImageFormat(String path) {
        String[] parts = path.split("\\.");
        return parts[parts.length - 1];
    }
    
}
