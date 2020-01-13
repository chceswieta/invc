module gip {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;

    opens gip to javafx.fxml;
    exports gip;
}