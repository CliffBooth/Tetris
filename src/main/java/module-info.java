module Tetris {
    requires javafx.controls;
    requires javafx.fxml;

    opens javaFX to javafx.graphics;
    opens fxml to javafx.fxml;
    exports javaFX;
    exports fxml;
}