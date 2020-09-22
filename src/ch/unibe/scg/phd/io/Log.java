package ch.unibe.scg.phd.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unibe.scg.phd.properties.Configuration;

public class Log {
    private final Logger _log;

	@SuppressWarnings("rawtypes")
	public Log(Class c) {
        _log = LoggerFactory.getLogger(c);
    }

    /**
     * Only logs a message if debug flag of executor is set to true
     * @param message the message to be logged
     */
    public void debug(String message) {
        if (Configuration.FEATURE_DEBUG_OUTPUT) {
            _log.debug(message);
        }
    }

    public void trace(String message) {
        _log.trace(message);
    }

    public void info(String message) {
        _log.info(message);
    }

    public void info(String message, Exception e) {
        _log.info(message);
        e.printStackTrace();
    }

    public void warn (String message) {
        _log.warn(message);
    }

    public void warn (String message, Exception e) {
        _log.warn(message);
        e.printStackTrace();
    }

    public void error (String message) {
        _log.error(message);
    }

    public void error (String message, Exception e) {
        _log.error(message);
        e.printStackTrace();
    }

}
