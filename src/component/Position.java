package component;

public class Position {
    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position positionToCopy) {
        this.x = positionToCopy.x;
        this.y = positionToCopy.y;
    }

    public void add(Position otherPosition) {
        this.x += otherPosition.x;
        this.y += otherPosition.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public static final Position DOWN = new Position(0, 1);

    public static final Position UP = new Position(0, -1);

    public static final Position LEFT = new Position(-1, 0);

    public static final Position RIGHT = new Position(1, 0);

    public static final Position ZERO = new Position(0, 0);
}
