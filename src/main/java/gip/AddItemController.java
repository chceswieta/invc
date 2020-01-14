package gip;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddItemController {
    public Spinner<Integer> quantity;
    public TextField nip;
    public TextField invoiceId;
    public ChoiceBox<String> product;

    public void initialize() throws SQLException {
        ResultSet rs = App.executeQuery("SELECT name FROM product");
        
    }

    public void addItem() {

    }
}
