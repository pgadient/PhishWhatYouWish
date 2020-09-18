package ch.unibe.scg.phd.data.web;

public enum ClickableInputTypes implements InputTypes{
    
	BUTTON("button"),
    SUBMIT("submit");

    private String _type;

    ClickableInputTypes(String type) {
        _type = type;
    }

    @Override
    public boolean equalsType(String otherType) {
        return _type.equals(otherType);
    }

    @Override
    public String toString() {
        return _type;
    }

}
