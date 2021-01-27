package logic;

import java.util.Random;

/**
 * not used
 */

public class Tetris {
    private int height; //by squares
    private int width;
    private Square[][] grid;
    Tetromino current;
    boolean gameOver;

    public Tetris() {
        current = null;
        gameOver = false;
        height = 20;
        width = 10;
        grid = new Square[width][height];
    }

    public Tetris(int width, int height) {
        new Tetris();
        this.height = height;
        this.width = width;
    }

    private void start() {
        System.out.println("start!");
        current = new Tetromino(Kind.values()[new Random().nextInt(Kind.values().length - 1)]); //getting a random shape for tetromino.
//        current = new Tetromino(Kind.L);
        if (current.kind.equals(Kind.I))
            current.setCoords(width / 2, 2);
        else
            current.setCoords(width / 2, 1);
        for (Square square : current.squares) {
            grid[square.x][square.y] = square;
        }
    }

    public void fall() {
        System.out.println("FALL!");
        if (current == null)
            start();
        //checking if tetromino can fall any further
        for (Square square : current.squares) {
            if (square.y + 1 == height ||
                    (grid[square.x][square.y + 1] != null && grid[square.x][square.y + 1].getParent() != current)) {
                checkGameOver();
                if (!gameOver)
                    start();
                else
                    System.out.println("Game Over!");
                return;
            }
        }
        for (Square square : current.squares) {
            grid[square.x][square.y] = null;
        }
        current.move(Direction.DOWN);
        for (Square square : current.squares) {
            grid[square.x][square.y] = square;
        }
    }

    private void checkGameOver() {
        for (int i = 0; i < width - 1; i++) {
            if (grid[i][0] != null) {
                gameOver = true;
                break;
            }
        }
    }

    public void move(Direction d) {
        for (Square square : current.squares) {
            grid[square.x][square.y] = null;
        }
        current.move(d);
        for (Square square : current.squares) {
            grid[square.x][square.y] = square;
        }
    }

    public void rotate(boolean forth) {
        for (Square square : current.squares) {
            grid[square.x][square.y] = null;
        }
        current.rotate(forth);
        for (Square square : current.squares) {
            grid[square.x][square.y] = square;
        }
    }

    public Square[][] getGrid() {
        return grid;
    }

    public boolean gameOver() {
        return gameOver;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
