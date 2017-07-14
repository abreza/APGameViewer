package com.company;

import org.newdawn.slick.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reza on 7/10/17.
 */
public class Animal extends ObjectView {
    public List<Image> up;
    public List<Image> down;
    public List<Image> right;
    public List<Image> left;
    public List<ObjectView> animalFood = new ArrayList<>();
    public List<ObjectView> currentObjectViews;
    public int upNumber = 0, downNumber = 0, leftNumber = 0, rightNumber = 0;
    public int moveX = 0, moveY = 0;
    public int Animation_SPEED = 10;
    public enum Direction{LEFT, RIGHT, UP, DOWN}
    public Direction direction = Direction.RIGHT;
    public Animal(Position position, Image image, String name, Type type, List<Image> up, List<Image> down, List<Image> left, List<Image> right) {
        super(position, image, name, type);
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    public void eat(){
        switch (direction){
            case UP:
                upNumber = (upNumber + 1) % (4 * 3);
                setImage(up.get(upNumber / 3)
                        .getScaledCopy(getPosition().width, getPosition().height));
                break;
            case DOWN:
                downNumber = (downNumber + 1) % (4 * 3);
                setImage(down.get(downNumber / 3)
                        .getScaledCopy(getPosition().width, getPosition().height));
                break;
            case LEFT:
                leftNumber = (leftNumber + 1) % (4 * 3);
                setImage(left.get(leftNumber / 3)
                        .getScaledCopy(getPosition().width, getPosition().height));
                break;
            case RIGHT:
                rightNumber = (rightNumber + 1) % (4 * 3);
                setImage(right.get(rightNumber / 3)
                        .getScaledCopy(getPosition().width, getPosition().height));
                break;
        }
    }

    public void move(){
        int b;
        if(getType() == Type.PLAYER){
            b = 3;
        }else{
            b = 2;
        }
        if (moveY < 0) {
            direction = Direction.UP;
            upNumber = (upNumber + 1) % (b * Animation_SPEED);
            setImage(up.get(upNumber / Animation_SPEED)
                    .getScaledCopy(getPosition().width, getPosition().height));
            if(!intersect(currentObjectViews)){
                if (GameState.app.getHeight() / 8 > GameState.player.getPosition().y && getType() == Type.PLAYER) {
                    GameState.firstY -= 1;
                } else {
                    getPosition(true).y -= 1;
                }
            }
            moveY++;
        }
        if (moveY > 0) {
            direction = Direction.DOWN;
            downNumber = (downNumber + 1) % (b * Animation_SPEED);
            setImage(down.get(downNumber / Animation_SPEED)
                    .getScaledCopy(getPosition().width, getPosition().height));
            if(!intersect(currentObjectViews)){
                if (7 * GameState.app.getHeight() / 8 < GameState.player.getPosition().y && getType() == Type.PLAYER) {
                    GameState.firstY += 1;
                } else {
                    getPosition(true).y += 1;
                }
            }
            moveY--;
        }
        if (moveX < 0) {
            direction = Direction.LEFT;
            leftNumber = (leftNumber + 1) % (b * Animation_SPEED);
            setImage(left.get(leftNumber / Animation_SPEED)
                    .getScaledCopy(getPosition().width, getPosition().height));
            if(!intersect(currentObjectViews)){
                if (GameState.app.getHeight() / 8 > GameState.player.getPosition().x && getType() == Type.PLAYER) {
                    GameState.firstX -= 1;
                } else {
                    getPosition(true).x -= 1;
                }
            }
            moveX++;
        }
        if (moveX > 0) {
            direction = Direction.RIGHT;
            rightNumber = (rightNumber + 1) % (b * Animation_SPEED);
            setImage(right.get(rightNumber / Animation_SPEED)
                    .getScaledCopy(getPosition().width, getPosition().height));
            if(!intersect(currentObjectViews)){
                if (7 * GameState.app.getHeight() / 8 < GameState.player.getPosition().x && getType() == Type.PLAYER) {
                    GameState.firstX += 1;
                } else {
                    getPosition(true).x += 1;
                }
            }
            moveX--;
        }
    }
    private boolean intersect(List<ObjectView> objectViews) {
        for (ObjectView objectView:objectViews) {
            if(intersect(objectView.getPosition(true), false)){
                if(this.getType() == Type.PLAYER && objectView.getType() == ObjectView.Type.BUILDING && intersect(objectView.getDoor(), false)){
                    BuildingObjectView building = (BuildingObjectView) objectView;
                    if (building.getStateId() == 3){
                        ((MapViewer) GameState.gameState.getCurrentState()).sendAndGetResponse("greenhouse/repair\n");
                    }
                    else {
                        ((MapViewer) GameState.gameState.getCurrentState()).goTo(building);
                    }
                }
                else if(this.getType() != Type.PLAYER && this.animalFood.contains(objectView))
                    eat();
                return true;
            }

        }
        if(getType() != Type.PLAYER && intersect(GameState.player.getPosition(), true))
            return true;
        return false;
    }


    public boolean intersect(Position position, Boolean p){
        int x = getPosition().x, y = getPosition().y;
        if(p){
            x -= GameState.firstX;
            y -= GameState.firstY;
        }
        if(getType() == Type.PLAYER){
            switch (direction){
                case UP:
                    return pointIntersect(x + GameState.firstX, y + GameState.firstY - 3, position);
                case DOWN:
                    return pointIntersect(x + GameState.firstX, y + GameState.firstY + 3, position);
                case LEFT:
                    return pointIntersect(x + GameState.firstX - 3, y + GameState.firstY, position);
                case RIGHT:
                    return pointIntersect(x + GameState.firstX + 3, y + GameState.firstY, position);
            }
        }
        else{
            switch (direction){
                case UP:
                    return pointIntersect(x + (getPosition().width / 2) + GameState.firstX, y + GameState.firstY - 2, position) ||
                            pointIntersect(x + getPosition().width + GameState.firstX, y + (getPosition().height / 2) + GameState.firstY - 2, position) ||
                            pointIntersect(x + (getPosition().width / 2) + GameState.firstX, y + getPosition().height + GameState.firstY - 2, position) ||
                            pointIntersect(x + GameState.firstX, y + (getPosition().height / 2) + GameState.firstY - 2, position);
                case DOWN:
                    return pointIntersect(x + (getPosition().width / 2) + GameState.firstX, y + GameState.firstY + 2, position) ||
                            pointIntersect(x + getPosition().width + GameState.firstX, y + (getPosition().height / 2) + GameState.firstY + 2, position) ||
                            pointIntersect(x + (getPosition().width / 2) + GameState.firstX, y + getPosition().height + GameState.firstY + 2, position) ||
                            pointIntersect(x + GameState.firstX, y + (getPosition().height / 2) + GameState.firstY + 2, position);
                case LEFT:
                    return pointIntersect(x + (getPosition().width / 2) + GameState.firstX - 2, y + GameState.firstY, position) ||
                            pointIntersect(x + getPosition().width + GameState.firstX - 2, y + (getPosition().height / 2) + GameState.firstY, position) ||
                            pointIntersect(x + (getPosition().width / 2) + GameState.firstX - 2, y + getPosition().height + GameState.firstY, position) ||
                            pointIntersect(x + GameState.firstX - 2, y + (getPosition().height / 2) + GameState.firstY, position);
                case RIGHT:
                    return pointIntersect(x + (getPosition().width / 2) + GameState.firstX + 2, y + GameState.firstY, position) ||
                            pointIntersect(x + getPosition().width + GameState.firstX + 2, y + (getPosition().height / 2) + GameState.firstY, position) ||
                            pointIntersect(x + (getPosition().width / 2) + GameState.firstX + 2, y + getPosition().height + GameState.firstY, position) ||
                            pointIntersect(x + GameState.firstX + 2, y + (getPosition().height / 2) + GameState.firstY, position);
            }
        }
        return false;
    }

    public static boolean pointIntersect(int x, int y, Position position){
        if(position.x < x && position.y < y && position.x + position.width > x && position.y + position.height > y){
            return true;
        }
        return false;
    }

}
