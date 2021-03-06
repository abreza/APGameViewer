package com.company;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class FirstMenu extends Application {

    public static String[] args;
    public static Stage primaryStage;

    public static void main(String[] args) {
        FirstMenu.args = args;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        FirstMenu.primaryStage = primaryStage;
        FirstScene firstScene = new FirstScene(primaryStage);
        primaryStage.setScene(firstScene.makeScene());
        primaryStage.getIcons().add(new Image(this.getClass().getClassLoader().getResource("icon.jpg").toString()));
        primaryStage.setTitle("Harvest Moon");
        primaryStage.show();
    }
}
