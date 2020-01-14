package gip;

import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddClientController {
    public TextField name;
    public TextField surname;
    public DatePicker date;
    public TextField nip;
    public Label info;

    public void initialize() {
        date.setValue(LocalDate.now().minusYears(18));
    }

    public void addClient() {
        info.setTextFill(Color.INDIANRED);
        if (name.getText().isEmpty()) {
            redFocus(name);
            info.setText("The name field cannot be empty.");
        }
        else if (surname.getText().isEmpty()) {
            redFocus(surname);
            info.setText("The surname field cannot be empty.");
        }
        else if (date.getEditor().getText().isEmpty()) {
            info.setText("Date of birth cannot be empty.");
        }
        else if (nip.getText().length() == 11 || nip.getText().length() == 10) {
            try {
                Long.parseLong(nip.getText());

                PreparedStatement preparedStatement = App.prepareStatement("INSERT INTO client VALUE (?, ?, ?, ?)");
                preparedStatement.setString(1, nip.getText());
                preparedStatement.setString(2, name.getText());
                preparedStatement.setString(3, surname.getText());
                preparedStatement.setDate(4, java.sql.Date.valueOf(date.getValue()));

                if (preparedStatement.executeUpdate() == 1) {
                    info.setTextFill(Color.SILVER);
                    info.setText("Done");
                }
                else info.setText("Something went wrong.");

                name.clear();
                surname.clear();
                date.getEditor().clear();
                nip.clear();

            } catch (SQLException e) {
                info.setText(e.getMessage());
            } catch (NumberFormatException e) {
                redFocus(nip);
                info.setText("NIP/PESEL can only contain digits.");
            }
        } else {
            redFocus(nip);
            info.setText("Please provide a 10-digit PESEL or a 11-digit NIP number.");
        }
    }

    public void redFocus(Node node) {
        node.setStyle("-fx-faint-focus-color: transparent; -fx-focus-color: indianred;");
        node.requestFocus();
    }
}
