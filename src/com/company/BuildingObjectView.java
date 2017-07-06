package com.company;

import org.newdawn.slick.Image;

/**
 * Created by Amin on 7/6/2017.
 */
public class BuildingObjectView extends ObjectView {
    private int stateId = 0, firstPlayerX, firstPlayerY, firstX, firstY;

    public BuildingObjectView(Position position, Image image, Position door, String name, Type type, int stateId) {
        super(position, image, name, type);
        this.stateId = stateId;
    }

    public void setFirstXAndY(int x, int y){
        firstX = x;
        firstY = y;
    }

    public int getFirstX() {
        return firstX;
    }

    public void setFirstX(int firstX) {
        this.firstX = firstX;
    }

    public int getFirstY() {
        return firstY;
    }

    public void setFirstY(int firstY) {
        this.firstY = firstY;
    }

    public void setPlayerXAndY(int x, int y){
        firstPlayerX = x;
        firstPlayerY = y;
    }

    public int getFirstPlayerX() {
        return firstPlayerX;
    }

    public void setFirstPlayerX(int firstPlayerX) {
        this.firstPlayerX = firstPlayerX;
    }

    public int getFirstPlayerY() {
        return firstPlayerY;
    }

    public void setFirstPlayerY(int firstPlayerY) {
        this.firstPlayerY = firstPlayerY;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }


}
