package gip;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private void openDialog(String option) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(option+".fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }
    private void openDialog(String option, int limit) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(option+".fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        SpinnerValueFactory.IntegerSpinnerValueFactory factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,limit);
        Spinner<Integer> spinner = (Spinner<Integer>) scene.lookup("#quantity");
        spinner.setValueFactory(factory);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
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
        App.setRoot("Login");
    }
}
