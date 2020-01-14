package gip;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class MainController {
    public Group clientAccess;
    public Group employeeAccess;
    public Group adminAccess;

    private void openDialog(String option) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(option + ".fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }

    private void openDialog(String option, int limit) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(option + ".fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        SpinnerValueFactory.IntegerSpinnerValueFactory factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, limit);
        Spinner<Integer> spinner = (Spinner<Integer>) scene.lookup("#quantity");
        spinner.setValueFactory(factory);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }

    @FXML
    public void initialize() {
        try {
            ResultSet rs = App.executeQuery("show grants");
            LinkedList<String> grants = new LinkedList<>();
            String grant;
            while (rs.next()) {
                grant = rs.getString(1).split(" TO ")[0];
                grants.add(grant);
            }
            boolean moreThanClientAccess = grants.contains("GRANT SELECT, INSERT, DELETE ON `invc`.`invoice`") && grants.contains("GRANT SELECT, INSERT, DELETE ON `invc`.`invoiceElement`");
            if (moreThanClientAccess) {
                if (grants.contains("GRANT SELECT, INSERT, UPDATE, DELETE ON `invc`.`client`") && grants.contains("GRANT SELECT, INSERT, UPDATE, DELETE ON `invc`.`product`"))
                    grantAccess("adminAccess");
                else if (grants.contains("GRANT SELECT, UPDATE ON `invc`.`product`") && grants.contains("GRANT SELECT ON `invc`.`client`"))
                    grantAccess("employeeAccess");
            }
            else if (grants.contains("GRANT SELECT ON `invc`.`invoiceElement`")) grantAccess("clientAccess");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void display() throws IOException {
        openDialog("Display");
    }

    @FXML
    private void addItem() throws IOException {
        openDialog("AddItem");
    }

    @FXML
    private void removeItem() throws IOException {
        openDialog("RemoveItem");
    }

    @FXML
    private void removeInvoice() throws IOException {
        openDialog("RemoveInvoice");
    }

    @FXML
    private void addClient() throws IOException {
        openDialog("AddClient");
    }

    @FXML
    private void removeClient() throws IOException {
        openDialog("RemoveClient");
    }

    @FXML
    private void addProduct() throws IOException {
        openDialog("AddProduct");
    }

    @FXML
    private void restock() throws IOException {
        openDialog("Restock");
    }

    @FXML
    private void removeProduct() throws IOException {
        openDialog("RemoveProduct");
    }

    @FXML
    private void logout() throws IOException {
        try {
            App.endConnection();
            App.setRoot("Login");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void grantAccess(String accessLevel) {
        LinkedList<Node> buttonGroup = new LinkedList<>();
        switch (accessLevel) {
            case "adminAccess":
                buttonGroup.addAll(adminAccess.getChildren());
            case "employeeAccess":
                buttonGroup.addAll(employeeAccess.getChildren());
            case "clientAccess":
                buttonGroup.addAll(clientAccess.getChildren());
                break;
        }
        for (Node button : buttonGroup) button.setDisable(false);
    }
}
