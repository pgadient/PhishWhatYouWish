package ch.unibe.scg.phd.data.web;

public enum TextInputTypes {
    
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

    public boolean equalsType(String otherType) {
        return _type.equals(otherType);
    }

    public String toString() {
        return _type;
    }
    
}
