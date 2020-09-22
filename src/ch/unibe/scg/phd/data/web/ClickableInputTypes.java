package ch.unibe.scg.phd.data.web;

public enum ClickableInputTypes {
    
	BUTTON("button"),
    SUBMIT("submit");

    private String _type;

    ClickableInputTypes(String type) {
        _type = type;
    }

    public boolean equalsType(String otherType) {
        return _type.equals(otherType);
    }

    public String toString() {
        return _type;
    }

}
