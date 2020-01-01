module gip {
    requires javafx.controls;
    requires javafx.fxml;

    opens gip to javafx.fxml;
    exports gip;
}