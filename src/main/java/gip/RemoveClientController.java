package gip;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public class RemoveClientController extends ResponseController{
    public TextField nip;
    public Label info;

    public void removeClient() {
        resetFocus();
        if (nip.getText().isEmpty()) {
            redFocus(nip);
            setInfo("Field can't be empty.", true);
        }
        else {
            try {
                PreparedStatement preparedStatement = App.prepareStatement("DELETE FROM client WHERE clientId = ?");
                preparedStatement.setString(1, nip.getText());
                int affected = preparedStatement.executeUpdate();
                if (affected == 1) setInfo("Done.", false);
                else if (affected == 0) setInfo("Client not found.", true);
                else setInfo("Something went wrong.", true);
                nip.clear();
            } catch (SQLException e) {
                setInfo(e.getMessage(), true);
            }

        }
    }
}
