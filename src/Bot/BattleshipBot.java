package Bot;

import component.Position;
import component.SelectionGrid;

import java.util.ArrayList;
import java.util.List;

public abstract class BattleshipBot {
    protected SelectionGrid playerGrid;

    protected List<Position> validMoves;

    public BattleshipBot(SelectionGrid playerGrid) {
        this.playerGrid = playerGrid;
        createValidMoveList();
    }

    public Position selectMove() {
        return Position.ZERO;
    }

    public void reset() {
        createValidMoveList();
    }

    protected void createValidMoveList() {
        validMoves = new ArrayList<>();
        for(int x = 0; x < SelectionGrid.GRID_WIDTH; x++) {
            for(int y = 0; y < SelectionGrid.GRID_HEIGHT; y++) {
                validMoves.add(new Position(x,y));
            }
        }
    }
}
