package fxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logic.Direction;

import java.io.FileInputStream;
import java.net.URL;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL location = getClass().getClassLoader().getResource( "layout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();
        Controller c = fxmlLoader.getController();
        Scene scene = new Scene(root, Controller.W + 200, Controller.H + 5); //5 is the border width
        stage.setScene(scene);
        stage.setResizable(false);
        Image icon = new Image(new FileInputStream("src/main/resources/icon.jpg"));
        stage.getIcons().add(icon);
        stage.setTitle("TETRIS");
        stage.show();
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT, A -> c.move(Direction.LEFT);
                case DOWN, S -> c.fall();
                case RIGHT, D -> c.move(Direction.RIGHT);
                //case UP, W -> c.move(Direction.UP);
                case SPACE -> c.rotate();
                case ESCAPE -> c.pause();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
