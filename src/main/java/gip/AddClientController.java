package gip;

import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddClientController {
    public TextField name;
    public TextField surname;
    public DatePicker date;
    public TextField nip;
    public Label info;

    public void addClient() {
        try {
            PreparedStatement preparedStatement = App.prepareStatement("INSERT INTO client (name, surname, birth) VALUE (?, ?, ?)");
            preparedStatement.setString(1, name.getText());
            preparedStatement.setString(2, surname.getText());
            preparedStatement.setDate(3, java.sql.Date.valueOf(date.getValue()));
            if (preparedStatement.executeUpdate() == 1) info.setText("Done");
            else info.setText("Something went wrong");
        } catch (SQLException e) {
            info.setText(e.getMessage());
        }
    }
}
