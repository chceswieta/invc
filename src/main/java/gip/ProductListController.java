package gip;

import javafx.scene.control.ChoiceBox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public abstract class ProductListController extends ResponseController {
    public ChoiceBox<String> product;
    private LinkedHashMap<String, Integer> products = new LinkedHashMap<>();

    public void initialize() throws SQLException {
        ResultSet rs = executeQuery("SELECT productId, productName FROM product ORDER BY productId");
        while (rs.next()) {
            product.getItems().add(rs.getString(2));
            products.put(rs.getString(2), rs.getInt(1));
        }
    }

    public int getProductId() {
        return products.get(product.getValue());
    }

    public boolean productChosen() {
        return !product.getSelectionModel().isEmpty();
    }
}
