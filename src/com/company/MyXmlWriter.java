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
}
