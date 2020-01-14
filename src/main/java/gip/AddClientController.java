package gip;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddClientController {
    public TextField name;
    public TextField surname;
    public DatePicker date;
    public TextField nip;
    public Label info;

    public void addClient() {

        if (nip.getText().length() == 11 || nip.getText().length() == 10) {
            try {
                PreparedStatement preparedStatement = App.prepareStatement("INSERT INTO client VALUE (?, ?, ?, ?)");
                preparedStatement.setString(1, nip.getText());
                preparedStatement.setString(2, name.getText());
                preparedStatement.setString(3, surname.getText());
                preparedStatement.setDate(4, java.sql.Date.valueOf(date.getValue()));
                if (preparedStatement.executeUpdate() == 1) info.setText("Done");
                else info.setText("Something went wrong");
            } catch (SQLException e) {
                info.setText(e.getMessage());
            }
        }
        else {
            nip.setStyle("-fx-faint-focus-color: transparent; -fx-focus-color: #793a3a;");
            nip.requestFocus();
        }



    }
}
