package gip;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class AddProductController extends ResponseController {
	public TextField name;
	public Spinner<Integer> quantity;
	public Spinner<Double> price;

	public void addProduct() {
		resetFocus();
		if (name.getText().isEmpty()) {
			redFocus(name);
			setInfo("Field cannot be empty", true);
		} else {
			try {
				PreparedStatement preparedStatement = prepareStatement(
						"INSERT INTO product (productName, number, price) VALUE (?,?,?)");
				preparedStatement.setString(1, name.getText());
				preparedStatement.setInt(2, quantity.getValue());
				preparedStatement.setDouble(3, price.getValue());

				int affected = preparedStatement.executeUpdate();
				if (affected == 1)
					setInfo("Done.", false);
				else
					setInfo("Something went wrong.", true);
				name.clear();
			} catch (SQLException e) {
				setInfo(e.getMessage(), true);
			}

		}
	}
}
