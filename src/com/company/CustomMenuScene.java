package com.company;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.sql.rowset.spi.XmlWriter;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class CustomMenuScene {

    enum Season{
        SPRING, SUMMER, FALL, WINTER
    }

    enum Type{
        PLANT, TREE, PLAYER
    }

    private MyXmlWriter xmlWriter;
    private Stage primaryStage;
    private VBox layout;
    private Scene scene;
    public static int WIDTH = 1280, HEIGHT = 960;

    public CustomMenuScene(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene makeScene() {
        layout = new VBox();
        layout.alignmentProperty().setValue(Pos.TOP_CENTER);
        layout.setSpacing(30);

        HBox firstRow = makeHbox();
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(Type.PLANT.name(),
                Type.TREE.name(), Type.PLAYER.name()));
        Label nameLabel = new Label("custom name: ");
        TextField nameField = new TextField();
        Button saveButton = makeButton("Save");
        Button exitButton = makeButton("exit");
        setListeners(cb, saveButton, nameField, exitButton);
        firstRow.setSpacing(20);
        HBox secondRow = makeHbox();
        firstRow.getChildren().addAll(nameLabel, nameField, saveButton, exitButton);
        secondRow.getChildren().addAll(cb);
        layout.getChildren().addAll(new Label(), firstRow, secondRow);
        layout.setBackground(new Background(new BackgroundImage(
                new Image(this.getClass().getClassLoader().getResource("wooden-texture.jpg").toString()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(
                FirstScene.WIDTH, FirstScene.HEIGHT, false, false, true, true)
        )));
        xmlWriter = new MyXmlWriter();
        xmlWriter.init();
        scene = new Scene(layout, FirstScene.WIDTH, FirstScene.HEIGHT);
        return scene;
    }

    private void setListeners(ChoiceBox cb, Button saveButton, TextField nameField, Button exitButton) {
        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Type type = Type.values()[newValue.intValue()];
                switch (type){
                    case PLANT:
                        makeSceneForPlant(makeSceneForPlantProduct());
                }
            }
        });
        saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xmlWriter.setId(nameField.getText());
                xmlWriter.save();
            }
        });
        exitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setScene(new FirstScene(primaryStage).makeScene());
            }
        });
    }

    private void makeSceneForPlant(ArrayList<Node> productNodes) {
        HBox titleRow = makeHbox();
        Label titleLabel  = new Label("Plant:");
        titleRow.getChildren().addAll(titleLabel);
        HBox costRow = makeHbox();
        Label costLabel = new Label("cost:");
        TextField costField = new TextField();
        costRow.getChildren().addAll(costLabel, costField);
        HBox ageRow = makeHbox();
        Label ageLabel = new Label("age:");
        TextField ageField = new TextField();
        ageRow.getChildren().addAll(ageLabel, ageField);
        HBox maxAgeRow = makeHbox();
        Label maxAgeLabel = new Label("maxAge:");
        TextField maxAgeField = new TextField();
        maxAgeRow.getChildren().addAll(maxAgeLabel, maxAgeField);
        HBox endRow = makeHbox();
        Button addButton = makeButton();
        addButton.setText("add");
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int productCost = Integer.parseInt(((TextField) productNodes.get(0)).getText());
                String productName = ((TextField) productNodes.get(1)).getText();
                int productEnergy = Integer.parseInt(((TextField) productNodes.get(2)).getText());
                int productHealth = Integer.parseInt(((TextField) productNodes.get(3)).getText());
                Season productSeason = Season.valueOf(((ChoiceBox) productNodes.get(4)).getSelectionModel().
                        getSelectedItem().toString());
                xmlWriter.addPlantProduct(productCost, productName, productEnergy, productHealth, productSeason);
                int plantCost = Integer.parseInt(costField.getText());
                int plantAge = Integer.parseInt(ageField.getText());
                int plantMaxAge = Integer.parseInt(maxAgeField.getText());
                xmlWriter.addPlant(productName, plantCost, plantAge, plantMaxAge);
            }
        });
        endRow.getChildren().addAll(addButton);
        layout.getChildren().addAll(titleRow, costRow, ageRow, maxAgeRow, endRow);
    }

    private ArrayList<Node> makeSceneForPlantProduct() {
        layout.getChildren().remove(3, layout.getChildren().size());
        HBox titleRow = makeHbox();
        Label titleLabel = new Label("Product:");
        titleRow.getChildren().addAll(titleLabel);
        HBox costRow = makeHbox();
        Label costLabel = new Label("cost:");
        TextField costField = new TextField();
        costRow.getChildren().addAll(costLabel, costField);
        HBox nameRow = makeHbox();
        Label nameLabel = new Label("name:");
        TextField nameField = new TextField();
        nameRow.getChildren().addAll(nameLabel, nameField);
        HBox energyRow = makeHbox();
        Label energyLabel = new Label("Energy:");
        TextField energyField = new TextField();
        energyRow.getChildren().addAll(energyLabel, energyField);
        HBox healthRow = makeHbox();
        Label healthLabel = new Label("Health:");
        TextField healthField = new TextField();
        healthRow.getChildren().addAll(healthLabel, healthField);
        HBox seasonRow = makeHbox();
        Label seasonLabel = new Label("season:");
        ChoiceBox seasonChoice = new ChoiceBox(FXCollections.observableArrayList(Season.SPRING.name(), Season.SUMMER.name(),
                Season.FALL.name(), Season.WINTER.name()));
        seasonRow.getChildren().addAll(seasonLabel, seasonChoice);
        layout.getChildren().addAll(titleRow, costRow, nameRow, energyRow, healthRow, seasonRow);
        ArrayList<Node> output = new ArrayList<>();
        output.add(costField);
        output.add(nameField);
        output.add(energyField);
        output.add(healthField);
        output.add(seasonChoice);
        return output;

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

    private Button makeButton(String text) {
        Button button = makeButton();
        button.setText(text);
        return button;
    }
}
