package gip;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import java.io.IOException;

public class OptionsMenuBar extends MenuBar {
    private OptionsMenuBar instance;

    public OptionsMenuBar() {
        Menu invoice = new Menu("Invoice");

        MenuItem display = new MenuItem("Display");
        display.setOnAction(actionEvent -> {
            try {
                App.setRoot("display");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem addItem = new MenuItem("Add item");
        addItem.setOnAction(actionEvent -> {
            try {
                App.setRoot("additem");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem removeItem = new MenuItem("Remove item");
        removeItem.setOnAction(actionEvent -> {
            try {
                App.setRoot("removeitem");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MenuItem removeInvoice = new MenuItem("Remove");
        removeInvoice.setOnAction(actionEvent -> {
            try {
                App.setRoot("removeinvoice");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        invoice.getItems().addAll(display, new SeparatorMenuItem(), addItem, removeItem, new SeparatorMenuItem(), removeInvoice);


        Menu product = new Menu("Product");
        MenuItem addProduct = new MenuItem("Add");
        addItem.setOnAction(actionEvent -> {
            try {
                App.setRoot("addproduct");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        MenuItem restock = new MenuItem("Restock");
        removeItem.setOnAction(actionEvent -> {
            try {
                App.setRoot("restock");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        MenuItem removeProduct = new MenuItem("Remove");
        removeItem.setOnAction(actionEvent -> {
            try {
                App.setRoot("removeproduct");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        product.getItems().addAll(addProduct, restock, removeProduct);

        Menu client = new Menu("Client");
        MenuItem addClient = new MenuItem("Add");
        addClient.setOnAction(actionEvent -> {
            try {
                App.setRoot("addclient");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        MenuItem removeClient = new MenuItem("Remove");
        removeClient.setOnAction(actionEvent -> {
            try {
                App.setRoot("removeclient");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        client.getItems().addAll(addClient, removeClient);

        Menu options = new Menu("Options");
        MenuItem logout = new MenuItem("Log out");
        logout.setOnAction(actionEvent -> {
            //todo: disconnect or something
            try {
                App.setRoot("login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        options.getItems().add(logout);
        options.getItems().add(logout);

        this.getMenus().addAll(invoice, client, product, options);
    }
}
