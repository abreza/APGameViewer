package com.company;

import javafx.application.Platform;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MapViewer extends BasicGameState {
    public static int PLAYER_SPEED = 1;
    public TiledMap map;
    private String TMXName;
    public List<ObjectView> objectViews;
    public enum Direction{LEFT, RIGHT, UP, DOWN}
    private Direction playerDirection = Direction.RIGHT;
    private int id;
    private Socket socket;
    private List<String> serverMessages;

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
            ObjectView intersectedView =
                    getIntersectedView(((MapViewer) GameState.gameState.getCurrentState()).objectViews);
            if (intersectedView != null){
                System.out.println(intersectedView.getName());
                sendAndGetResponse("inspect " + intersectedView.getName() + "\n");
            }
        }
        if(input.isKeyDown(Input.KEY_UP)){
            playerDirection = Direction.UP;
            if(!playerIntersect(((MapViewer) GameState.gameState.getCurrentState()).objectViews)) {
                if (GameState.app.getHeight() / 8 > GameState.player.getPosition().y) {
                    GameState.firstY -= PLAYER_SPEED;
                } else {
                    GameState.player.getPosition().y -= PLAYER_SPEED;
                }
            }
        }
        if(input.isKeyDown(Input.KEY_DOWN)){
            playerDirection = Direction.DOWN;
            if(!playerIntersect(((MapViewer) GameState.gameState.getCurrentState()).objectViews)) {
                if (7 * GameState.app.getHeight() / 8 < GameState.player.getPosition().y) {
                    GameState.firstY += PLAYER_SPEED;
                } else {
                    GameState.player.getPosition().y += PLAYER_SPEED;
                }
            }
        }
        if(input.isKeyDown(Input.KEY_LEFT)){
            playerDirection = Direction.LEFT;
            if(!playerIntersect(((MapViewer) GameState.gameState.getCurrentState()).objectViews)) {
                if (GameState.app.getWidth() / 8 > GameState.player.getPosition().x) {
                    GameState.firstX -= PLAYER_SPEED;
                } else {
                    GameState.player.getPosition().x -= PLAYER_SPEED;
                }
            }
        }
        if(input.isKeyDown(Input.KEY_RIGHT)){
            playerDirection = Direction.RIGHT;
            if(!playerIntersect(((MapViewer) GameState.gameState.getCurrentState()).objectViews)) {
                if (7 * GameState.app.getWidth() / 8 < GameState.player.getPosition().x) {
                    GameState.firstX += PLAYER_SPEED;
                } else {
                    GameState.player.getPosition().x += PLAYER_SPEED;
                }
            }
        }
    }

    public void sendAndGetResponse(String message) {
        try {
            send(message);
            TimeUnit.MILLISECONDS.sleep(500);
            System.out.println(serverMessages);
            if (serverMessages.size() >= 1) {
                showMenu(serverMessages);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showMenu(List<String> serverMessages) {
        if (serverMessages.size() == 1){
            MyJDialog dialog = new MyJDialog(new JFrame(), "Message", serverMessages.get(0), this);
            dialog.setSize(500, 300);
        }
        else {
            String[] messages = new String[serverMessages.size() - 1];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = serverMessages.get(i + 1);
            }
            MyJDialog dialog = new MyJDialog(new JFrame(), serverMessages.get(0), this, messages);
            dialog.setSize(500, 300);
        }
    }

    private String send(final String message) throws IOException {
        final String[] res = new String[1];
        res[0] = new String();
        socket = new Socket("localhost", 1377);
        new Thread(() -> {
            try {
                serverMessages = new ArrayList<>();
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.write(message);
                out.flush();
                for (int i = 0; i < 100; i++) {
                    read(in);
                    if (serverMessages == null || serverMessages.get(serverMessages.size() - 1) == null )
                        break;
                    if(serverMessages.get(serverMessages.size() - 1).equals("___")){
                        serverMessages.remove(serverMessages.size() - 1);
                        break;
                    }
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return res[0];
    }

    private void read(BufferedReader in) throws IOException {
        serverMessages.add(in.readLine());
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

    private ObjectView getIntersectedView(List<ObjectView> objectViews){
        for (ObjectView objectView: objectViews){
            if (playerIntersect(objectView.getPosition())){
                return objectView;
            }
        }
        return null;
    }


    private boolean playerIntersect(List<ObjectView> objectViews) {
        for (ObjectView objectView:objectViews) {
            if(playerIntersect(objectView.getPosition())){
                if(objectView.getType() == ObjectView.Type.BUILDING && playerIntersect(objectView.getDoor())){
                    BuildingObjectView building = (BuildingObjectView) objectView;
                    GameState.gameState.enterState(building.getStateId());
                    GameState.player.getPosition().x = building.getFirstPlayerX();
                    GameState.player.getPosition().y = building.getFirstPlayerY();
                    GameState.firstX = building.getFirstX();
                    GameState.firstY = building.getFirstY();
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

