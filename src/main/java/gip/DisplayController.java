package gip;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DisplayController {
    public ChoiceBox<Integer> invoiceChoice;
    public Label client;
    public Label date;
    public Label total;
    public TableView<InvoiceItem> invoice;
    public TableColumn<InvoiceItem, Integer> no;
    public TableColumn<InvoiceItem, String> productName;
    public TableColumn<InvoiceItem, Integer> quantity;
    public TableColumn<InvoiceItem, Double> subtotal;

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

        no.setCellValueFactory(new PropertyValueFactory<>("no"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        subtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
    }

    public void display() {
        try {
            invoice.getItems().clear();
            int chosenId = invoiceChoice.getValue();

            CallableStatement hdr = App.prepareCall("CALL gen(?)");
            hdr.setInt(1, chosenId);
            ResultSet rs = hdr.executeQuery();
            if (rs.next()) {
                String[] header = rs.getString(1).split(" ");
                client.setText(header[0]+", "+header[1]+" "+header[2]);
                date.setText(header[3]);
                total.setText("Total: "+header[4]);
            }

            PreparedStatement body = App.prepareStatement("SELECT productName, iE.number, iE.number * p.price FROM invoiceElement iE INNER JOIN product p ON iE.productId = p.productId WHERE invoiceId = ?");
            body.setInt(1, chosenId);
            rs = body.executeQuery();
            int count = 1;
            while (rs.next()) {
                invoice.getItems().add(new InvoiceItem(count++, rs.getString(1), rs.getInt(2), rs.getDouble(3)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class InvoiceItem {
        int no;
        String productName;
        int quantity;
        Double subtotal;

        InvoiceItem (int no, String productName, int quantity, Double subtotal) {
            this.no = no;
            this.productName = productName;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }

        public int getNo() { return no; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public Double getSubtotal() { return subtotal; }
    }
}
