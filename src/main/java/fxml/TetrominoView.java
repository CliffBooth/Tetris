package fxml;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.Square;
import logic.Tetromino;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class TetrominoView {
    final int size = Controller.size;
    Tetromino t; //probably should be a private variable??
    Map<Square, StackPane> map;

    TetrominoView() {
        map = new HashMap<>();
        t = new Tetromino();
        for (Square s : t.getSquares()) {
            FileInputStream is;
            Rectangle rect = new Rectangle(size, size);
            StackPane pane = new StackPane();
            //pane.getChildren().add(rect);
            //pane.relocate(s.getX() * size, s.getY() * size);
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(1);
            try {
                switch (t.getKind()) {
                    case L -> {
                        rect.setFill(Color.GREEN);
                        is = new FileInputStream("src/main/resources/green.png");
                    }
                    case I -> {
                        rect.setFill(Color.BLUE);
                        is = new FileInputStream("src/main/resources/blue.png");
                    }
                    case O -> {
                        rect.setFill(Color.RED);
                        is = new FileInputStream("src/main/resources/red.png");
                    }
                    case T -> {
                        rect.setFill(Color.ORANGE);
                        is = new FileInputStream("src/main/resources/orange.png");
                    }
                    case S -> {
                        rect.setFill(Color.PURPLE);
                        is = new FileInputStream("src/main/resources/purple.png");
                    }
                    case J -> {
                        rect.setFill(Color.YELLOW);
                        is = new FileInputStream("src/main/resources/yellow.png");
                    }
                    default -> {
                        rect.setFill(Color.CYAN);
                        is = new FileInputStream("src/main/resources/lightblue.png");
                    }
                }
                Image image = new Image(is);
                ImageView imageView = new ImageView(image);
                pane.getChildren().add(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put(s, pane);
        }
    }

    //place the tetromino logically, after using this method you need to use update() and the tetrominoView will
    //be set in its place on the board.
    void setPosition(int x) {
        int y;
        switch (t.getKind()) {
            case S, Z, O -> y = 0;
            default -> y = 1;
        }
        t.setCoords(x, y);
    }
}
