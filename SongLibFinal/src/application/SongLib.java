//Mikhail Prigozhiy and Michael Calabrese

package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class SongLib extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Song Library");
			primaryStage.setMinHeight(440);
			primaryStage.setMinWidth(650);
			primaryStage.setResizable(false);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/application/Layout.fxml")); 
			BorderPane root = (BorderPane) loader.load();
			LayoutController controller = loader.getController();
			controller.start(primaryStage);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
