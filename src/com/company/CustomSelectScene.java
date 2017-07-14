package com.company;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class CustomSelectScene {

    private Stage primaryStage;
    private VBox layout;
    private Scene scene;
    public static int WIDTH = 1280, HEIGHT = 960;

    public CustomSelectScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @SuppressWarnings("unchecked")
    public Scene makeScene() {
        layout = new VBox();
        layout.alignmentProperty().setValue(Pos.TOP_CENTER);
        layout.setSpacing(20);

        Button button1 = makeButton();

        ArrayList<Label> labels = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            labels.add(new Label());
        }

        HBox first = makeHbox();
        Label selectCustomLabel = CustomMenuScene.makeLabel("Select Custom:");
        File folder = new File(System.getProperty("user.dir") + "/customs");
        File[] filesList = folder.listFiles();
        String[] filesNames = new String[filesList.length + 1];
        for (int i = 0; i < filesList.length; i++) {
            filesNames[i + 1] = filesList[i].getName();
        }
        filesNames[0] = "None";
        ChoiceBox customSelectCb = new ChoiceBox(FXCollections.observableArrayList(filesNames));
        customSelectCb.getSelectionModel().selectFirst();
        first.getChildren().addAll(selectCustomLabel, customSelectCb);
        HBox second = makeHbox();
        button1.setText("Go!");
        second.getChildren().addAll(button1);
        setListeners(button1, customSelectCb);
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

    private void setListeners(Button button1, ChoiceBox customSelectCb) {
        button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Main.initCreateSingle();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String selectedString = customSelectCb.getSelectionModel().getSelectedItem().toString();
                if (!selectedString.equalsIgnoreCase("None")) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                Scanner scanner = new Scanner(new File(
                                        System.getProperty("user.dir") + "/customs/" + selectedString));
                                scanner.useDelimiter("\\Z");
                                String content = scanner.next();
                                Socket socket = new Socket("localhost", MapViewer.port);
                                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                                out.write(Main.playerId + " " + Main.gameID + " " + "xml/" + content);
                                out.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 400);
                }
                Main.main(FirstMenu.args);
                Platform.exit();
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
