package ch.unibe.scg.phd.data.web;

public enum TextInputTypes implements InputTypes{
    
	TEXT("text"),
    PASSWORD("password"),
    EMAIL("email"),
    NUMBER("number"),
    SEARCH("search"),
    URL("url");

    private String _type;
    
    TextInputTypes(String type) {
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
