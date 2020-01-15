package gip;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AddItemController extends ResponseController {
    public Spinner<Integer> quantity;
    public TextField nip;
    public TextField invoiceId;
    public ChoiceBox<String> product;

    public void initialize() throws SQLException {
        
    }

    public void addItem() {

    }
}
