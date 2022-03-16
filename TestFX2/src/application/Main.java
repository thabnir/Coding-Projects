package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class Main extends Application {
	Color bgColor = Color.rgb(50, 168, 82, .7);
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		Group root = new Group();
		Scene scene = new Scene(root, bgColor);
		stage.setTitle("Pog");
		stage.setScene(scene);
		stage.show();
	}
}
