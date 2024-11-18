module JavaChessGame {
    requires javafx.controls;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.fxml;
   
    opens view to javafx.graphics;
    opens application to javafx.graphics, javafx.fxml;

    exports model;
    exports view;
    exports controller;
}
