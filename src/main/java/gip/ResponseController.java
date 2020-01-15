package gip;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ResponseController {
    public Label info;
    private Node redFocused = null;

    public void redFocus(Node node) {
        redFocused = node;
        node.setStyle("-fx-faint-focus-color: rgba(181,74,74,0.25); -fx-focus-color: indianred;");
        node.requestFocus();
    }

    public void resetFocus() {
        if (redFocused != null) {
            redFocused.setStyle(null);
            redFocused = null;
        }
    }

    public void setInfo(String message, boolean error) {
        if (error) info.setTextFill(Color.INDIANRED);
        else info.setTextFill(Color.DIMGRAY);
        info.setText(message);
    }

    public ResultSet executeQuery(String query) throws SQLException { return App.executeQuery(query); }
    public int executeUpdate(String query) throws SQLException { return App.executeUpdate(query); }
    public PreparedStatement prepareStatement(String statement) throws SQLException { return App.prepareStatement(statement); }
    public CallableStatement prepareCall(String statement) throws SQLException { return App.prepareCall(statement); }

}
