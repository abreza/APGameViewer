package com.company;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;


public class MapViewer extends BasicGameState {
    public TiledMap map;
    private String TMXName;
    public List<ObjectView> objectViews;
    public enum Direction{LEFT, RIGHT, UP, DOWN}
    private Direction playerDirection = Direction.RIGHT;
    private int id;

    public MapViewer(String TMXName, int id) {
        this.id= id;
        this.TMXName = TMXName;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        map = new TiledMap("resource/" + TMXName + ".tmx");
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        String fileName = playerDirection.name().toLowerCase();
        GameState.player.setImage(new Image("resource/person/person-" + fileName + ".png")
                .getScaledCopy(GameState.player.getPosition().width, GameState.player.getPosition().height));
        map.render(-GameState.firstX, -GameState.firstY);
        graphics.drawImage(GameState.player.getImage(), GameState.player.getPosition().x, GameState.player.getPosition().y);
        for (ObjectView objectView : objectViews) {
            if(objectView.getImage() != null){
                graphics.drawImage(objectView.getImage(), objectView.getPosition().x, objectView.getPosition().y);
            }
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        Input input  = gameContainer.getInput();
        if(input.isKeyPressed(Input.KEY_ENTER)){
            
        }
        if(input.isKeyDown(Input.KEY_UP)){
            playerDirection = Direction.UP;
            if(!playerIntersect(((MapViewer) GameState.gameState.getCurrentState()).objectViews)) {
                if (GameState.app.getHeight() / 8 > GameState.player.getPosition().y) {
                    GameState.firstY -= 6;
                } else {
                    GameState.player.getPosition().y -= 6;
                }
            }
        }
        if(input.isKeyDown(Input.KEY_DOWN)){
            playerDirection = Direction.DOWN;
            if(!playerIntersect(((MapViewer) GameState.gameState.getCurrentState()).objectViews)) {
                if (7 * GameState.app.getHeight() / 8 < GameState.player.getPosition().y) {
                    GameState.firstY += 6;
                } else {
                    GameState.player.getPosition().y += 6;
                }
            }
        }
        if(input.isKeyDown(Input.KEY_LEFT)){
            playerDirection = Direction.LEFT;
            if(!playerIntersect(((MapViewer) GameState.gameState.getCurrentState()).objectViews)) {
                if (GameState.app.getWidth() / 8 > GameState.player.getPosition().x) {
                    GameState.firstX -= 6;
                } else {
                    GameState.player.getPosition().x -= 6;
                }
            }
        }
        if(input.isKeyDown(Input.KEY_RIGHT)){
            playerDirection = Direction.RIGHT;
            if(!playerIntersect(((MapViewer) GameState.gameState.getCurrentState()).objectViews)) {
                if (7 * GameState.app.getWidth() / 8 < GameState.player.getPosition().x) {
                    GameState.firstX += 6;
                } else {
                    GameState.player.getPosition().x += 6;
                }
            }
        }
    }

    private void send(final String message) {
        new Thread(() -> {
            Socket socket;
            try {
                socket = new Socket("localhost", 1377);
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                out.write(message);
                out.flush();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    public boolean playerIntersect(Position position){
        int x = GameState.player.getPosition().x;
        int y = GameState.player.getPosition().y;
        switch (playerDirection){
            case UP:
                return pointIntersect(x + GameState.firstX, y + GameState.firstY - 6, position);
            case DOWN:
                return pointIntersect(x + GameState.firstX, y + GameState.firstY + 6, position);
            case LEFT:
                return pointIntersect(x + GameState.firstX - 6, y + GameState.firstY, position);
            case RIGHT:
                return pointIntersect(x + GameState.firstX + 6, y + GameState.firstY, position);
        }
        return false;
    }

    private boolean playerIntersect(List<ObjectView> objectViews) {
        for (ObjectView objectView:objectViews) {
            if(playerIntersect(objectView.getPosition())){
                if(objectView.getType() == ObjectView.Type.BUILDING && playerIntersect(objectView.getDoor())){
                    GameState.gameState.enterState(0);
                    GameState.player.getPosition().x = 20;
                    GameState.player.getPosition().y = 300;
                    GameState.firstX = 0;
                    GameState.firstY = 0;
                }
                return true;
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

    public String getTMXName() {
        return TMXName;
    }

    public void setTMXName(String TMXName) {
        this.TMXName = TMXName;
    }
}

