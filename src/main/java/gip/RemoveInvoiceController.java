package gip;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.scene.control.TextField;

public class RemoveInvoiceController extends ResponseController {
	public TextField invoiceID;

	public void removeInvoice() {
		resetFocus();
		if (invoiceID.getText().isEmpty()) {
			redFocus(invoiceID);
			setInfo("Field cannot be empty.", true);
		} else {
			try {
                PreparedStatement preparedStatement = prepareStatement("DELETE FROM invoice WHERE invoiceId = ?");
                preparedStatement.setString(1, invoiceID.getText());
                int affected = preparedStatement.executeUpdate();
                if (affected == 1) setInfo("Done.", false);
                else if (affected == 0) setInfo("Invoice not found.", true);
                else setInfo("Something went wrong.", true);
                invoiceID.clear();
			} catch (SQLException e) {
				setInfo(e.getMessage(), true);
			}
		}
	}
}
