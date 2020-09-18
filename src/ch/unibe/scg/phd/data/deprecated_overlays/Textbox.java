package ch.unibe.scg.phd.data.deprecated_overlays;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.util.Map;

public class Textbox extends Rectangle{
	
    private final Map<String, String> _padding;
    private final String _type;
    private final String _background;
    private final String _placeholder;
    private final String _text;
    private final Map<String, String> _border;
    private final Map<String, String> _font;

    public Textbox(Point location, Dimension dimension, String type, Map<String, String> padding, String placeholder, String background, String text, Map<String, String> border, Map<String, String> font) {
        super(location, dimension);
        _type = type;
        _padding = padding;
        _placeholder = placeholder;
        _background = background;
        _text = text;
        _border = border;
        _font = font;
    }

    public String getType() {
        return _type;
    }

    public Map<String, String> getPadding() {
        return _padding;
    }

    public String getPlaceholder() {
        return _placeholder;
    }

    public String getBackground() {
        return _background;
    }

    public String getText() {
        return _text;
    }

    public Map<String, String> getBorder() {
        return _border;
    }

    public Map<String, String> getFont() {
        return _font;
    }
}
