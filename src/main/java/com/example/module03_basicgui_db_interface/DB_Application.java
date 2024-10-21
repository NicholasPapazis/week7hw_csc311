package com.example.module03_basicgui_db_interface;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.IOException;


public class DB_Application extends Application {

    public static void main(String[] args) {
        launch();
    }


    private Stage primaryStage;

    public void start(Stage primaryStage) {



        this.primaryStage = primaryStage;
        this.primaryStage.setResizable(false);
        showScene1();

    }

    //dislays splash screen
    private void showScene1() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("splash_screen.fxml"));
            Scene scene = new Scene(root, 850, 475);
            scene.getStylesheets().add("style.css");
            primaryStage.setScene(scene);
            primaryStage.show();
            changeScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //displays db interface screen
    public void changeScene() {
        try {
            Parent newRoot = FXMLLoader.load(getClass().getResource("db_interface_gui.fxml"));

            Scene currentScene = primaryStage.getScene();
            Parent currentRoot = currentScene.getRoot();
            currentScene.getStylesheets().add("style.css");
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(4), currentRoot);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(.1);
            fadeOut.setOnFinished(e -> {


                Scene newScene = new Scene(newRoot,850, 560);
                primaryStage.setScene(newScene);
                primaryStage.setTitle("Database Management System");

            });

            fadeOut.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}