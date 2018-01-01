package by.overpass.hungerprovision.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import by.overpass.hungerprovision.InfoOperations;
import by.overpass.hungerprovision.dao.IngredientDAO;
import by.overpass.hungerprovision.dao.JDBCIngredientDAOImpl;
import by.overpass.hungerprovision.model.Ingredient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {
	
	@FXML private TextField txtName;
	@FXML private RadioButton rbtnKilos;
	@FXML private RadioButton rbtnPieces;
	@FXML private ToggleGroup groupUnits;
	@FXML private TextField txtQuantity;
	@FXML private TextField txtProvider;
	@FXML private DatePicker dtpckrImportDate;
	@FXML private DatePicker dtpckrExpiryDate;
	@FXML private Button btnDelete;
	@FXML private Button btnUpdate;
	@FXML private Button btnNew;
	@FXML private TableView<Ingredient> tblViewIngredients;
	@FXML private TableColumn<Ingredient, String> clmnName;
	@FXML private TableColumn<Ingredient, Double> clmnQuantity;
	@FXML private TableColumn<Ingredient, String> clmnUnits;
	@FXML private TableColumn<Ingredient, String> clmnProvider;
	@FXML private TableColumn<Ingredient, Date> clmnImportDate;
	@FXML private TableColumn<Ingredient, Date> clmnExpiryDate;
	private int chosenIngredientId;
	private InputHelper inputHelper = new InputHelper();
	private IngredientDAO dao = null;
	private ObservableList<Ingredient> observables = FXCollections.observableArrayList();
	public Stage stage;
	
	private boolean openConnection() {
		try {
			dao = new JDBCIngredientDAOImpl();
		} catch (ClassNotFoundException | SQLException e1) {
			Alert databaseConnectionAlert = new Alert(AlertType.ERROR);
			databaseConnectionAlert.setTitle("Ошибка подключения");
			databaseConnectionAlert.setContentText("Повторите попытку позже");
			databaseConnectionAlert.setHeaderText("Не удалось подключиться к базе данных");
			databaseConnectionAlert.showAndWait();
			e1.printStackTrace();
		}
		
		return dao != null;
	}
	
	private boolean closeConnection() {
		boolean connectionClosed = false;
		try {
			connectionClosed = dao.disconnect();
		} catch (SQLException e) {
			Alert databaseConnectionAlert = new Alert(AlertType.ERROR);
			databaseConnectionAlert.setTitle("Ошибка подключения");
			databaseConnectionAlert.setContentText("Повторите попытку позже");
			databaseConnectionAlert.setHeaderText("Не удалось подключиться к базе данных");
			databaseConnectionAlert.showAndWait();
			e.printStackTrace();
		}
		
		return connectionClosed;
	}
	
	private void completeTable() {
		try {
			fillTable();
		} catch (SQLException e) {
			Alert databaseConnectionAlert = new Alert(AlertType.ERROR);
			databaseConnectionAlert.setTitle("Ошибка подключения");
			databaseConnectionAlert.setContentText("Повторите попытку позже");
			databaseConnectionAlert.setHeaderText("Не удалось подключиться к базе данных");
			databaseConnectionAlert.showAndWait();
			e.printStackTrace();
		}
	}
	
	private boolean offerChoice(ActionEvent event, InfoOperations operation) {
		Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        Parent parent;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			parent = fxmlLoader.load(getClass().getResource("../confirmation_window.fxml").openStream());
			Scene dialogScene = new Scene(parent);
			dialogScene.getStylesheets().add("main_window.css");
	        dialog.setScene(dialogScene);
	        dialog.getIcons().add(new Image("file:resources/images/hunger.png"));
			PopupDialogController dialogController = fxmlLoader.getController();
			dialogController.dialogStage = dialog;
			System.out.println("DIALOG STAGE IN MAIN CONTROLLER: " + dialog);
			System.out.println("DIALOG STAGE IN DIALOG CONTROLLER: " + dialogController.dialogStage);
			dialogController.mainController = this;
			System.out.println("MAIN CONTROLLER IN MAIN CONTROLLER: " + this);
			System.out.println("MAIN CONTROLLER IN DIALOG CONTROLLER: " + dialogController.mainController);
			dialogController.currentOperation = operation;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		dialog.show();
        
		return true;
	}
	
	@FXML
	public void saveInfo() {
		System.out.println("SAVE");
	}
	
	@FXML
	public void updateInfo(ActionEvent event) {
		offerChoice(event, InfoOperations.UPDATE);
	}
	
	public void updateRecord() {
		openConnection();
		
		System.out.println("UPDATE");
		if (!inputHelper.isInputValid()) {
			System.out.println("input is invalid");
			inputHelper.showInvalidInputAlert();
			return;
		}
		
		dao.updateIngredient(new Ingredient(chosenIngredientId, txtName.getText(),
				rbtnKilos.isSelected() ? rbtnKilos.getText() : rbtnPieces.getText(),
				Double.parseDouble(txtQuantity.getText()), txtProvider.getText(),
				Date.valueOf(dtpckrImportDate.getValue()),
				Date.valueOf(dtpckrExpiryDate.getValue())));
		
		completeTable();
		closeConnection();
	}
	
	@FXML
	public void deleteInfo(ActionEvent event) {
		offerChoice(event, InfoOperations.DELETE);
	}
	
	public void deleteRecord() {
		openConnection();
		
		System.out.println("DELETE");
		dao.deleteIngredient(new Ingredient(chosenIngredientId, txtName.getText(),
				rbtnKilos.isSelected() ? rbtnKilos.getText() : rbtnPieces.getText(),
				Double.parseDouble(txtQuantity.getText()), txtProvider.getText(),
				Date.valueOf(dtpckrImportDate.getValue()),
				Date.valueOf(dtpckrExpiryDate.getValue())));
		
		inputHelper.clearInputFields();
		btnDelete.setDisable(true);
		btnUpdate.setDisable(true);
		
		completeTable();
		closeConnection();
	}
	
	@FXML
	public void createNewRecord(ActionEvent event) {
		offerChoice(event, InfoOperations.CREATE);
	}
	
	public void createRecord() {
		openConnection();
		
		System.out.println("CREATE");
		if (!inputHelper.isInputValid()) {
			System.out.println("input is invalid");
			inputHelper.showInvalidInputAlert();
			return;
		}
		
		dao.insertIngredient(new Ingredient(txtName.getText(),
				rbtnKilos.isSelected() ? rbtnKilos.getText() : rbtnPieces.getText(),
				Double.parseDouble(txtQuantity.getText()), txtProvider.getText(),
				Date.valueOf(dtpckrImportDate.getValue()),
				Date.valueOf(dtpckrExpiryDate.getValue())));
		System.out.println("dao insertion has been conducted");
		
		completeTable();
		closeConnection();
	}

	public void fillTable() throws SQLException {
		openConnection();
		
		observables = FXCollections.observableArrayList(dao.selectIngredients());
		tblViewIngredients.setItems(observables);
		
		closeConnection();
 	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		clmnName.setCellValueFactory(
				new PropertyValueFactory<Ingredient, String>("name"));
		clmnQuantity.setCellValueFactory(
				new PropertyValueFactory<Ingredient, Double>("quantity"));
		clmnUnits.setCellValueFactory(
				new PropertyValueFactory<Ingredient, String>("units"));
		clmnProvider.setCellValueFactory(
				new PropertyValueFactory<Ingredient, String>("provider"));
		clmnImportDate.setCellValueFactory(
				new PropertyValueFactory<Ingredient, Date>("importDate"));
		clmnExpiryDate.setCellValueFactory(
				new PropertyValueFactory<Ingredient, Date>("expiryDate"));
		
		btnUpdate.setDisable(true);
		btnDelete.setDisable(true);
		
		inputHelper.restrictQuantityInput();
		groupUnits.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) {
				inputHelper.restrictQuantityInput();
			}
		});
		
		dtpckrImportDate.setEditable(false);
		dtpckrExpiryDate.setEditable(false);
		
		tblViewIngredients.setOnMouseClicked(e -> {
			Ingredient ingredient = (Ingredient) tblViewIngredients
					.getSelectionModel()
					.getSelectedItem();
			boolean allocatedAnItem = true;
			
			try {
				chosenIngredientId = ingredient.getId();
				txtName.setText(ingredient.getName());
				if (ingredient.getUnits().equals("шт")) 
					groupUnits.selectToggle(rbtnPieces);
				else 
					groupUnits.selectToggle(rbtnKilos);
				if (rbtnPieces.isSelected())
					txtQuantity.setText(String.valueOf((int) ingredient.getQuantity()));
				else
					txtQuantity.setText(String.valueOf(ingredient.getQuantity()));
				txtProvider.setText(ingredient.getProvider());
				dtpckrImportDate.setValue(ingredient.getImportDate().toLocalDate());
				dtpckrExpiryDate.setValue(ingredient.getExpiryDate().toLocalDate());
			} catch (NullPointerException ex) {
				allocatedAnItem = false;
			}
			
			if (allocatedAnItem) {
				btnNew.setDisable(false);
				btnDelete.setDisable(false);
				btnUpdate.setDisable(false);
			}
		});
	}
	
	private class InputHelper implements ChangeListener<String>{
		
		private void clearInputFields() {
			txtName.setText("");
			txtProvider.setText("");
			txtQuantity.setText("");
			dtpckrImportDate.getEditor().clear();
			dtpckrExpiryDate.getEditor().clear();
			if (rbtnKilos.isSelected()) rbtnKilos.setSelected(false);
			if (rbtnPieces.isSelected()) rbtnPieces.setSelected(false);
		}
		
		private boolean isInputValid() {
			if (txtName.getText().isEmpty())
				return false;
			else if (txtProvider.getText().isEmpty())
				return false;
			else if (txtQuantity.getText().isEmpty())
				return false;
			else if (!txtQuantity.getText().isEmpty()) {
				try {
					Double d = Double.parseDouble(txtQuantity.getText());
				} catch (NumberFormatException e) {
					return false;
				}
			} else if (!(rbtnKilos.isSelected() || rbtnPieces.isSelected())) {
				return false;
			} else if (dtpckrImportDate.getValue() != null) {
				if (dtpckrImportDate.getValue().toString().trim().equals(""))
					return false;
			} else if (dtpckrExpiryDate.getValue() != null) {
				if (dtpckrExpiryDate.getValue().toString().trim().equals(""))
					return false;
			}
			
			return true;
		}
		
		private void restrictQuantityInput() {
			clearQuantityInput();
			
			if (rbtnKilos.isSelected()) {
				Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d*");
				TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
				    return pattern.matcher(change.getControlNewText()).matches() ? change : null;
				});
				txtQuantity.setTextFormatter(formatter);
				txtQuantity.textProperty().removeListener(this);
			} else if (rbtnPieces.isSelected()) {
				txtQuantity.textProperty().addListener(this);
				txtQuantity.setTextFormatter(null);
			} else {
				System.out.println("Quantity input restrictions: allow any.");
			}
		}
		
		private void clearQuantityInput() {
			txtQuantity.setText("");
		}
		
		private void showInvalidInputAlert() {
			Alert invalidInputAlert = new Alert(AlertType.ERROR);
			invalidInputAlert.setTitle("Ошибка ввода");
			invalidInputAlert.setContentText("Пожалуйста, введите данные");
			invalidInputAlert.setHeaderText("Недостаточно данных");
			invalidInputAlert.showAndWait();
		}

		public void changed(ObservableValue<? extends String> observableValue,
				String oldValue, String newValue) {
			if (!newValue.matches("\\d*"))
		        txtQuantity.setText(newValue.replaceAll("[^\\d]", ""));
		}
		
	}
	
}
