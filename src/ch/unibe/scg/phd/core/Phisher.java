package ch.unibe.scg.phd.core;

import ch.unibe.scg.phd.io.Log;

public class Phisher {
    private final Log _log = new Log(Phisher.class);

    public void doPhish(byte[] image, BrowserController controller) {
        //Add Phishing Functionality here
        //String windowHandle = data.getWindowHandle() != null ? data.getWindowHandle() : controller.getCurrentTab();
        _log.info("---------------------------------------------------------------");
        //_log.info(String.format("page: %s", controller.getCurrentUrl(windowHandle)));
        //_log.info(String.format("user: %s", data.getId()));
        //_log.info(String.format("user action: %s",data.getType()));
//        switch (data.getType()) {
//            case "mouseclick":
//                _log.info(String.format("x Coordinate: %d", data.getxCoord()));
//                _log.info(String.format("y Coordinate: %d", data.getyCoord()));
//                break;
//            case "keystroke":
//                _log.info(String.format("Pressed Key(s): %s", data.getKeys()));
//                break;
//        }
        _log.info("---------------------------------------------------------------");
    }
}
