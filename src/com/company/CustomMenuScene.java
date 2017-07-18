package com.company;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class CustomMenuScene {

    enum Season {
        SPRING, SUMMER, FALL, WINTER
    }

    enum Type {
        PLANT, TREE, TREE_FIELDS, COOK, PLAYER, DRUG, MACHINE, MISSION
    }

    enum CookIngredient {
        BREAD, CHICKEN_MEAT, ORANGE, POTATO, CARROT, GREENGAGE, MELON, FLOUR, SHEEP_MEAT,
        PINEAPPLE, STRAWBERRY, PEPPER, PEACH, PEAR, FISH_MEAT, GARLIC, PEAS, LETTUCE, EGGPLANT, LEMON,
        SPICE, COW_MEAT, POMEGRANATE, CUCUMBER, WATERMELON, ONION, TURNIP, APPLE,
        OIL, SALT, LEMON_JUICE, MILK, CHEESE, EDG, SUGAR
    }

    enum CookingTool {
        FRYING_PAN, KNIFE, MIXER, POT, OVEN
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
                Type.TREE.name(), Type.TREE_FIELDS.name(), Type.COOK.name(), Type.PLAYER.name(),
                Type.DRUG.name(), Type.MACHINE.name(), Type.MISSION.name()));
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
                layout.setSpacing(30);
                switch (type) {
                    case PLANT:
                        makeSceneForPlant(makeSceneForPlantProduct());
                        break;
                    case TREE:
                        makeSceneForTree(makeSceneForPlantProduct());
                        break;
                    case TREE_FIELDS:
                        makeSceneForTreeFields();
                        break;
                    case COOK:
                        makeSceneForCook();
                        break;
                    case PLAYER:
                        makeSceneForPlayer();
                        break;
                    case DRUG:
                        makeSceneForDrug();
                        break;
                    case MACHINE:
                        makeSceneForMachine();
                        break;
                    case MISSION:
                        makeSceneForMission();
                        break;
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

    private void makeSceneForMission() {
        VBox layout = new VBox();
        layout.alignmentProperty().setValue(Pos.TOP_CENTER);
        layout.setSpacing(30);
        layout.setBackground(new Background(new BackgroundImage(
                new Image(this.getClass().getClassLoader().getResource("wooden-texture.jpg").toString()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(
                FirstScene.WIDTH, FirstScene.HEIGHT, false, false, true, true)
        )));
        final int[] inputsNumber = {0}, outputsNumber = {0};
        ArrayList<ChoiceBox> choiceBoxes = new ArrayList<>();
        ArrayList<TextField> numberFields = new ArrayList<>();
        ArrayList<String> inputs = new ArrayList<>();
        for (int i = 0; i < CookIngredient.values().length; i++) {
            inputs.add(CookIngredient.values()[i].name());
        }
        inputs.addAll(xmlWriter.getPlantProducts());
        HBox titleRow = makeHbox();
        Label titleLabel = makeLabel("Mission:");
        titleRow.getChildren().addAll(titleLabel);
        HBox addInputRow = makeHbox();
        Button addInputButton = makeButton("Add input");
        addInputButton.setOnMouseClicked(event -> {
            inputsNumber[0]++;
            HBox inputRow = makeHbox();
            Label inputLabel = makeLabel("input" + inputsNumber[0] + ":");
            ChoiceBox inputChoice = new ChoiceBox(FXCollections.observableArrayList(inputs));
            choiceBoxes.add(inputChoice);
            Label numberLabel = makeLabel("Number:");
            TextField numberField = new TextField();
            numberFields.add(numberField);
            inputRow.getChildren().addAll(inputLabel, inputChoice, numberLabel, numberField);
            layout.getChildren().add(inputsNumber[0], inputRow);
        });
        addInputRow.getChildren().addAll(addInputButton);
        HBox nameRow = makeHbox();
        Label nameLabel = makeLabel("name:");
        TextField nameField = new TextField();
        nameRow.getChildren().addAll(nameLabel, nameField);
        HBox feeRow = makeHbox();
        Label feeLabel = makeLabel("feeContract:");
        TextField feeField = new TextField();
        feeRow.getChildren().addAll(feeLabel, feeField);
        HBox timeRow = makeHbox();
        Label timeLabel = makeLabel("timesNeeded:");
        TextField timeField = new TextField();
        timeRow.getChildren().addAll(timeLabel, timeField);
        HBox endRow = makeHbox();
        Button addButton = makeButton();
        addButton.setText("add");
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    HashMap<String, Integer> inputNames = new HashMap<>();
                    for (int i = 0; i < choiceBoxes.size(); i++) {
                        if (i + 1 <= inputsNumber[0])
                            inputNames.put(choiceBoxes.get(i).getSelectionModel().getSelectedItem().toString(),
                                    Integer.parseInt(numberFields.get(i).getText()));
                    }
                    String name = nameField.getText();
                    int fee = Integer.parseInt(feeField.getText());
                    int time = Integer.parseInt(timeField.getText());
                    xmlWriter.addMission(inputNames, name, fee, time);
                    Toast.makeText(primaryStage, "Added!", 500, 500, 500);
                }catch (NumberFormatException e){
                    Toast.makeText(primaryStage, "please write a number", 500, 500, 500);
                }
            }
        });
        endRow.getChildren().addAll(addButton);
        ScrollPane scrollPane = new ScrollPane();
        layout.getChildren().addAll(titleRow, addInputRow, nameRow
                , feeRow, timeRow, endRow);
        scrollPane.setContent(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.layout.getChildren().addAll(scrollPane);
    }

    private void makeSceneForMachine() {
        VBox layout = new VBox();
        layout.alignmentProperty().setValue(Pos.TOP_CENTER);
        layout.setSpacing(30);
        layout.setBackground(new Background(new BackgroundImage(
                new Image(this.getClass().getClassLoader().getResource("wooden-texture.jpg").toString()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(
                FirstScene.WIDTH, FirstScene.HEIGHT, false, false, true, true)
        )));
        final int[] inputsNumber = {0}, outputsNumber = {0};
        ArrayList<ChoiceBox> choiceBoxes = new ArrayList<>();
        ArrayList<String> inputs = new ArrayList<>(), outpus = new ArrayList<>();
        for (int i = 0; i < CookIngredient.values().length; i++) {
            inputs.add(CookIngredient.values()[i].name());
            outpus.add(CookIngredient.values()[i].name());
        }
        inputs.addAll(xmlWriter.getPlantProducts());
        outpus.addAll(xmlWriter.getPlantProducts());
        HBox titleRow = makeHbox();
        Label titleLabel = makeLabel("Machine:");
        titleRow.getChildren().addAll(titleLabel);
        HBox addInputRow = makeHbox();
        Button addInputButton = makeButton("Add input");
        addInputButton.setOnMouseClicked(event -> {
            inputsNumber[0]++;
            HBox inputRow = makeHbox();
            Label inputLabel = makeLabel("input" + inputsNumber[0] + ":");
            ChoiceBox inputChoice = new ChoiceBox(FXCollections.observableArrayList(inputs));
            choiceBoxes.add(inputChoice);
            inputRow.getChildren().addAll(inputLabel, inputChoice);
            layout.getChildren().add(inputsNumber[0], inputRow);
        });
        addInputRow.getChildren().addAll(addInputButton);
        HBox addOutputRow = makeHbox();
        Button addOutputButton = makeButton("Add Output");
        addOutputButton.setOnMouseClicked(event -> {
            outputsNumber[0]++;
            HBox outputRow = makeHbox();
            Label outputLabel = makeLabel("Output" + outputsNumber[0] + ":");
            ChoiceBox outputChoice = new ChoiceBox(FXCollections.observableArrayList(outpus));
            choiceBoxes.add(outputChoice);
            outputRow.getChildren().addAll(outputLabel, outputChoice);
            layout.getChildren().add(layout.getChildren().size() - 4, outputRow);
        });
        addOutputRow.getChildren().addAll(addOutputButton);
        HBox nameRow = makeHbox();
        Label nameLabel = makeLabel("name:");
        TextField nameField = new TextField();
        nameRow.getChildren().addAll(nameLabel, nameField);
        HBox costRow = makeHbox();
        Label costLabel = makeLabel("Cost:");
        TextField costField = new TextField();
        costRow.getChildren().addAll(costLabel, costField);
        HBox endRow = makeHbox();
        Button addButton = makeButton();
        addButton.setText("add");
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    ArrayList<String> inputNames = new ArrayList<>();
                    ArrayList<String> outputNames = new ArrayList<>();
                    for (int i = 0; i < choiceBoxes.size(); i++) {
                        if (i + 1 <= inputsNumber[0]) {
                            inputNames.add(choiceBoxes.get(i).getSelectionModel().getSelectedItem().toString());
                        } else {
                            outputNames.add(choiceBoxes.get(i).getSelectionModel().getSelectedItem().toString());
                        }
                    }
                    String name = nameField.getText();
                    int cost = Integer.parseInt(costField.getText());
                    xmlWriter.addMachine(inputNames, outputNames, name, cost);
                    Toast.makeText(primaryStage, "Added!", 500, 500, 500);
                }catch (NumberFormatException e){
                    Toast.makeText(primaryStage, "please write a number", 500, 500, 500);
                }
            }
        });
        endRow.getChildren().addAll(addButton);
        ScrollPane scrollPane = new ScrollPane();
        layout.getChildren().addAll(titleRow, addInputRow, addOutputRow, nameRow
                , costRow, endRow);
        scrollPane.setContent(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.layout.getChildren().addAll(scrollPane);
    }

    private void makeSceneForDrug() {
        HBox titleRow = makeHbox();
        Label titleLabel = makeLabel("Drug:");
        titleRow.getChildren().addAll(titleLabel);
        HBox costRow = makeHbox();
        Label costLabel = makeLabel("Cost:");
        TextField costField = new TextField();
        costRow.getChildren().addAll(costLabel, costField);
        HBox nameRow = makeHbox();
        Label nameLabel = makeLabel("Name:");
        TextField nameField = new TextField();
        nameRow.getChildren().addAll(nameLabel, nameField);
        HBox healthTitleRow = makeHbox();
        Label healthTitleLabel = makeLabel("Health:");
        healthTitleRow.getChildren().addAll(healthTitleLabel);
        HBox maxHealthRow = makeHbox();
        Label maxHealthLabel = makeLabel("maximum:");
        TextField maxHealthField = new TextField();
        maxHealthRow.getChildren().addAll(maxHealthLabel, maxHealthField);
        HBox currentHealthRow = makeHbox();
        Label currentHealthLabel = makeLabel("current:");
        TextField currentHealthField = new TextField();
        currentHealthRow.getChildren().addAll(currentHealthLabel, currentHealthField);
        HBox refillHealthRow = makeHbox();
        Label refillHealthLabel = makeLabel("refill rate:");
        TextField refillHealthField = new TextField();
        refillHealthRow.getChildren().addAll(refillHealthLabel, refillHealthField);
        HBox consumptionHealthRow = makeHbox();
        Label consumptionHealthLabel = makeLabel("consumption rate:");
        TextField consumptionHealthField = new TextField();
        consumptionHealthRow.getChildren().addAll(consumptionHealthLabel, consumptionHealthField);
        HBox energyTitleRow = makeHbox();
        Label energyTitleLabel = makeLabel("Energy:");
        energyTitleRow.getChildren().addAll(energyTitleLabel);
        HBox maxEnergyRow = makeHbox();
        Label maxEnergyLabel = makeLabel("maximum:");
        TextField maxEnergyField = new TextField();
        maxEnergyRow.getChildren().addAll(maxEnergyLabel, maxEnergyField);
        HBox currentEnergyRow = makeHbox();
        Label currentEnergyLabel = makeLabel("current:");
        TextField currentEnergyField = new TextField();
        currentEnergyRow.getChildren().addAll(currentEnergyLabel, currentEnergyField);
        HBox refillEnergyRow = makeHbox();
        Label refillEnergyLabel = makeLabel("refill rate:");
        TextField refillEnergyField = new TextField();
        refillEnergyRow.getChildren().addAll(refillEnergyLabel, refillEnergyField);
        HBox consumptionEnergyRow = makeHbox();
        Label consumptionEnergyLabel = makeLabel("consumption rate:");
        TextField consumptionEnergyField = new TextField();
        consumptionEnergyRow.getChildren().addAll(consumptionEnergyLabel, consumptionEnergyField);
        HBox endRow = makeHbox();
        Button addButton = makeButton();
        addButton.setText("add");
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    int cost = Integer.parseInt(costField.getText());
                    String name = nameField.getText();
                    int maxHealth = Integer.parseInt(maxHealthField.getText());
                    int curHealth = Integer.parseInt(currentHealthField.getText());
                    int consHealth = Integer.parseInt(consumptionHealthField.getText());
                    int refHealth = Integer.parseInt(refillHealthField.getText());
                    int maxEnergy = Integer.parseInt(maxEnergyField.getText());
                    int curEnergy = Integer.parseInt(currentEnergyField.getText());
                    int consEnergy = Integer.parseInt(consumptionEnergyField.getText());
                    int refEnergy = Integer.parseInt(refillEnergyField.getText());
                    xmlWriter.addDrug(cost, name, maxHealth, curHealth, consHealth, refHealth,
                            maxEnergy, curEnergy, consEnergy, refEnergy);
                    Toast.makeText(primaryStage, "Added!", 500, 500, 500);
                } catch (NumberFormatException e) {
                    Toast.makeText(primaryStage, "please write a number", 500, 500, 500);
                }
            }
        });
        endRow.getChildren().addAll(addButton);
        layout.setSpacing(20);
        layout.getChildren().addAll(titleRow, costRow, nameRow, healthTitleRow,
                maxHealthRow, currentHealthRow, consumptionHealthRow, refillHealthRow, energyTitleRow,
                maxEnergyRow, currentEnergyRow, consumptionEnergyRow, refillEnergyRow, endRow);
    }

    private void makeSceneForPlayer() {
        HBox titleRow = makeHbox();
        Label titleLabel = makeLabel("com.company.Player:");
        titleRow.getChildren().addAll(titleLabel);
        HBox moneyRow = makeHbox();
        Label moneyLabel = makeLabel("money:");
        TextField moneyField = new TextField();
        moneyRow.getChildren().addAll(moneyLabel, moneyField);
        HBox capacityRow = makeHbox();
        Label capacityLabel = makeLabel("Capacity:");
        TextField capacityField = new TextField();
        capacityRow.getChildren().addAll(capacityLabel, capacityField);
        HBox healthTitleRow = makeHbox();
        Label healthTitleLabel = makeLabel("Health:");
        healthTitleRow.getChildren().addAll(healthTitleLabel);
        HBox maxHealthRow = makeHbox();
        Label maxHealthLabel = makeLabel("maximum:");
        TextField maxHealthField = new TextField();
        maxHealthRow.getChildren().addAll(maxHealthLabel, maxHealthField);
        HBox currentHealthRow = makeHbox();
        Label currentHealthLabel = makeLabel("first:");
        TextField currentHealthField = new TextField();
        currentHealthRow.getChildren().addAll(currentHealthLabel, currentHealthField);
        HBox refillHealthRow = makeHbox();
        Label refillHealthLabel = makeLabel("refill rate:");
        TextField refillHealthField = new TextField();
        refillHealthRow.getChildren().addAll(refillHealthLabel, refillHealthField);
        HBox consumptionHealthRow = makeHbox();
        Label consumptionHealthLabel = makeLabel("consumption rate:");
        TextField consumptionHealthField = new TextField();
        consumptionHealthRow.getChildren().addAll(consumptionHealthLabel, consumptionHealthField);
        HBox energyTitleRow = makeHbox();
        Label energyTitleLabel = makeLabel("Energy:");
        energyTitleRow.getChildren().addAll(energyTitleLabel);
        HBox maxEnergyRow = makeHbox();
        Label maxEnergyLabel = makeLabel("maximum:");
        TextField maxEnergyField = new TextField();
        maxEnergyRow.getChildren().addAll(maxEnergyLabel, maxEnergyField);
        HBox currentEnergyRow = makeHbox();
        Label currentEnergyLabel = makeLabel("first:");
        TextField currentEnergyField = new TextField();
        currentEnergyRow.getChildren().addAll(currentEnergyLabel, currentEnergyField);
        HBox refillEnergyRow = makeHbox();
        Label refillEnergyLabel = makeLabel("refill rate:");
        TextField refillEnergyField = new TextField();
        refillEnergyRow.getChildren().addAll(refillEnergyLabel, refillEnergyField);
        HBox consumptionEnergyRow = makeHbox();
        Label consumptionEnergyLabel = makeLabel("consumption rate:");
        TextField consumptionEnergyField = new TextField();
        consumptionEnergyRow.getChildren().addAll(consumptionEnergyLabel, consumptionEnergyField);
        HBox endRow = makeHbox();
        Button addButton = makeButton();
        addButton.setText("add");
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    int money = Integer.parseInt(moneyField.getText());
                    int capacity = Integer.parseInt(moneyField.getText());
                    int maxHealth = Integer.parseInt(maxHealthField.getText());
                    int curHealth = Integer.parseInt(currentHealthField.getText());
                    int consHealth = Integer.parseInt(consumptionHealthField.getText());
                    int refHealth = Integer.parseInt(refillHealthField.getText());
                    int maxEnergy = Integer.parseInt(maxEnergyField.getText());
                    int curEnergy = Integer.parseInt(currentEnergyField.getText());
                    int consEnergy = Integer.parseInt(consumptionEnergyField.getText());
                    int refEnergy = Integer.parseInt(refillEnergyField.getText());
                    xmlWriter.addPlayer(money, capacity, maxHealth, curHealth, consHealth, refHealth,
                            maxEnergy, curEnergy, consEnergy, refEnergy);
                    Toast.makeText(primaryStage, "Added!", 500, 500, 500);
                } catch (NumberFormatException e) {
                    Toast.makeText(primaryStage, "please write a number", 500, 500, 500);
                }
            }
        });
        endRow.getChildren().addAll(addButton);
        layout.setSpacing(20);
        layout.getChildren().addAll(titleRow, moneyRow, capacityRow, healthTitleRow,
                maxHealthRow, currentHealthRow, consumptionHealthRow, refillHealthRow, energyTitleRow,
                maxEnergyRow, currentEnergyRow, consumptionEnergyRow, refillEnergyRow, endRow);
    }

    private void makeSceneForCook() {
        VBox layout = new VBox();
        layout.alignmentProperty().setValue(Pos.TOP_CENTER);
        layout.setSpacing(30);
        layout.setBackground(new Background(new BackgroundImage(
                new Image(this.getClass().getClassLoader().getResource("wooden-texture.jpg").toString()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(
                FirstScene.WIDTH, FirstScene.HEIGHT, false, false, true, true)
        )));
        final int[] ingredientsNumber = {0}, cookingToolsNumber = {0};
        ArrayList<ChoiceBox> choiceBoxes = new ArrayList<>();
        ArrayList<TextField> numberFields = new ArrayList<>();
        ArrayList<String> ingredients = new ArrayList<>();
        for (int i = 0; i < CookIngredient.values().length; i++) {
            ingredients.add(CookIngredient.values()[i].name());
        }
        ingredients.addAll(xmlWriter.getPlantProducts());
        ArrayList<String> cookingTools = new ArrayList<>();
        for (int i = 0; i < CookingTool.values().length; i++) {
            cookingTools.add(CookingTool.values()[i].name());
        }
        HBox titleRow = makeHbox();
        Label titleLabel = makeLabel("Cook:");
        titleRow.getChildren().addAll(titleLabel);
        HBox addIngredientRow = makeHbox();
        Button addIngredientButton = makeButton("Add ingredient");
        addIngredientButton.setOnMouseClicked(event -> {
            ingredientsNumber[0]++;
            HBox ingredientRow = makeHbox();
            Label ingredientLabel = makeLabel("Ingredient" + ingredientsNumber[0] + ":");
            ChoiceBox ingredientChoice = new ChoiceBox(FXCollections.observableArrayList(ingredients));
            choiceBoxes.add(ingredientChoice);
            Label numberLabel = makeLabel("Number:");
            TextField numberField = new TextField();
            numberFields.add(numberField);
            ingredientRow.getChildren().addAll(ingredientLabel, ingredientChoice, numberLabel, numberField);
            layout.getChildren().add(ingredientsNumber[0], ingredientRow);
        });
        addIngredientRow.getChildren().addAll(addIngredientButton);
        HBox addCookingToolRow = makeHbox();
        Button addCookingToolButton = makeButton("Add CookingTool");
        addCookingToolButton.setOnMouseClicked(event -> {
            cookingToolsNumber[0]++;
            HBox cookingToolRow = makeHbox();
            Label cookingToolLabel = makeLabel("Cooking Tool" + cookingToolsNumber[0] + ":");
            ChoiceBox cookingToolChoice = new ChoiceBox(FXCollections.observableArrayList(cookingTools));
            choiceBoxes.add(cookingToolChoice);
            cookingToolRow.getChildren().addAll(cookingToolLabel, cookingToolChoice);
            layout.getChildren().add(layout.getChildren().size() - 5, cookingToolRow);
        });
        addCookingToolRow.getChildren().addAll(addCookingToolButton);
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
        HBox endRow = makeHbox();
        Button addButton = makeButton();
        addButton.setText("add");
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    HashMap<String, Integer> ingredientNames = new HashMap<>();
                    ArrayList<String> cookingToolNames = new ArrayList<>();
                    for (int i = 0; i < choiceBoxes.size(); i++) {
                        if (i + 1 <= ingredientsNumber[0]) {
                            ingredientNames.put(choiceBoxes.get(i).getSelectionModel().getSelectedItem().toString(),
                                    Integer.parseInt(numberFields.get(i).getText()));
                        } else {
                            cookingToolNames.add(choiceBoxes.get(i).getSelectionModel().getSelectedItem().toString());
                        }
                    }
                    String name = nameField.getText();
                    int energy = Integer.parseInt(energyField.getText());
                    int health = Integer.parseInt(healthField.getText());
                    xmlWriter.addCook(ingredientNames, cookingToolNames, name, energy, health);
                    Toast.makeText(primaryStage, "Added!", 500, 500, 500);
                }catch (NumberFormatException e){
                    Toast.makeText(primaryStage, "please write a number", 500, 500, 500);
                }
            }
        });
        endRow.getChildren().addAll(addButton);
        ScrollPane scrollPane = new ScrollPane();
        layout.getChildren().addAll(titleRow, addIngredientRow, addCookingToolRow, nameRow
                , energyRow, healthRow, endRow);
        scrollPane.setContent(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.layout.getChildren().addAll(scrollPane);
    }

    private void makeSceneForTreeFields() {
        ArrayList<String> treeNames = xmlWriter.getTrees();
        treeNames.addAll(Arrays.asList(Names.PEACH_TREE.name(), Names.PEAR_TREE.name(),
                Names.POMEGRANATE_TREE.name(), Names.LEMON_TREE.name(),
                Names.APPLE_TREE.name(), Names.ORANGE_TREE.name()));
        HBox titleRow = makeHbox();
        Label titleLabel = makeLabel("Tree Fields:");
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
        Label titleLabel = makeLabel("Tree:");
        titleRow.getChildren().addAll(titleLabel);
        HBox costRow = makeHbox();
        Label costLabel = makeLabel("cost:");
        TextField costField = new TextField();
        costRow.getChildren().addAll(costLabel, costField);
        HBox browseFileRow = makeHbox();
        Button browseButton = makeButton("Browse Image");
        final File[] imageFile = {null};
        browseButton.setOnMouseClicked(event -> {
            imageFile[0] = new FileChooser().showOpenDialog(primaryStage);
        });
        browseFileRow.getChildren().addAll(browseButton);
        HBox endRow = makeHbox();
        Button addButton = makeButton();
        addButton.setText("add");
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    int productCost = Integer.parseInt(((TextField) productNodes.get(0)).getText());
                    String productName = ((TextField) productNodes.get(1)).getText();
                    int productEnergy = Integer.parseInt(((TextField) productNodes.get(2)).getText());
                    int productHealth = Integer.parseInt(((TextField) productNodes.get(3)).getText());
                    Season productSeason = Season.valueOf(((ChoiceBox) productNodes.get(4)).getSelectionModel().
                            getSelectedItem().toString());
                    xmlWriter.addPlantProduct(productCost, productName, productEnergy, productHealth, productSeason);
                    int treeCost = Integer.parseInt(costField.getText());
                    xmlWriter.addTree(productName, treeCost);
                    if (imageFile[0] != null){
                        copyFileUsingStream(imageFile[0], new File(System.getProperty("user.dir") +
                                "/resource/tree/" + productName + "_TREE.png"));
                    }
                    Toast.makeText(primaryStage, "Added!", 500, 500, 500);
                }catch (NumberFormatException e){
                    Toast.makeText(primaryStage, "please write a number", 500, 500, 500);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        endRow.getChildren().addAll(addButton);
        layout.getChildren().addAll(titleRow, costRow, browseFileRow, endRow);

    }

    private void makeSceneForPlant(ArrayList<Node> productNodes) {
        HBox titleRow = makeHbox();
        Label titleLabel = makeLabel("Plant:");
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
        HBox browseFileRow = makeHbox();
        Button browseButton = makeButton("Browse Image");
        final File[] imageFile = {null};
        browseButton.setOnMouseClicked(event -> {
            imageFile[0] = new FileChooser().showOpenDialog(primaryStage);
        });
        browseFileRow.getChildren().addAll(browseButton);
        HBox endRow = makeHbox();
        Button addButton = makeButton();
        addButton.setText("add");
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {

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
                    if (imageFile[0] != null){
                        copyFileUsingStream(imageFile[0], new File(System.getProperty("user.dir") +
                                "/resource/crete/" + productName + "_PLANT.png"));
                    }
                    Toast.makeText(primaryStage, "Added!", 500, 500, 500);
                }catch (NumberFormatException e){
                    Toast.makeText(primaryStage, "please write a number", 500, 500, 500);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        endRow.getChildren().addAll(addButton);
        layout.getChildren().addAll(titleRow, costRow, ageRow, maxAgeRow, browseFileRow, endRow);
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        if (!dest.exists()){
            dest.createNewFile();
        }
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
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

    public static Label makeLabel(String text) {
        Label label = new Label(text);
        label.setFont(new Font("Arial", 30));
        label.setTextFill(Paint.valueOf("#010221"));
        return label;
    }
}
