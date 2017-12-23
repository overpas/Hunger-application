package by.overpass.hungerprovision;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import by.overpass.hungerprovision.dao.IngredientDAO;
import by.overpass.hungerprovision.dao.JDBCIngredientDAOImpl;
import by.overpass.hungerprovision.model.Ingredient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class Controller implements Initializable {
	
	@FXML private TextField txtName;
	@FXML private RadioButton rbtnKilos;
	@FXML private RadioButton rbtnPieces;
	@FXML private ToggleGroup groupUnits;
	@FXML private TextField txtQuantity;
	@FXML private TextField txtProvider;
	@FXML private DatePicker dtpckrImportDate;
	@FXML private DatePicker dtpckrExpiryDate;
	@FXML private Button btnSave;
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
	private InputHelper inputHelper = new InputHelper();
	private static IngredientDAO dao;
	private static ObservableList<Ingredient> observables = FXCollections.observableArrayList();
	
	static {
		try {
			dao = new JDBCIngredientDAOImpl();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	public void saveInfo() {
		System.out.println("SAVE");
	}
	
	@FXML
	public void updateInfo() {
		System.out.println("UPDATE");
	}
	
	@FXML
	public void deleteInfo() {
		System.out.println("DELETE");
	}
	
	@FXML
	public void createNewRecord() {
		System.out.println("CREATE");
		if (!inputHelper.isInputValid()) {
			System.out.println("input is invalid");
			// TODO
			return;
		}
		dao.insertIngredient(new Ingredient(txtName.getText(),
				rbtnKilos.isSelected() ? rbtnKilos.getText() : rbtnPieces.getText(),
				Double.parseDouble(txtQuantity.getText()), txtProvider.getText(),
				Date.valueOf(dtpckrImportDate.getValue()),
				Date.valueOf(dtpckrExpiryDate.getValue())));
		System.out.println("dao insertion has been conducted");
		try {
			fillTable();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void fillTable() throws SQLException {
		observables = FXCollections.observableArrayList(dao.selectIngredients());
		tblViewIngredients.setItems(observables);
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
		btnSave.setDisable(true);
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
				txtName.setText(ingredient.getName());
				if (ingredient.getUnits().equals("רע")) 
					groupUnits.selectToggle(rbtnPieces);
				else 
					groupUnits.selectToggle(rbtnKilos);
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
				btnSave.setDisable(true);
				btnUpdate.setDisable(false);
			}
		});
	}
	
	private class InputHelper implements ChangeListener<String>{
		
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

		public void changed(ObservableValue<? extends String> observableValue,
				String oldValue, String newValue) {
			if (!newValue.matches("\\d*"))
		        txtQuantity.setText(newValue.replaceAll("[^\\d]", ""));
		}
		
	}
	
}
