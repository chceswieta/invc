package gip;

import javafx.scene.control.Spinner;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RestockController extends ProductListController {
    public Spinner<Integer> quantity;

    public void restock() {
        resetFocus();
        if (!productChosen()) {
            redFocus(product);
            setInfo("Field cannot be empty.", true);
        }
        else {
            try {
                PreparedStatement preparedStatement = prepareStatement("UPDATE product SET number = number + ? WHERE productId = ?");
                preparedStatement.setInt(1, quantity.getValue());
                preparedStatement.setInt(2, getProductId());
                preparedStatement.executeUpdate();
                setInfo("Done.", false);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

}
