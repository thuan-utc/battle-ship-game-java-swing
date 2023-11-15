package component;

public class Rectangle {
    protected Position position;

    protected int width;

    protected int height;

    public Rectangle(Position position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public Rectangle(int x, int y, int width, int height) {
        this(new Position(x, y), width, height);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isPositionInside(Position targetPosition) {
        return targetPosition.x >= position.x
                && targetPosition.y >= position.y
                && targetPosition.x < position.x + width
                && targetPosition.y < position.y + height;
    }
}
