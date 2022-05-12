package com.henry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * JavaFX App for pathfinding between nodes
 * TODO: add map generator, manual first, then maybe automatic? (could do mazes)
 */
public class App extends Application {

    private static Scene scene;

    public static final int gridSizeX = 50;
    public static final int gridSizeY = 20;

    @Override
    public void start(Stage stage) throws IOException {
        // stage = window including x and minimize, etc.
        // scene = stuff inside the window
        // scene = new Scene(loadFXML("primary"), 1920 / 2, 1080 / 2);
        stage.setScene(new Scene(createGrid(gridSizeX, gridSizeY)));
        stage.setTitle("Henry Pathfinding");

        // TODO: figure out how the hell to make it locate map.png
        stage.getIcons().add(new Image("/map.png"));
        stage.show();
    }

    GridPane createGrid(int width, int height) {
        GridPane grid = new GridPane();
        for (int x = 0; x < gridSizeX; x++) {
            for (int y = 0; y < gridSizeY; y++) {
                // Color c = (x + y) % 2 == 0 ? Color.rgb(240, 217, 181) : Color.rgb(181, 136,
                // 99);
                //// BackgroundFill b = new BackgroundFill(c);
                Button button = new Button();
                //// button.setBackground(c);
                grid.add(button, x, y);

            }
        }
        return grid;
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}