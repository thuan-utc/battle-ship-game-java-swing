package Bot;

import component.Marker;
import component.Position;
import component.SelectionGrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmartBot extends BattleshipBot {
    /**
     * we paint color white and black for board,
     * min length ship is 2, so each ship is always in 2 color
     * -> only attack 1 color -> latest find ship position -> attack adjacent -> attack by direction the ship
     * level 1: attack by color -> attack adjacent
     * level 2: level 1 + attack by direction, 2 cell hit in row/column -> find by row/column
     */
    private List<Position> currentShipAttackCells;

    private final boolean debugAI = false;

    private boolean enableAttackByDirection;

    private boolean enableAttackByFindmaximiseAdjacentRandomIsation;

    private boolean enableAttackByColor;

    private boolean attackWhite = true;

    public SmartBot(SelectionGrid playerGrid, boolean enableAttackByDirection,
                    boolean enableAttackByFindmaximiseAdjacentRandomIsation, boolean enableAttackByColor) {
        super(playerGrid);
        currentShipAttackCells = new ArrayList<>();
        this.enableAttackByDirection = enableAttackByDirection;
        this.enableAttackByFindmaximiseAdjacentRandomIsation = enableAttackByFindmaximiseAdjacentRandomIsation;
        this.enableAttackByColor = enableAttackByColor;
        Collections.shuffle(validMoves);
    }

    @Override
    public void reset() {
        super.reset();
        currentShipAttackCells.clear();
        Collections.shuffle(validMoves);
    }

    @Override
    public Position selectMove() {
        if (debugAI) System.out.println("\nBEGIN TURN===========");
        Position selectedMove;
        if (currentShipAttackCells.size() > 0) { // Having a ship has been hit, but not destroyed
            if (enableAttackByDirection) { // attack by direction
                selectedMove = attackByDirection(); // attack cell around the HIT_SUCCESS cell
            } else {
                selectedMove = attackAdjacent();
            }
        } else {
            if (enableAttackByFindmaximiseAdjacentRandomIsation) {
                selectedMove = findMostOpenPosition(); // find cell that number of adjacent not attacked is max
            } else if (enableAttackByColor) {
                selectedMove = attackByColor();
            } else {
                // Use a random move
                selectedMove = validMoves.get(0);
            }
        }
        updateShipHits(selectedMove);
        validMoves.remove(selectedMove);
        if (debugAI) {
            System.out.println("Selected Move: " + selectedMove);
            System.out.println("END TURN===========");
        }
        return selectedMove;
    }

    /**
     * we paint color white and black for board,
     * min length ship is 2, so each ship is always in 2 color
     * -> only attack 1 color -> latest find ship position -> attack adjacent -> attack by direction the ship
     */
    private Position attackByColor() {
        List<Position> positionWithColor = getValidPositionByColor();
        if (positionWithColor.size() == 0) {
            attackWhite = !attackWhite;
            positionWithColor = getValidPositionByColor();
        }
        Collections.shuffle(positionWithColor);
        return positionWithColor.get(0);
    }

    private List<Position> getValidPositionByColor() {
        List<Position> colorPosition = new ArrayList<>();
        for (Position cell : validMoves) {
            if (playerGrid.getMarkerAtPosition(cell).isLogicColorIsWhite() == attackWhite) {
                colorPosition.add(cell);
            }
        }
        return colorPosition;
    }

    /**
     * attack by direction left, right, up, down
     */
    private Position attackByDirection() {
        List<Position> suggestedMoves = getAdjacentSmartMoves();
        for (Position possibleOptimalMove : suggestedMoves) {
            if (atLeastTwoHitsInDirection(possibleOptimalMove, Position.LEFT)) return possibleOptimalMove;
            if (atLeastTwoHitsInDirection(possibleOptimalMove, Position.RIGHT)) return possibleOptimalMove;
            if (atLeastTwoHitsInDirection(possibleOptimalMove, Position.DOWN)) return possibleOptimalMove;
            if (atLeastTwoHitsInDirection(possibleOptimalMove, Position.UP)) return possibleOptimalMove;
        }
        // No optimal choice found, just randomise the move.
        Collections.shuffle(suggestedMoves);
        return suggestedMoves.get(0);
    }

    /**
     * attack cell adjacent to cell HIT_SUCCESS (same column or same row)
     */
    private Position attackAdjacent() {
        List<Position> suggestedMoves = getAdjacentSmartMoves();
        Collections.shuffle(suggestedMoves);
        return suggestedMoves.get(0);
    }

    /**
     * @return find cell that have number adjacent not attack max
     */
    private Position findMostOpenPosition() {
        Position position = validMoves.get(0);
        ;
        int highestNotAttacked = -1;
        for (int i = 0; i < validMoves.size(); i++) {
            int testCount = getAdjacentNotAttackedCount(validMoves.get(i));
            if (testCount == 4) { // Maximum found, just return immediately
                return validMoves.get(i);
            } else if (testCount > highestNotAttacked) {
                highestNotAttacked = testCount;
                position = validMoves.get(i);
            }
        }
        return position;
    }

    private int getAdjacentNotAttackedCount(Position position) {
        List<Position> adjacentCells = getAdjacentCells(position);
        int notAttackedCount = 0;
        for (Position cell : adjacentCells) {
            if (!playerGrid.getMarkerAtPosition(cell).isMarked()) {
                notAttackedCount++;
            }
        }
        return notAttackedCount;
    }

    private boolean atLeastTwoHitsInDirection(Position start, Position direction) {
        Position testPosition = new Position(start);
        testPosition.add(direction);
        if (!currentShipAttackCells.contains(testPosition)) return false;
        testPosition.add(direction);
        if (!currentShipAttackCells.contains(testPosition)) return false;
        if (debugAI) System.out.println("Smarter match found AT: " + start + " TO: " + testPosition);
        return true;
    }

    private List<Position> getAdjacentSmartMoves() {
        List<Position> result = new ArrayList<>();
        for (Position shipHitPos : currentShipAttackCells) {
            List<Position> aroundCells = getAdjacentCells(shipHitPos); // get cell in same column, row
            for (Position cell : aroundCells) {
                if (!result.contains(cell) && validMoves.contains(cell)) {
                    result.add(cell);
                }
            }
        }
        if (debugAI) {
            printPositionList("Ship Hits: ", currentShipAttackCells);
            printPositionList("Adjacent Smart Moves: ", result);
        }
        return result;
    }

    private void printPositionList(String messagePrefix, List<Position> data) {
        String result = "[";
        for (int i = 0; i < data.size(); i++) {
            result += data.get(i);
            if (i != data.size() - 1) {
                result += ", ";
            }
        }
        result += "]";
        System.out.println(messagePrefix + " " + result);
    }

    private List<Position> getAdjacentCells(Position position) {
        List<Position> result = new ArrayList<>();
        if (position.x != 0) {
            Position left = new Position(position);
            left.add(Position.LEFT);
            result.add(left);
        }
        if (position.x != SelectionGrid.GRID_WIDTH - 1) {
            Position right = new Position(position);
            right.add(Position.RIGHT);
            result.add(right);
        }
        if (position.y != 0) {
            Position up = new Position(position);
            up.add(Position.UP);
            result.add(up);
        }
        if (position.y != SelectionGrid.GRID_HEIGHT - 1) {
            Position down = new Position(position);
            down.add(Position.DOWN);
            result.add(down);
        }
        return result;
    }

    /**
     * if a ship is sunk, clear all it's data
     */
    private void updateShipHits(Position testPosition) {
        Marker marker = playerGrid.getMarkerAtPosition(testPosition);
        if (marker.isShip()) {
            currentShipAttackCells.add(testPosition);
            // Check to find if this was the last place to hit on the targeted ship
            List<Position> allPositionsOfLastShip = marker.getAssociatedShip().getOccupiedCoordinates();
            if (debugAI) printPositionList("Last Ship", allPositionsOfLastShip);
            boolean hitAllOfShip = containsAllPositions(allPositionsOfLastShip, currentShipAttackCells);
            // If it was remove the ship data from history to now ignore it
            if (hitAllOfShip) {
                for (Position shipPosition : allPositionsOfLastShip) {
                    for (int i = 0; i < currentShipAttackCells.size(); i++) {
                        if (currentShipAttackCells.get(i).equals(shipPosition)) {
                            currentShipAttackCells.remove(i);
                            if (debugAI) System.out.println("Removed " + shipPosition);
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean containsAllPositions(List<Position> positionsToSearch, List<Position> listToSearchIn) {
        for (Position searchPosition : positionsToSearch) {
            boolean found = false;
            for (Position searchInPosition : listToSearchIn) {
                if (searchInPosition.equals(searchPosition)) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    public boolean isEnableAttackByColor() {
        return enableAttackByColor;
    }

    public void setEnableAttackByColor(boolean enableAttackByColor) {
        this.enableAttackByColor = enableAttackByColor;
    }
}
