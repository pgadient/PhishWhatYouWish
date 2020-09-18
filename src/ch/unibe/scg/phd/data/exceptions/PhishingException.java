package ch.unibe.scg.phd.data.exceptions;

public class PhishingException extends Exception {

	private static final long serialVersionUID = -254846553358124800L;

	public PhishingException(String message) {
        super(message);
    }

    public PhishingException(String message, Exception e) {
        super(message, e);
    }
}
