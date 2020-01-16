package gip;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.TextField;

public class RemoveItemController extends ProductListController {
	public TextField invoiceId;
	public TextField productId;
	int pId = -1;

	public void removeItem() {
		resetFocus();

		if (invoiceId.getText().isEmpty()) {
			redFocus(invoiceId);
			setInfo("Please fill invoice number", true);

//			try {
//				int iId = Integer.parseInt(invoiceId.getText());
//			} catch (NumberFormatException e) {
//				redFocus(productId);
//				setInfo("Invoice identifier can only contain digits", true);
//			}
		} else if (!productChosen() && productId.getText().isEmpty()) {
			redFocus(productId);
			setInfo("Don't be so modest, choose one", true);
		} else if (productChosen() && !productId.getText().isEmpty()) {
			redFocus(productId);
			setInfo("Don't be greedy, choose one.", true);
		} else {
			if (!productId.getText().isEmpty()) {
				try {
					pId = Integer.parseInt(productId.getText());
				} catch (NumberFormatException e) {
					redFocus(productId);
					setInfo("Product identifier can only contain digits", true);
				}
			}
			if (productChosen()) {
				pId = getProductId();
			}

			try {
				CallableStatement callStatement = prepareCall("CALL dellie(?,?)");
				callStatement.setString(1, invoiceId.getText());
				callStatement.setInt(2, pId);
				callStatement.executeQuery();
				setInfo("Done.", false);
				productId.clear();
				invoiceId.clear();
			} catch (SQLException e) {
				setInfo("Something went wrong. Try again.", true);
			}

		}

	}
}
