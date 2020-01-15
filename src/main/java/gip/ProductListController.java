package gip;

import javafx.scene.control.ChoiceBox;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ProductListController extends ResponseController {
    public ChoiceBox<String> product;

    public void initialize() throws SQLException {
        ResultSet rs = executeQuery("SELECT productId, productName FROM product");
        while (rs.next()) product.getItems().add(rs.getString(1) + " " + rs.getString(2));
    }

    public int getProductId() {
        return Integer.parseInt(product.getValue().split(" ")[0]);
    }

    public boolean productChosen() {
        return !product.getSelectionModel().isEmpty();
    }
}
