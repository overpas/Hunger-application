package by.overpass.hungerprovision;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("ui.fxml"));
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add("style.css");
		
		stage.setScene(scene);
		stage.getIcons().add(new Image("file:resources/images/hunger.png"));
		stage.show();
	}

}
