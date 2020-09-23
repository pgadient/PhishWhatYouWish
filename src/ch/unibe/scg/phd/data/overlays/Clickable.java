package ch.unibe.scg.phd.data.overlays;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

public class Clickable extends Rectangle{
    
	public Clickable(Point location, Dimension dimension) {
        super(location, dimension);
    }
	
}
