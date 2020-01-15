package gip;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

import java.io.IOException;

public class App extends Application {
    private static Scene scene;
    private static Connection connection;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Login"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void attemptConnection(String user, String password) throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/invc?noAccessToProcedureBodies=true", user, password);
    }

    public static void endConnection() throws SQLException {
        connection.close();
    }

    public static ResultSet executeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public static int executeUpdate(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }

    public static PreparedStatement prepareStatement(String statement) throws SQLException {
        return connection.prepareStatement(statement);
    }

    public static CallableStatement prepareCall(String statement) throws SQLException {
        return connection.prepareCall(statement);
    }

    @Override
    public void stop() throws Exception {
        if (connection != null && !connection.isClosed()) endConnection();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }

}