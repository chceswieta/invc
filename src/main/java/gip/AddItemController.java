package gip;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddItemController extends ResponseController {
    public Spinner<Integer> quantity;
    public TextField nip;
    public TextField invoiceId;
    public ChoiceBox<String> product;

    public void initialize() throws SQLException {
        ResultSet rs = App.executeQuery("SELECT productId, productName FROM product");
        while (rs.next()) product.getItems().add(rs.getString(1) + " " + rs.getString(2));
    }

    public void addItem() {
        resetFocus();

        if (product.getSelectionModel().isEmpty()) {
            redFocus(product);
            setInfo("Field cannot be empty.", true);
        } else if (invoiceId.getText().isEmpty()) {
            if (nip.getText().isEmpty()) {
                redFocus(nip);
                setInfo("Please at least fill in the NIP number.", true);
            } else {
                try {
                    ResultSet rs = App.executeQuery("SELECT MAX(invoiceId) FROM invoice");
                    int newId = rs.next() ? rs.getInt(1) + 1 : 0;
                    App.executeUpdate("INSERT INTO invoice VALUE ("+newId+", "+nip.getText()+", CURDATE(), 0)");
                    nip.clear();
                    invoiceId.setText(String.valueOf(newId));
                    setInfo("Your new invoice has been generated.", false);
                } catch (SQLException e) {
                    if (e.getMessage().contains("foreign key")) {
                        redFocus(nip);
                        setInfo("This client does not exist.", true);
                    }
                    else setInfo(e.getMessage(), true);
                }
            }
        } else {
            try {
                CallableStatement callStatement = App.prepareCall("CALL addie(?,?,?)");
                callStatement.setString(1, invoiceId.getText());
                callStatement.setString(2, product.getValue().split(" ")[0]);
                callStatement.setInt(3, quantity.getValue());
                callStatement.executeQuery();
                setInfo("Done.", false);
            } catch (SQLException e) {
                if (e.getMessage().contains("foreign key")) {
                    redFocus(invoiceId);
                    setInfo("This invoice does not exist.", true);
                }
                else setInfo(e.getMessage(), true);
            }
        }
    }

    public void onlyOne(KeyEvent keyEvent) {
        TextField source = (TextField) keyEvent.getSource();
        if (source.getId().equals("nip")) invoiceId.clear();
        else nip.clear();
    }
}
