package ch.unibe.scg.phd.utils;

import ch.unibe.scg.phd.data.exceptions.AttributeNotFoundException;

public class HtmlElement {
    
	private final String _content;

    HtmlElement(String content) {
        _content = content;
    }

    public boolean hasAttribute(String attributeName) {
        return _content.matches("(.)*" + attributeName + "=(.)*");
    }

    public String getAttributeValue(String attributeName) throws AttributeNotFoundException {
        if (!hasAttribute(attributeName)) {
            throw new AttributeNotFoundException("Html Element" + this + "does not contain Attribute" + attributeName);
        }
        int attributeStart = _content.indexOf(attributeName);
        //answer start after first = after start of the attribute name
        String answer = _content.substring(_content.indexOf("=", attributeStart) + 1);
        //answer finishes with first space after start
        if (!answer.startsWith("\"")) {
            answer = answer.split(" ")[0];
        } else {
            answer = answer.split("\"")[1];
        }
        //remove " if present
        answer = answer.replace("\"", "");
        return answer;
    }

    @Override
    public String toString() {
        return _content;
    }
    
}
