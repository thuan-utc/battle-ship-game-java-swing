package component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Ship {
    public enum ShipPlacementColour {Valid, Invalid, Placed}

    ;

    private Position gridPosition;

    private Position drawPosition;

    private int segments; // length of ship

    private boolean isSideways; // vertical or horizontal

    private int destroyedSections; // number of ship's cell has been destroyed

    private ShipPlacementColour shipPlacementColour;

    public Ship(Position gridPosition, Position drawPosition, int segments, boolean isSideways) {
        this.gridPosition = gridPosition;
        this.drawPosition = drawPosition;
        this.segments = segments;
        this.isSideways = isSideways;
        this.shipPlacementColour = ShipPlacementColour.Placed;
        destroyedSections = 0;
    }

    public void paint(Graphics g) {
        if (shipPlacementColour == ShipPlacementColour.Placed) {
            g.setColor(destroyedSections >= segments ? Color.RED : Color.DARK_GRAY);
        } else {
            g.setColor(shipPlacementColour == ShipPlacementColour.Valid ? Color.GREEN : Color.RED);
        }
        if (isSideways) {
            paintHorizontal(g);
        } else {
            paintVertical(g);
        }
    }

    public void paintVertical(Graphics g) {
        int boatWidth = (int) (SelectionGrid.CELL_SIZE * 0.8);
        int boatLeftX = drawPosition.x + SelectionGrid.CELL_SIZE / 2 - boatWidth / 2;
        g.fillPolygon(new int[]{drawPosition.x + SelectionGrid.CELL_SIZE / 2, boatLeftX, boatLeftX + boatWidth},
                new int[]{drawPosition.y + SelectionGrid.CELL_SIZE / 4, drawPosition.y + SelectionGrid.CELL_SIZE, drawPosition.y + SelectionGrid.CELL_SIZE}, 3);
        g.fillRect(boatLeftX, drawPosition.y + SelectionGrid.CELL_SIZE, boatWidth,
                (int) (SelectionGrid.CELL_SIZE * (segments - 1.2)));
    }

    public void paintHorizontal(Graphics g) {
        int boatWidth = (int) (SelectionGrid.CELL_SIZE * 0.8);
        int boatTopY = drawPosition.y + SelectionGrid.CELL_SIZE / 2 - boatWidth / 2;
        g.fillPolygon(new int[]{drawPosition.x + SelectionGrid.CELL_SIZE / 4, drawPosition.x + SelectionGrid.CELL_SIZE, drawPosition.x + SelectionGrid.CELL_SIZE},
                new int[]{drawPosition.y + SelectionGrid.CELL_SIZE / 2, boatTopY, boatTopY + boatWidth},
                3);
        g.fillRect(drawPosition.x + SelectionGrid.CELL_SIZE, boatTopY,
                (int) (SelectionGrid.CELL_SIZE * (segments - 1.2)), boatWidth);
    }

    public void toggleSideways() {
        isSideways = !isSideways;
    }

    public void destroySection() {
        destroyedSections++;
    }

    public boolean isDestroyed() {
        return destroyedSections >= segments;
    }

    public void setDrawPosition(Position gridPosition, Position drawPosition) {
        this.drawPosition = drawPosition;
        this.gridPosition = gridPosition;
    }

    public java.util.List<Position> getOccupiedCoordinates() {
        List<Position> result = new ArrayList<>();
        if (isSideways) { // handle the case when horizontal
            for (int x = 0; x < segments; x++) {
                result.add(new Position(gridPosition.x + x, gridPosition.y));
            }
        } else { // handle the case when vertical
            for (int y = 0; y < segments; y++) {
                result.add(new Position(gridPosition.x, gridPosition.y + y));
            }
        }
        return result;
    }

    public boolean isSideways() {
        return isSideways;
    }

    public void setSideways(boolean sideways) {
        isSideways = sideways;
    }

    public int getSegments() {
        return segments;
    }

    public void setSegments(int segments) {
        this.segments = segments;
    }

    public Position getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(Position gridPosition) {
        this.gridPosition = gridPosition;
    }

    public Position getDrawPosition() {
        return drawPosition;
    }

    public void setDrawPosition(Position drawPosition) {
        this.drawPosition = drawPosition;
    }

    public int getDestroyedSections() {
        return destroyedSections;
    }

    public void setDestroyedSections(int destroyedSections) {
        this.destroyedSections = destroyedSections;
    }

    public ShipPlacementColour getShipPlacementColour() {
        return shipPlacementColour;
    }

    public void setShipPlacementColour(ShipPlacementColour shipPlacementColour) {
        this.shipPlacementColour = shipPlacementColour;
    }
}
