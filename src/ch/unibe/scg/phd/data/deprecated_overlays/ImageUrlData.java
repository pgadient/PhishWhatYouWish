package ch.unibe.scg.phd.data.deprecated_overlays;

public class ImageUrlData {
	
    private int _id;
    private String _currentUrl;
    private int _clientWidth;
    private int _clientHeight;
    private String _windowHandle;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public String getCurrentUrl() {
        return _currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        _currentUrl = currentUrl;
    }

    public int getClientWidth() {
        return _clientWidth;
    }

    public void setClientWidth(int clientWidth) {
        _clientWidth = clientWidth;
    }

    public int getClientHeight() {
        return _clientHeight;
    }

    public void setClientHeight(int clientHeight) {
        _clientHeight = clientHeight;
    }

    public String getWindowHandle() {
        return _windowHandle;
    }

    public void setWindowHandle(String windowHandle) {
        _windowHandle = windowHandle;
    }
    
}
