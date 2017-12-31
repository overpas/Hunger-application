package by.overpass.hungerprovision.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import by.overpass.hungerprovision.ConfirmationState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class PopupDialogController implements Initializable {
	
	@FXML private Button btnOk;
	@FXML private Button btnUndo;

	@FXML
	public void confirmChange(ActionEvent event) {
		System.out.println(ConfirmationState.CONFIRMED);
		ConfirmationState.currentState = ConfirmationState.CONFIRMED;
	}
	
	@FXML
	public void stopChange(ActionEvent event) {
		System.out.println(ConfirmationState.CANCELLED);
		ConfirmationState.currentState = ConfirmationState.CANCELLED;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

}
