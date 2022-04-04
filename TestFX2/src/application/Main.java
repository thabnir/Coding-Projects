package application;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
	Color bgColor = Color.rgb(50, 168, 82, .3);

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
