package com.company;

import javafx.application.Platform;
import org.newdawn.slick.Image;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class ObjectView {

    private Position position;
    private Position dynamicPosition;
    private Image image;
    private Position door;
    public enum Type{BUILDING, BUILDING_ITEM, ITEM, PLAYER, ANIMAL}
    private Type type;
    private String name;
    private String imagePath = null;
    private String sound;
    public Thread thread;

    private boolean first = false;
    public void soundPlayStart(){
            thread = new Thread(() -> {
                try {
                    if(sound != null) {
                        while (true) {
                            Thread.sleep(1200);
                            try {
                                InputStream in = new FileInputStream(new File((this.getClass().getResource("animalSounds/" + sound + ".wav")).toURI()));
                                AudioStream audioStream = new AudioStream(in);
                                AudioPlayer.player.start(audioStream);
                            } catch (Exception e) {
                                System.err.println(e.getMessage());
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            first = true;

        thread.start();
    }
    public void soundPlayStop(){
        if(thread != null)
            thread.stop();
    }

    public ObjectView(Position position, Image image, String name, Type type, String sound) {
        this.door = new Position(position.x + (position.width / 2 - 10), position.y + position.height, 1, 1);
        this.type = type;
        this.name = name;
        this.position = position;
        this.dynamicPosition = new Position(0, 0, position.width, position.height);
        this.image = image;
        this.sound = sound;
    }
    public ObjectView(Position position, Image image, String name, Type type) {
        this.door = new Position(position.x + (position.width / 2 - 10), position.y + position.height, 1, 1);
        this.type = type;
        this.name = name;
        this.position = position;
        this.dynamicPosition = new Position(0, 0, position.width, position.height);
        this.image = image;
        this.sound = sound;
    }

    public ObjectView(Position position,  Type type, String name, String imagePath) {
        this.door = new Position(position.x + (position.width / 2 - 10), position.y + position.height, 1, 1);
        this.type = type;
        this.name = name;
        this.position = position;
        this.dynamicPosition = new Position(0, 0, position.width, position.height);
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Position getPosition() {
        if(image == null || type == Type.PLAYER)
            return position;
        else{
            dynamicPosition.x = position.x - GameState.firstX;
            dynamicPosition.y = position.y - GameState.firstY;
            return dynamicPosition;
        }
    }
    public Position getPosition(boolean b) {
        if(image == null || type == Type.PLAYER || b)
            return position;
        else{
            dynamicPosition.x = position.x - GameState.firstX;
            dynamicPosition.y = position.y - GameState.firstY;
            return dynamicPosition;
        }
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
