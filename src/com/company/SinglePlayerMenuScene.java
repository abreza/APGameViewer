package com.company;


import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class SinglePlayerMenuScene {
    private Stage primaryStage;
    private VBox layout;
    private Scene scene;
    public static int WIDTH = 1280, HEIGHT = 960;

    public SinglePlayerMenuScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene makeScene() {
        layout = new VBox();
        layout.alignmentProperty().setValue(Pos.TOP_CENTER);
        layout.setSpacing(20);

        Button button1 = makeButton(), button2 = makeButton(), button3 = makeButton();

        ArrayList<Label> labels = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            labels.add(new Label());
        }
        button1.setText("Create new game");
        HBox first = makeHbox();
        ImageView firstIcon = new ImageView(new Image(this.getClass().getClassLoader().getResource("singleplayer-icon.png").toString()));
        first.getChildren().addAll(button1, firstIcon);
        button2.setText("Load game");
        HBox second = makeHbox();
        ImageView secondIcon = new ImageView(new Image(this.getClass().getClassLoader().getResource("multiplayer-icon.png").toString()));
        second.getChildren().addAll(button2, secondIcon);

        setListeners(button1, button2);
        layout.getChildren().addAll(labels);
        layout.getChildren().addAll(first, second);
        layout.setBackground(new Background(new BackgroundImage(
                new Image(this.getClass().getClassLoader().getResource("wallpaper-firstMenu.jpg").toString()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(
                FirstScene.WIDTH, FirstScene.HEIGHT, false, false, true, true)
        )));
        scene = new Scene(layout, FirstScene.WIDTH, FirstScene.HEIGHT);
        return scene;
    }

    private void setListeners(Button button1, Button button2) {
        button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setScene(new CustomSelectScene(primaryStage).makeScene());
            }
        });

    }

    private HBox makeHbox() {
        HBox output = new HBox();
        output.alignmentProperty().setValue(Pos.CENTER);
        return output;
    }

    private Button makeButton() {
        Button button = new Button();
        button.setStyle("-fx-font: 22 arial; -fx-base: #497de5;");
        button.setMinWidth(200);
        return button;
    }
}
