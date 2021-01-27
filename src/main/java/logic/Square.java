package logic;


import java.util.Arrays;
import java.util.List;

public class Square {
    /**
     * directions is a representation of the shift of a piece (square)
     * relative to one central piece in a tetromino.
     * 1
     * 0
     * 11
     * Here is an L shape tetromino. 0 - is a central piece, 1 - the rest of the pices.
     * As we can see here, the topmost piece has only one direction in the list directions and it's up.
     * The same thing with the bottom one. But bottom-right piece will have two directions in list :
     * bottom and right.
     * <p>
     * distance is distance from square to the central square.
     */

    List<Direction> directions;
    int x;
    int y;
    private final int distance;
    private Tetromino parent;

    public Square(int distance, Direction... direction) {
        this.distance = distance;
        this.directions = Arrays.asList(direction);
    }

    public void setParent(Tetromino parent) {
        this.parent = parent;
        setCoords();
    }

//    public void setDirection(List<Direction> directions) {
//        this.directions = directions;
//        setCoords();
//    }

    void setCoords() {
        int dx = 0, dy = 0;
        for (Direction d : directions) {
            dx += distance * d.x;
            dy += distance * d.y;
        }
        x = parent.getX() + dx;
        y = parent.getY() + dy;
    }

    public Tetromino getParent() {
        return parent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
