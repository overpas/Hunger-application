package by.overpass.hungerprovision.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import by.overpass.hungerprovision.InfoOperations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PopupDialogController implements Initializable {
	
	@FXML private Button btnOk;
	@FXML private Button btnUndo;
	public Stage dialogStage;
	public MainController mainController;
	public InfoOperations currentOperation;
	
	public PopupDialogController() {
		System.out.println("A PopupDialogController instance has been created.");
	}

	@FXML
	public void confirmChange(ActionEvent event) {
		System.out.println("CONFIRMED");
		switch (currentOperation) {
		case CREATE:
			mainController.createRecord();
			break;
		case DELETE:
			mainController.deleteRecord();
			break;
		case UPDATE:
			mainController.updateRecord();
			break;
		default:
			break;
		}
		dialogStage.close();
	}
	
	@FXML
	public void stopChange(ActionEvent event) {
		System.out.println("CANCELLED");
		dialogStage.close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

}
