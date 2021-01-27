package logic;

import java.util.*;

/**
 * Basically, class container for Squares. Its purpose is to coordinate squares.
 */

public class Tetromino {
    private int x, y;
    List<Square> squares;
    Kind kind;

    public Tetromino(Kind kind) {
        this.kind = kind;
        Direction RIGHT = Direction.RIGHT;
        Direction DOWN = Direction.DOWN;
        Direction LEFT = Direction.LEFT;
        Direction UP = Direction.UP;
        List<Square> s = new ArrayList<>();
        switch (kind) {
            case L:
                s.addAll(Arrays.asList(new Square(1, UP), new Square(0),
                        new Square(1, DOWN), new Square(1, DOWN, RIGHT)));
                break;
            case I:
                s.addAll(Arrays.asList(new Square(1, UP), new Square(0),
                        new Square(1, DOWN), new Square(2, DOWN)));
                break;
            case T:
                s.addAll(Arrays.asList(new Square(1, UP), new Square(0), new Square(1, LEFT),
                        new Square(1, RIGHT)));
                break;
            case O:
                s.addAll(Arrays.asList(new Square(0), new Square(1, RIGHT),
                        new Square(1, DOWN), new Square(1, DOWN, RIGHT)));
                break;
            case S:
                s.addAll(Arrays.asList(new Square(0), new Square(1, RIGHT),
                        new Square(1, DOWN), new Square(1, DOWN, LEFT)));
                break;
            case J:
                s.addAll(Arrays.asList(new Square(1, UP), new Square(0),
                        new Square(1, DOWN), new Square(1, DOWN, LEFT)));
                break;
            case Z:
                s.addAll(Arrays.asList(new Square(1, LEFT), new Square(0),
                        new Square(1, DOWN), new Square(1, DOWN, RIGHT)));
                break;
        }
        this.squares = s;
        for (Square square : s) {
            square.setParent(this);
        }
    }

    public Tetromino() {
        this(Kind.values()[new Random().nextInt(Kind.values().length)]);
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
        squares.forEach(s -> {
            s.x += dx;
            s.y += dy;
        });
    }

    public void move(Direction direction) {
        move(direction.x, direction.y);
    }

    public void rotate(boolean forth) {
        if (kind.equals(Kind.O))
            return;
        for (Square square : squares) {
            List<Direction> newDir = new ArrayList<>();
            if (forth) {
                square.directions.forEach(e -> newDir.add(e.next()));
            }
            else
                square.directions.forEach(e -> newDir.add(e.prev()));
            square.directions = newDir;
            square.setCoords();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
        squares.forEach(Square::setCoords);
    }

    public List<Square> getSquares() {
        return squares;
    }

    public Kind getKind() {
        return kind;
    }
}
