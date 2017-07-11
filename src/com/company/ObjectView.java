package com.company;

import org.newdawn.slick.Image;


public class ObjectView {

    private Position position;
    private Image image;
    private Position door;
    public enum Type{BUILDING, BUILDING_ITEM, ITEM, PLAYER}
    private Type type;
    private String name;
    private String imagePath = null;

    public ObjectView(Position position, Image image, String name, Type type) {
        this.door = new Position(position.x + (position.width / 2 - 10), position.y + position.height, 1, 1);
        this.type = type;
        this.name = name;
        this.position = position;
        this.image = image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Position getDoor() {
        return door;
    }

    public void setDoor(Position door) {
        this.door = door;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
