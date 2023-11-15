package Bot;

import component.Position;
import component.SelectionGrid;

import java.util.Collections;

public class NoobBot extends BattleshipBot {
    /* only random next move*/
    public NoobBot(SelectionGrid playerGrid) {
        super(playerGrid);
        Collections.shuffle(validMoves); // create random move
    }

    @Override
    public void reset() {
        super.reset();
        Collections.shuffle(validMoves);
    }

    @Override
    public Position selectMove() {
        Position nextMove = validMoves.get(0);
        validMoves.remove(0);
        return nextMove;
    }
}
