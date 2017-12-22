package by.overpass.hungerprovision;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mysql.fabric.jdbc.ErrorReportingExceptionInterceptor;

import by.overpass.hungerprovision.dao.IngredientDAO;
import by.overpass.hungerprovision.dao.JDBCIngredientDAOImpl;
import by.overpass.hungerprovision.model.Ingredient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
		
	}
	
	@FXML
	public void updateInfo() {
		
	}
	
	@FXML
	public void deleteInfo() {
		
	}
	
	@FXML
	public void createNewRecord() {
		
	}

	public void fillTable() throws SQLException {
		/*clmnName.setCellValueFactory(
				new PropertyValueFactory<Ingredient, String>("name"));
		clmnQuantity.setCellValueFactory(
				new PropertyValueFactory<Ingredient, Double>("quantity"));
		clmnProvider.setCellValueFactory(
				new PropertyValueFactory<Ingredient, String>("provider"));
		clmnImportDate.setCellValueFactory(
				new PropertyValueFactory<Ingredient, Date>("importDate"));
		clmnExpiryDate.setCellValueFactory(
				new PropertyValueFactory<Ingredient, Date>("expiryDate"));*/
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
		btnNew.setDisable(true);
		
		tblViewIngredients.setOnMouseClicked(e -> {
			Ingredient ingredient = (Ingredient) tblViewIngredients
					.getSelectionModel()
					.getSelectedItem();
			
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
			} catch (NullPointerException ex) {}
		});
	}
}
