package com.company;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MyXmlWriter {


    private String id;
    private Document doc;
    private Element root;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void init(){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
            root = doc.createElement("Custom");
            doc.appendChild(root);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void addPlantProduct(int cost, String name, int energy, int health, CustomMenuScene.Season season){
        Element plantProduct = doc.createElement("PlantProduct");
        root.appendChild(plantProduct);
        Element costEl = doc.createElement("cost");
        costEl.appendChild(doc.createTextNode(Integer.toString(cost)));
        plantProduct.appendChild(costEl);
        Element nameEl = doc.createElement("name");
        nameEl.appendChild(doc.createTextNode(name));
        plantProduct.appendChild(nameEl);
        Element energyEl = doc.createElement("energy");
        energyEl.appendChild(doc.createTextNode(Integer.toString(energy)));
        plantProduct.appendChild(energyEl);
        Element healthEl = doc.createElement("health");
        healthEl.appendChild(doc.createTextNode(Integer.toString(health)));
        plantProduct.appendChild(healthEl);
        Element seasonEl = doc.createElement("season");
        seasonEl.appendChild(doc.createTextNode(season.name()));
        plantProduct.appendChild(seasonEl);
    }

    public void addPlant(String productName, int cost, int age, int maxAge){
        Element plant = doc.createElement("Plant");
        root.appendChild(plant);
        Attr productNameAt = doc.createAttribute("productName");
        productNameAt.setValue(productName);
        plant.setAttributeNode(productNameAt);
        Element costEl = doc.createElement("cost");
        costEl.appendChild(doc.createTextNode(Integer.toString(cost)));
        plant.appendChild(costEl);
        Element ageEl = doc.createElement("age");
        ageEl.appendChild(doc.createTextNode(Integer.toString(age)));
        plant.appendChild(ageEl);
        Element maxAgeEl = doc.createElement("maxAge");
        maxAgeEl.appendChild(doc.createTextNode(Integer.toString(maxAge)));
        plant.appendChild(maxAgeEl);
    }

    public void save(){
        try {
            Attr customId = doc.createAttribute("name");
            customId.setValue(id);
            root.setAttributeNode(customId);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(
                    System.getProperty("user.dir") + "/customs/" + id + ".xml"));

            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void addTree(String productName, int cost) {
        Element tree = doc.createElement("Tree");
        root.appendChild(tree);
        Attr productNameAt = doc.createAttribute("productName");
        productNameAt.setValue(productName);
        tree.setAttributeNode(productNameAt);
        Element costEl = doc.createElement("cost");
        costEl.appendChild(doc.createTextNode(Integer.toString(cost)));
        tree.appendChild(costEl);
    }

    public ArrayList<String> getTrees() {
        ArrayList<String> output = new ArrayList<>();
        NodeList treesList = doc.getElementsByTagName("Tree");
        for (int i = 0; i < treesList.getLength(); i++) {
            Node treeNode = treesList.item(i);
            if (treeNode.getNodeType() == Node.ELEMENT_NODE){
                Element treeElement = (Element) treeNode;
                output.add(treeElement.getAttribute("productName") + "_TREE");
            }
        }
        return output;
    }

    public void addTreeField(String name, int index) {
        Element treeFieldEl = doc.createElement("TreeField");
        root.appendChild(treeFieldEl);
        Attr indexAt = doc.createAttribute("index");
        indexAt.setValue(Integer.toString(index));
        treeFieldEl.setAttributeNode(indexAt);
        Element nameEl = doc.createElement("name");
        nameEl.appendChild(doc.createTextNode(name));
        treeFieldEl.appendChild(nameEl);
    }

    public ArrayList<String> getPlantProducts(){
        ArrayList<String> output = new ArrayList<>();
        NodeList productNodeList = doc.getElementsByTagName("PlantProduct");

        for (int i = 0; i < productNodeList.getLength(); i++) {
            Node productNode = productNodeList.item(i);
            if (productNode.getNodeType() == Node.ELEMENT_NODE){
                Element productElement = (Element) productNode;
                String name = productElement.getElementsByTagName("name").item(0).getTextContent();
                output.add(name);
            }
        }
        return output;
    }

    public void addCook(HashMap<String, Integer> ingredientNames, ArrayList<String> cookingToolNames, String name, int energy, int health) {
        Element cookElement = doc.createElement("Cook");
        root.appendChild(cookElement);
        for (String ingredient :
                ingredientNames.keySet()) {
            Element ingredientEl = doc.createElement("ingredient");
            Attr numberAtr = doc.createAttribute("number");
            numberAtr.setValue(Integer.toString(ingredientNames.get(ingredient)));
            ingredientEl.setAttributeNode(numberAtr);
            ingredientEl.appendChild(doc.createTextNode(ingredient));
            cookElement.appendChild(ingredientEl);
        }
        for (String cookingTool :
                cookingToolNames) {
            Element cookingToolEl = doc.createElement("cookingTool");
            cookingToolEl.appendChild(doc.createTextNode(cookingTool));
            cookElement.appendChild(cookingToolEl);
        }
        Element nameEl = doc.createElement("name");
        nameEl.appendChild(doc.createTextNode(name));
        cookElement.appendChild(nameEl);
        Element energyEl = doc.createElement("energy");
        energyEl.appendChild(doc.createTextNode(Integer.toString(energy)));
        cookElement.appendChild(energyEl);
        Element healthEl = doc.createElement("health");
        healthEl.appendChild(doc.createTextNode(Integer.toString(health)));
        cookElement.appendChild(healthEl);

    }


    public void addPlayer(int money, int capacity, int maxHealth, int curHealth, int consHealth,
                          int refHealth, int maxEnergy, int curEnergy, int consEnergy, int refEnergy) {
        removeLastPlayer();
        Element playerElement = doc.createElement("com.company.Player");
        root.appendChild(playerElement);
        Element moneyEl = doc.createElement("money");
        moneyEl.appendChild(doc.createTextNode(Integer.toString(money)));
        playerElement.appendChild(moneyEl);
        Element capacityEl = doc.createElement("capacity");
        capacityEl.appendChild(doc.createTextNode(Integer.toString(capacity)));
        playerElement.appendChild(capacityEl);
        Element maxHealthEl = doc.createElement("maxHealth");
        maxHealthEl.appendChild(doc.createTextNode(Integer.toString(maxHealth)));
        playerElement.appendChild(maxHealthEl);
        Element curHealthEl = doc.createElement("curHealth");
        curHealthEl.appendChild(doc.createTextNode(Integer.toString(curHealth)));
        playerElement.appendChild(curHealthEl);
        Element consHealthEl = doc.createElement("consHealth");
        consHealthEl.appendChild(doc.createTextNode(Integer.toString(consHealth)));
        playerElement.appendChild(consHealthEl);
        Element refHealthEl = doc.createElement("refHealth");
        refHealthEl.appendChild(doc.createTextNode(Integer.toString(refHealth)));
        playerElement.appendChild(refHealthEl);
        Element maxEnergyEl = doc.createElement("maxEnergy");
        maxEnergyEl.appendChild(doc.createTextNode(Integer.toString(maxEnergy)));
        playerElement.appendChild(maxEnergyEl);
        Element curEnergyEl = doc.createElement("curEnergy");
        curEnergyEl.appendChild(doc.createTextNode(Integer.toString(curEnergy)));
        playerElement.appendChild(curEnergyEl);
        Element consEnergyEl = doc.createElement("consEnergy");
        consEnergyEl.appendChild(doc.createTextNode(Integer.toString(consEnergy)));
        playerElement.appendChild(consEnergyEl);
        Element refEnergyEl = doc.createElement("refEnergy");
        refEnergyEl.appendChild(doc.createTextNode(Integer.toString(refEnergy)));
        playerElement.appendChild(refEnergyEl);

    }

    private void removeLastPlayer(){
        NodeList playerList = doc.getElementsByTagName("com.company.Player");
        if (playerList.getLength() == 0)
            return;
        Element playerElement = (Element) playerList.item(0);
        root.removeChild(playerElement);
    }

    public void addDrug(int cost, String name, int maxHealth, int curHealth, int consHealth, int refHealth, int maxEnergy, int curEnergy, int consEnergy, int refEnergy) {
        Element drugElement = doc.createElement("Drug");
        root.appendChild(drugElement);
        Element costEl = doc.createElement("cost");
        costEl.appendChild(doc.createTextNode(Integer.toString(cost)));
        drugElement.appendChild(costEl);
        Element nameEl = doc.createElement("name");
        nameEl.appendChild(doc.createTextNode(name));
        drugElement.appendChild(nameEl);
        Element maxHealthEl = doc.createElement("maxHealth");
        maxHealthEl.appendChild(doc.createTextNode(Integer.toString(maxHealth)));
        drugElement.appendChild(maxHealthEl);
        Element curHealthEl = doc.createElement("curHealth");
        curHealthEl.appendChild(doc.createTextNode(Integer.toString(curHealth)));
        drugElement.appendChild(curHealthEl);
        Element consHealthEl = doc.createElement("consHealth");
        consHealthEl.appendChild(doc.createTextNode(Integer.toString(consHealth)));
        drugElement.appendChild(consHealthEl);
        Element refHealthEl = doc.createElement("refHealth");
        refHealthEl.appendChild(doc.createTextNode(Integer.toString(refHealth)));
        drugElement.appendChild(refHealthEl);
        Element maxEnergyEl = doc.createElement("maxEnergy");
        maxEnergyEl.appendChild(doc.createTextNode(Integer.toString(maxEnergy)));
        drugElement.appendChild(maxEnergyEl);
        Element curEnergyEl = doc.createElement("curEnergy");
        curEnergyEl.appendChild(doc.createTextNode(Integer.toString(curEnergy)));
        drugElement.appendChild(curEnergyEl);
        Element consEnergyEl = doc.createElement("consEnergy");
        consEnergyEl.appendChild(doc.createTextNode(Integer.toString(consEnergy)));
        drugElement.appendChild(consEnergyEl);
        Element refEnergyEl = doc.createElement("refEnergy");
        refEnergyEl.appendChild(doc.createTextNode(Integer.toString(refEnergy)));
        drugElement.appendChild(refEnergyEl);
    }

    public void addMachine(ArrayList<String> inputNames, ArrayList<String> outputNames, String name, int cost) {
        Element machineElement = doc.createElement("Machine");
        root.appendChild(machineElement);
        for (String inputName :
                inputNames) {
            Element inputEl = doc.createElement("input");
            inputEl.appendChild(doc.createTextNode(inputName));
            machineElement.appendChild(inputEl);
        }
        for (String outputName :
                outputNames) {
            Element outputEl = doc.createElement("output");
            outputEl.appendChild(doc.createTextNode(outputName));
            machineElement.appendChild(outputEl);
        }
        Element nameEl = doc.createElement("name");
        nameEl.appendChild(doc.createTextNode(name));
        machineElement.appendChild(nameEl);
        Element costEl = doc.createElement("cost");
        costEl.appendChild(doc.createTextNode(Integer.toString(cost)));
        machineElement.appendChild(costEl);
    }

    public void addMission(HashMap<String, Integer> inputNames, String name, int fee, int time) {
        Element missionElement = doc.createElement("Mission");
        root.appendChild(missionElement);
        for (String inputName :
                inputNames.keySet()) {
            Element inputEl = doc.createElement("input");
            inputEl.appendChild(doc.createTextNode(inputName));
            Attr numberAtr = doc.createAttribute("number");
            numberAtr.setValue(Integer.toString(inputNames.get(inputName)));
            inputEl.setAttributeNode(numberAtr);
            missionElement.appendChild(inputEl);
        }
        Element nameEl = doc.createElement("name");
        nameEl.appendChild(doc.createTextNode(name));
        missionElement.appendChild(nameEl);
        Element feeEl = doc.createElement("fee");
        feeEl.appendChild(doc.createTextNode(Integer.toString(fee)));
        missionElement.appendChild(feeEl);
        Element timeEl = doc.createElement("time");
        timeEl.appendChild(doc.createTextNode(Integer.toString(time)));
        missionElement.appendChild(timeEl);
    }
}
