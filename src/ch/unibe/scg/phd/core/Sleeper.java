package ch.unibe.scg.phd.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sleeper {
	
	private static Logger _LOG = LoggerFactory.getLogger(Sleeper.class);

    /**
     * Sleep no mather in which mode application runs
     * @param millis the amount of milliseconds to sleep
     */
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            _LOG.error("Failed to sleep for " + millis + " ms");
        }
    }
    
}
