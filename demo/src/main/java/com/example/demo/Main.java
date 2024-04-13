package com.example.demo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Controller_View.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        String css = this.getClass().getResource("/Css/App.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.setResizable(false);
        Image stageIcon = new Image(getClass().getResourceAsStream("/Photo/operating-system.png"));
        stage.getIcons().add(stageIcon);
        stage.setTitle("Process Scheduling");
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}