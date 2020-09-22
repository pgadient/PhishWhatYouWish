package ch.unibe.scg.phd.core;

import ch.unibe.scg.phd.io.Log;
import ch.unibe.scg.phd.properties.Configuration;

public class Sleeper {
    private final Log _log = new Log(Sleeper.class);

    /**
     * Only sleep if application runs in debug mode
     * @param millis the amount of milliseconds to sleep
     */
    public void debugSleep(long millis) {
        if (Configuration.FEATURE_DEBUG_OUTPUT) {
            sleep(millis);
        }
    }

    /**
     * Sleep no mather in which mode application runs
     * @param millis the amount of milliseconds to sleep
     */
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            _log.error(String.format("Failed to sleep for [%d] ms", millis), e);
        }
    }
}
