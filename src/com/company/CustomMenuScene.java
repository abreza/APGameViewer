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
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.sql.rowset.spi.XmlWriter;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class CustomMenuScene {

    enum Season{
        SPRING, SUMMER, FALL, WINTER
    }

    enum Type{
        PLANT, TREE, TREE_FIELDS
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
                Type.TREE.name(), Type.TREE_FIELDS.name()));
        Label nameLabel = makeLabel("custom name: ");
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
                layout.getChildren().remove(3, layout.getChildren().size());
                switch (type){
                    case PLANT:
                        makeSceneForPlant(makeSceneForPlantProduct());
                        break;
                    case TREE:
                        makeSceneForTree(makeSceneForPlantProduct());
                        break;
                    case TREE_FIELDS:
                        makeSceneForTreeFields();
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

    private void makeSceneForTreeFields(){
        ArrayList<String> treeNames = xmlWriter.getTrees();
        treeNames.addAll(Arrays.asList(Names.PEACH_TREE.name(), Names.PEAR_TREE.name(),
                Names.POMEGRANATE_TREE.name(), Names.LEMON_TREE.name(),
                Names.APPLE_TREE.name(), Names.ORANGE_TREE.name()));
        HBox titleRow = makeHbox();
        Label titleLabel  = makeLabel("Tree Fields:");
        titleRow.getChildren().addAll(titleLabel);
        HBox firstFieldRow = makeHbox();
        Label firstLabel = makeLabel("First:");
        ChoiceBox firstCb = new ChoiceBox(FXCollections.observableArrayList(treeNames));
        firstFieldRow.getChildren().addAll(firstLabel, firstCb);
        HBox secondFieldRow = makeHbox();
        Label secondLabel = makeLabel("second:");
        ChoiceBox secondCb = new ChoiceBox(FXCollections.observableArrayList(treeNames));
        secondFieldRow.getChildren().addAll(secondLabel, secondCb);
        HBox thirdFieldRow = makeHbox();
        Label thirdLabel = makeLabel("third:");
        ChoiceBox thirdCb = new ChoiceBox(FXCollections.observableArrayList(treeNames));
        thirdFieldRow.getChildren().addAll(thirdLabel, thirdCb);
        HBox fourthFieldRow = makeHbox();
        Label fourthLabel = makeLabel("fourth:");
        ChoiceBox fourthCb = new ChoiceBox(FXCollections.observableArrayList(treeNames));
        fourthFieldRow.getChildren().addAll(fourthLabel, fourthCb);
        HBox fifthFieldRow = makeHbox();
        Label fifthLabel = makeLabel("fifth:");
        ChoiceBox fifthCb = new ChoiceBox(FXCollections.observableArrayList(treeNames));
        fifthFieldRow.getChildren().addAll(fifthLabel, fifthCb);
        HBox sixthFieldRow = makeHbox();
        Label sixthLabel = makeLabel("sixth:");
        ChoiceBox sixthCb = new ChoiceBox(FXCollections.observableArrayList(treeNames));
        sixthFieldRow.getChildren().addAll(sixthLabel, sixthCb);
        HBox endRow = makeHbox();
        Button addButton = makeButton();
        addButton.setText("add");
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xmlWriter.addTreeField(firstCb.getSelectionModel().getSelectedItem().toString(), 0);
                xmlWriter.addTreeField(secondCb.getSelectionModel().getSelectedItem().toString(), 1);
                xmlWriter.addTreeField(thirdCb.getSelectionModel().getSelectedItem().toString(), 2);
                xmlWriter.addTreeField(fourthCb.getSelectionModel().getSelectedItem().toString(), 3);
                xmlWriter.addTreeField(fifthCb.getSelectionModel().getSelectedItem().toString(), 4);
                xmlWriter.addTreeField(sixthCb.getSelectionModel().getSelectedItem().toString(), 5);
                Toast.makeText(primaryStage, "Added!", 500, 500, 500);
            }
        });
        endRow.getChildren().addAll(addButton);
        layout.getChildren().addAll(titleRow, firstFieldRow, secondFieldRow, thirdFieldRow,
                fourthFieldRow, fifthFieldRow, sixthFieldRow, endRow);

    }

    private void makeSceneForTree(ArrayList<Node> productNodes) {
        HBox titleRow = makeHbox();
        Label titleLabel  = makeLabel("Tree:");
        titleRow.getChildren().addAll(titleLabel);
        HBox costRow = makeHbox();
        Label costLabel = makeLabel("cost:");
        TextField costField = new TextField();
        costRow.getChildren().addAll(costLabel, costField);
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
                int treeCost = Integer.parseInt(costField.getText());
                xmlWriter.addTree(productName, treeCost);
                Toast.makeText(primaryStage, "Added!", 500, 500, 500);
            }
        });
        endRow.getChildren().addAll(addButton);
        layout.getChildren().addAll(titleRow, costRow, endRow);

    }

    private void makeSceneForPlant(ArrayList<Node> productNodes) {
        HBox titleRow = makeHbox();
        Label titleLabel  = makeLabel("Plant:");
        titleRow.getChildren().addAll(titleLabel);
        HBox costRow = makeHbox();
        Label costLabel = makeLabel("cost:");
        TextField costField = new TextField();
        costRow.getChildren().addAll(costLabel, costField);
        HBox ageRow = makeHbox();
        Label ageLabel = makeLabel("age:");
        TextField ageField = new TextField();
        ageRow.getChildren().addAll(ageLabel, ageField);
        HBox maxAgeRow = makeHbox();
        Label maxAgeLabel = makeLabel("maxAge:");
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
                Toast.makeText(primaryStage, "Added!", 500, 500, 500);
            }
        });
        endRow.getChildren().addAll(addButton);
        layout.getChildren().addAll(titleRow, costRow, ageRow, maxAgeRow, endRow);
    }

    private ArrayList<Node> makeSceneForPlantProduct() {
        HBox titleRow = makeHbox();
        Label titleLabel = makeLabel("Product:");
        titleRow.getChildren().addAll(titleLabel);
        HBox costRow = makeHbox();
        Label costLabel = makeLabel("cost:");
        TextField costField = new TextField();
        costRow.getChildren().addAll(costLabel, costField);
        HBox nameRow = makeHbox();
        Label nameLabel = makeLabel("name:");
        TextField nameField = new TextField();
        nameRow.getChildren().addAll(nameLabel, nameField);
        HBox energyRow = makeHbox();
        Label energyLabel = makeLabel("Energy:");
        TextField energyField = new TextField();
        energyRow.getChildren().addAll(energyLabel, energyField);
        HBox healthRow = makeHbox();
        Label healthLabel = makeLabel("Health:");
        TextField healthField = new TextField();
        healthRow.getChildren().addAll(healthLabel, healthField);
        HBox seasonRow = makeHbox();
        Label seasonLabel = makeLabel("season:");
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

    public static Label makeLabel(String text){
        Label label = new Label(text);
        label.setFont(new Font("Arial", 30));
        label.setTextFill(Paint.valueOf("#010221"));
        return label;
    }
}
