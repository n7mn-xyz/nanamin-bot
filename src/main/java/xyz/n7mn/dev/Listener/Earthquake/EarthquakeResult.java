package xyz.n7mn.dev.Listener.Earthquake;

import xyz.n7mn.dev.Listener.Earthquake.eq.Body;
import xyz.n7mn.dev.Listener.Earthquake.eq.Control;
import xyz.n7mn.dev.Listener.Earthquake.eq.Head;

public class EarthquakeResult {

    private Control Control;
    private Head Head;
    private Body Body;

    public Control getControl() {
        return Control;
    }

    public Head getHead() {
        return Head;
    }

    public Body getBody() {
        return Body;
    }
}