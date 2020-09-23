package ch.unibe.scg.phd.data.overlays;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

/**
 * An object that has a 2D position, a width and a height.
 */
public class Rectangle {

    private final Point _location;
    private final Dimension _dimension;

    public Rectangle(Point location, Dimension dimension) {
        _location = location;
        _dimension = dimension;
    }

    public Dimension getDimension() {
        return _dimension;
    }

    public Point getLocation() {
        return _location;
    }
}
