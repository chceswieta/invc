package gip;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddClientController extends ResponseController {
    public TextField name;
    public TextField surname;
    public DatePicker date;
    public TextField nip;

    public void initialize() {
        date.setValue(LocalDate.now().minusYears(18));
    }

    public void addClient() {
        resetFocus();
        if (name.getText().isEmpty()) {
            redFocus(name);
            setInfo("The name field cannot be empty.", true);
        }
        else if (surname.getText().isEmpty()) {
            redFocus(surname);
            setInfo("The surname field cannot be empty.", true);
        }
        else if (date.getEditor().getText().isEmpty()) {
            redFocus(date);
            setInfo("Date of birth cannot be empty.", true);
        }
        else if (nip.getText().length() == 11 || nip.getText().length() == 10) {
            try {
                Long.parseLong(nip.getText());

                PreparedStatement preparedStatement = prepareStatement("INSERT INTO client VALUE (?, ?, ?, ?, NULL)");
                preparedStatement.setString(1, nip.getText());
                preparedStatement.setString(2, name.getText());
                preparedStatement.setString(3, surname.getText());
                preparedStatement.setDate(4, java.sql.Date.valueOf(date.getValue()));

                if (preparedStatement.executeUpdate() == 1) setInfo("Done.", false);
                else setInfo("Something went wrong.", true);

                name.clear();
                surname.clear();
                date.getEditor().clear();
                nip.clear();

            } catch (SQLException e) {
                setInfo(e.getMessage(), true);
            } catch (NumberFormatException e) {
                redFocus(nip);
                setInfo("NIP/PESEL can only contain digits.", true);
            }
        } else {
            redFocus(nip);
            setInfo("Please provide a 10-digit PESEL or a 11-digit NIP number.", true);
        }
    }
}
