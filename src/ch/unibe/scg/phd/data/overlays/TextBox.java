package ch.unibe.scg.phd.data.overlays;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

public class TextBox extends Rectangle{
	
    private String _type = "";
    private String _background = "";
    private String _placeholder = "";
    private String _text = "";
    
    private String _paddingLeft;
    private String _paddingRight;
    private String _paddingTop;
    private String _paddingBottom;
    
    private String _borderLeft;
    private String _borderRight;
    private String _borderTop;
    private String _borderBottom;
    
    private String _fontStyle;
    private String _fontWeight;
    private String _fontSize;
    private String _fontFamily;
    private String _fontColor;
    
    public TextBox(Point location, Dimension dimension) {
        super(location, dimension);
    }
    
    public String getType() {
        return _type;
    }
    
    public void setType(String type) {
        _type = type;
    }

    public String getPlaceholder() {
        return _placeholder;
    }
    
    public void setPlaceholder(String placeholder) {
        _placeholder = placeholder;
    }

    public String getBackground() {
        return _background;
    }
    
    public void setBackground(String background) {
    	_background = background;
    }

    public String getText() {
        return _text;
    }
    
    public void setText(String text) {
        _text = text;
    }
    
    public String getPaddingLeft() {
        return _paddingLeft;
    }
    
    public void setPaddingLeft(String paddingLeft) {
        _paddingLeft = paddingLeft;
    }
    
    public String getPaddingRight() {
        return _paddingRight;
    }
    
    public void setPaddingRight(String paddingRight) {
        _paddingRight = paddingRight;
    }
    
    public String getPaddingTop() {
        return _paddingTop;
    }
    
    public void setPaddingTop(String paddingTop) {
        _paddingTop = paddingTop;
    }
    
    public String getPaddingBottom() {
        return _paddingBottom;
    }
    
    public void setPaddingBottom(String paddingBottom) {
        _paddingBottom = paddingBottom;
    }
    
    public String getBorderLeft() {
        return _borderLeft;
    }
    
    public void setBorderLeft(String borderLeft) {
        _borderLeft = borderLeft;
    }
    
    public String getBorderRight() {
        return _borderRight;
    }
    
    public void setBorderRight(String borderRight) {
        _borderRight = borderRight;
    }
    
    public String getBorderTop() {
        return _borderTop;
    }
    
    public void setBorderTop(String borderTop) {
        _borderTop = borderTop;
    }
    
    public String getBorderBottom() {
        return _borderBottom;
    }
    
    public void setBorderBottom(String borderBottom) {
        _borderBottom = borderBottom;
    }
    
    
    public String getFontStyle() {
        return _fontStyle;
    }
    
    public void setFontStyle(String fontStyle) {
        _fontStyle = fontStyle;
    }
    
    
    public String getFontWeight() {
        return _fontWeight;
    }
    
    public void setFontWeight(String fontWeight) {
        _fontWeight = fontWeight;
    }
    
    public String getFontSize() {
        return _fontSize;
    }
    
    public void setFontSize(String fontSize) {
        _fontSize = fontSize;
    }
    
    public String getFontFamily() {
        return _fontFamily;
    }
    
    public void setFontFamily(String fontFamily) {
        _fontFamily = fontFamily;
    }
    
    public String getFontColor() {
        return _fontColor;
    }
    
    public void setFontColor(String fontColor) {
        _fontColor = fontColor;
    }
    
}
