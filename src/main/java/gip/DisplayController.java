package gip;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DisplayController {
    public ChoiceBox<Integer> invoiceChoice;
    public Label invoice_contents;

    public void initialize() throws SQLException {
        ResultSet rs;
        if (App.getAllInvoicesAvailable()) rs = App.executeQuery("SELECT invoiceId FROM invoice ORDER BY invoiceId");
        else {
            CallableStatement choices = App.prepareCall("CALL num(?)");
            choices.setString(1, App.getUsername());
            rs = choices.executeQuery();
        }
        while (rs.next()) {
            invoiceChoice.getItems().add(rs.getInt(1));
        }
    }

    public void display() {
        try {
            int chosenId = invoiceChoice.getValue();
            StringBuilder invoice = new StringBuilder();
            CallableStatement header = App.prepareCall("CALL gen(?)");
            header.setInt(1, chosenId);
            ResultSet rs = header.executeQuery();
            while (rs.next()) invoice.append(rs.getString(1)).append("\n");
            PreparedStatement body = App.prepareStatement("SELECT productName, iE.number FROM invoiceElement iE INNER JOIN product p ON iE.productId = p.productId WHERE invoiceId = ?");
            body.setInt(1, chosenId);
            rs = body.executeQuery();
            while (rs.next()) invoice.append(rs.getString(1)).append(" ").append(rs.getString(2)).append("\n");
            invoice_contents.setText(invoice.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
