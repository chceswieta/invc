package gip;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    public TextField user;
    public PasswordField password;
    public Label incorrectInfo;

    @FXML
    private void login() throws IOException {
        try {
            App.attemptConnection(user.getText(), password.getText());
            App.setUsername(user.getText());
            //App.attemptConnection("invClient","cpassword");
            //App.setUsername("invClient");
            App.setRoot("Main");
        } catch (SQLException e) {
            incorrectInfo.setText("Incorrect username or password");
        }
    }
}
