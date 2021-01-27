package fxml;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import logic.Direction;
import logic.Square;
import logic.Tetromino;

import java.util.*;

public class Controller {

    static final int size = 45; //size of a cell in the grid
    static final int width = 10; //width in cells
    static final int height = 20; //height in cells
    static final int W = size * width;
    static final int H = size * height;
    Timeline timeline;
    TetrominoView current;
    static TetrominoView next;
    static boolean[][] grid; //true - cell is taken, false - cell is free
    List<StackPane> fallenList; //list of fallen pieces
    boolean gameOver;
    boolean gamePaused;

    @FXML
    Label scoreText;
    IntegerProperty score;
    @FXML
    Pane board;
    @FXML
    Pane nextShapePane;
    @FXML
    Pane gameOverPanel;
    @FXML
    Label resultLabel;
    @FXML
    Pane pausePanel;

    public void initialize() {
        gamePaused = false;
        pausePanel.setVisible(false);
        gameOverPanel.setVisible(false);
        gameOver = false;
        fallenList = new ArrayList<>();
        grid = new boolean[width][height];
        score = new SimpleIntegerProperty(0);
        scoreText.textProperty().bind(score.asString());
        makeGrid();
        board.requestFocus();
        next();
        board.setOnMouseClicked(e -> {
            if (gameOver)
                gameOverPanel.setVisible(true);
        });
        gameOverPanel.setOnMouseClicked(e -> {
            gameOverPanel.setVisible(!gameOverPanel.isVisible());
        });
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.3), ev -> {
            move(Direction.DOWN);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    void makeGrid() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Rectangle rect = new Rectangle(size - 1, size - 1);
                rect.setFill(null);
                rect.relocate(i * size, j * size);
                rect.setStrokeWidth(1);
                rect.setStroke(Color.LIGHTGRAY);
                rect.toBack();
                board.getChildren().add(rect);
            }
        }
    }


    void setScore(int s) {
        score.setValue(score.getValue() + s * s);
    }

    //call the next piece
    void next() {
        if (next == null) {
            next = new TetrominoView();
        } else {
            for (Square s : current.map.keySet()) {
                grid[s.getX()][s.getY()] = true;
            }
            fallenList.addAll(current.map.values());
            sweepRows();
            gameOver();
            if (gameOver)
                return;
        }
        current = next;
        current.setPosition(width / 2);
        board.getChildren().addAll(current.map.values());
        update();

        //creating next and setting its sqares in the correct order.
        nextShapePane.getChildren().clear();
        next = new TetrominoView();
        Group group = new Group();
        for (Square s : next.map.keySet()) {
            Pane p = next.map.get(s);
            p.relocate(s.getX() * size - 0.5, s.getY() * size - 0.5);
            group.getChildren().add(p);
        }
        nextShapePane.getChildren().add(group);
    }

    //check whether moving further is possible
    boolean checkCollision(Direction dir) {
        for (Square s : current.map.keySet()) {
            if (s.getX() + dir.getX() < 0 || s.getX() + dir.getX() >= width ||
                    s.getY() + dir.getY() < 0 || s.getY() + dir.getY() >= height ||
                    grid[s.getX() + dir.getX()][s.getY() + dir.getY()])
                return true;
        }
        return false;
    }

    //move current tetromino and display the changes on the board
    void move(Direction dir) {
        if (gameOver || gamePaused)
            return;
        if (checkCollision(dir)) {
            if (dir.equals(Direction.DOWN)) {
                next();
            }
            return;
        }
        current.t.move(dir);
        update();
    }

    void fall() {
        while (!checkCollision(Direction.DOWN)) {
            move(Direction.DOWN);
        }
    }

    //rotate current tetromino and display the changes on the board
    void rotate() {
        if (gameOver || gamePaused)
            return;
        Tetromino t = current.t;
        t.rotate(true);
        for (Square s : current.t.getSquares()) {
            if (s.getX() < 0 || s.getX() >= width || s.getY() < 0 || s.getY() >= height
                    || grid[s.getX()][s.getY()]) {
                t.rotate(false);
                return;
            }
        }
        update();
    }

    void sweepRows() {
        boolean remove; //check if we're removing the row
        int count = 0; //how many rows we've removed
        for (int i = height - 1; i >= 0; i--) {
            remove = true;
            for (int j = 0; j < width; j++) {
                if (!grid[j][i]) {
                    remove = false;
                    break;
                }
            }
            if (remove) {
                count++;
                removeRow(i);
                i++;
            }
        }
        setScore(count);
    }

    void removeRow(int y) {
        fallenList.forEach(e -> {
            if (e.getLayoutY() == size * y) {
                board.getChildren().remove(e);
            }
        });
        for (int i = 0; i < width; i++) {
            grid[i][y] = false;
        }
        fallenList.removeIf(e -> e.getLayoutY() == size * y);
        fallenList.forEach(e -> {
            if (e.getLayoutY() < size * y)
                e.setLayoutY(e.getLayoutY() + size);
        });
        for (int i = y; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                if (grid[j][i]) {
                    grid[j][i] = false;
                    grid[j][i + 1] = true;
                }
            }
        }
    }

    //check whether the game is Over
    void gameOver() {
        for (int i = 0; i < width; i++) {
            if (grid[i][0]) {
                gameOver = true;
                break;
            }
        }
        if (gameOver) {
            resultLabel.setText(score.getValue() + "");
            timeline.stop();
            gameOverPanel.setVisible(true);
        }
    }

    void update() {
        for (Square s : current.map.keySet()) {
            current.map.get(s).relocate(s.getX() * size, s.getY() * size);
        }
    }

    void pause() {
        if (gameOver)
            return;
        if (!gamePaused) {
            timeline.stop();
            pausePanel.setVisible(true);
            gamePaused = true;
        } else {
            timeline.play();
            pausePanel.setVisible(false);
            gamePaused = false;
        }
    }

    @FXML
    void restart() {
        timeline.stop();
        board.getChildren().clear();
        nextShapePane.getChildren().clear();
        current = null;
        next = null;
        initialize();
    }

}
