package ch.unibe.scg.phd.utils;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpUtil {

    public static String getHost(URL request) {
        String host = request.toString();
        String uri;
		try {
			uri = request.toURI().toString();
			if (uri != null && !uri.equals("/")) {
	            host = host.replace(uri, "");
	        }
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        
        host = host.replace("?", "");
        String query = request.getQuery();
        if (query != null) {
            host = host.replace(query, "");
        }
        return host;
    }

    public static String getHost(String url) throws MalformedURLException {
        return new URL(url).getHost();
    }
    
}
