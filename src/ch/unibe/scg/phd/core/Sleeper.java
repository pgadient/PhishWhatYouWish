package ch.unibe.scg.phd.core;

import ch.unibe.scg.phd.io.Log;

public class Sleeper {
    private final Log _log = new Log(Sleeper.class);

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
