package ch.unibe.scg.phd.data.exceptions;

public class AttributeNotFoundException extends PhishingException {

	private static final long serialVersionUID = -7877240477454021376L;

	public AttributeNotFoundException(String message) {
        super(message);
    }

    public AttributeNotFoundException(String message, Exception e) {
        super(message, e);
    }
}
