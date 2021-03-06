package gip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.stage.Modality;
import javafx.stage.Stage;

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

	public void openDialog(ActionEvent actionEvent) throws IOException {
		openDialog(((Button) actionEvent.getSource()).getId());
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
            boolean moreThanClientAccess = grants.contains("GRANT SELECT, INSERT, UPDATE, DELETE ON `invc`.`invoice`") && grants.contains("GRANT SELECT, INSERT, UPDATE, DELETE ON `invc`.`invoiceElement`");
            if (moreThanClientAccess) {
                if (grants.contains("GRANT SELECT, INSERT, UPDATE, DELETE ON `invc`.`client`") && grants.contains("GRANT SELECT, INSERT, UPDATE, DELETE ON `invc`.`product`") && grants.contains("GRANT LOCK TABLES, EXECUTE ON `invc`.*"))
                    grantAccess("adminAccess");
                else if (grants.contains("GRANT SELECT, UPDATE ON `invc`.`product`") && grants.contains("GRANT SELECT ON `invc`.`client`") && grants.contains("GRANT EXECUTE ON `invc`.*"))
                    grantAccess("employeeAccess");
            } else if (grants.contains("GRANT SELECT ON `invc`.`invoiceElement`") && grants.contains("GRANT SELECT ON `invc`.`product`") && grants.contains("GRANT EXECUTE ON PROCEDURE `invc`.`gen`") && grants.contains("GRANT EXECUTE ON PROCEDURE `invc`.`num`")) grantAccess("clientAccess");

		} catch (SQLException e) {
			e.printStackTrace();
		}
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

	public void backup() {
    	ProcessBuilder processBuilder = new ProcessBuilder();
    	processBuilder.command("bash", "-c", "mysqldump -u root invc > src/main/backupFileHere.sql");

    	try {
    		Process process = processBuilder.start();

    		StringBuilder output = new StringBuilder();

    		BufferedReader reader = new BufferedReader(
    				new InputStreamReader(process.getInputStream()));

    		String line;
    		while ((line = reader.readLine()) != null) {
    			output.append(line + "\n");
    		}

    		int exitVal = process.waitFor();
    		if (exitVal == 0) {
    			System.out.println("Success!");
    			System.out.println(output);
    		} else {
    			//abnormal...
    		}

    	} catch (IOException | InterruptedException e) {
    		e.printStackTrace();
    	}

		Alert ok = new Alert(Alert.AlertType.INFORMATION);
        ok.setTitle("Backup");
        ok.setHeaderText("Success");
        ok.setContentText("Done");
        ok.showAndWait();
	}

	public void restore() {
    	ProcessBuilder processBuilder = new ProcessBuilder();
    	processBuilder.command("bash", "-c", "mysql -u root invc < src/main/backupFileHere.sql");

    	try {

    		Process process = processBuilder.start();

    		StringBuilder output = new StringBuilder();

    		BufferedReader reader = new BufferedReader(
    				new InputStreamReader(process.getInputStream()));

    		String line;
    		while ((line = reader.readLine()) != null) {
    			output.append(line + "\n");
    		}

    		int exitVal = process.waitFor();
    		if (exitVal == 0) {
    			System.out.println("Success!");
    			System.out.println(output);
    		} else {
    			//abnormal...
    		}

    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}

        Alert ok = new Alert(Alert.AlertType.INFORMATION);
        ok.setTitle("Restore");
        ok.setHeaderText("Success");
        ok.setContentText("Done");
        ok.showAndWait();
	}

	public void grantAccess(String accessLevel) {
		if (!accessLevel.equals("clientAccess"))
			App.setAllInvoicesAvailable(true);
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
		for (Node button : buttonGroup)
			button.setDisable(false);
	}

}
