package logic;

public enum Direction {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    int x, y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Direction next() {
        int ord = ordinal() + 1 == Direction.values().length ? 0 : ordinal() + 1;
        return Direction.values()[ord];
    }

    public Direction prev() {
        int ord = ordinal() - 1 == -1 ? Direction.values().length - 1 : ordinal() - 1;
        return Direction.values()[ord];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
