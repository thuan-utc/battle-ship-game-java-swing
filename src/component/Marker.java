package component;

import java.awt.*;

public class Marker extends Rectangle {
    private final Color HIT_COLOUR = new Color(219, 23, 23, 180);

    private final Color MISS_COLOUR = new Color(26, 26, 97, 180);

    private final int PADDING = 3;

    private boolean showMarker;

    private Ship shipAtMarker;

    private boolean logicColorIsWhite; // 0 , 1 white and black

    public Marker(int x, int y, int width, int height) {
        super(x, y, width, height);
        reset();
    }

    public void reset() {
        shipAtMarker = null;
        showMarker = false;
    }

    public void mark() {
        if (!showMarker && isShip()) {
            shipAtMarker.destroySection();
        }
        showMarker = true;
    }

    public boolean isShip() {
        return shipAtMarker != null;
    }

    public void setAsShip(Ship ship) {
        this.shipAtMarker = ship;
    }

    public boolean isMarked() {
        return showMarker;
    }

    public Ship getAssociatedShip() {
        return shipAtMarker;
    }

    public void paint(Graphics g) {
        if (!showMarker) return;

        g.setColor(isShip() ? HIT_COLOUR : MISS_COLOUR);
        g.fillRect(position.x + PADDING + 1, position.y + PADDING + 1, width - PADDING * 2, height - PADDING * 2);
    }

    public boolean isLogicColorIsWhite() {
        return logicColorIsWhite;
    }

    public void setLogicColorIsWhite(boolean logicColorIsWhite) {
        this.logicColorIsWhite = logicColorIsWhite;
    }
}
