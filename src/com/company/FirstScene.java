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

public class FirstScene {

    private Stage primaryStage;
    private VBox layout;
    private Scene scene;
    public static int WIDTH = 1280, HEIGHT = 960;

    public FirstScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene makeScene() {
        layout = new VBox();
        layout.alignmentProperty().setValue(Pos.TOP_CENTER);
        layout.setSpacing(20);

        Button button1 = makeButton(), button2 = makeButton(), button3 = makeButton(), button4 = makeButton();

        ArrayList<Label> labels = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            labels.add(new Label());
        }
        button1.setText("Single Player");
        HBox first = makeHbox();
        button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Main.main(FirstMenu.args);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Platform.exit();
            }
        });
        ImageView firstIcon = new ImageView(new Image(this.getClass().getClassLoader().getResource("singleplayer-icon.png").toString()));
        first.getChildren().addAll(button1, firstIcon);
        button2.setText("MultiPlayer");
        HBox second = makeHbox();
        ImageView secondIcon = new ImageView(new Image(this.getClass().getClassLoader().getResource("multiplayer-icon.png").toString()));
        second.getChildren().addAll(button2, secondIcon);
        button3.setText("Custom Game");
        HBox third = makeHbox();
        ImageView thirdIcon = new ImageView(new Image(this.getClass().getClassLoader().getResource("custom-icon1.png").toString()));
        third.getChildren().addAll(button3, thirdIcon);
        button4.setText("Exit");
        button4.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Platform.exit();
            }
        });
        HBox fourth = makeHbox();
        ImageView fourthIcon = new ImageView(new Image(this.getClass().getClassLoader().getResource("exit-icon.png").toString()));
        fourth.getChildren().addAll(button4, fourthIcon);

        layout.getChildren().addAll(labels);
        layout.getChildren().addAll(first, second, third, fourth);
        layout.setBackground(new Background(new BackgroundImage(
                new Image(this.getClass().getClassLoader().getResource("wallpaper-firstMenu.jpg").toString()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(
                FirstScene.WIDTH, FirstScene.HEIGHT, false, false, true, true)
        )));
        scene = new Scene(layout, FirstScene.WIDTH, FirstScene.HEIGHT);
        return scene;
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
