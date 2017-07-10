package com.company;


import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import javax.naming.Name;
import javax.print.attribute.standard.MediaSize;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MapViewer extends BasicGameState {
    public static int PLAYER_SPEED = 1;
    public static int Animation_SPEED = 10;
    public static int STATE_ID = 1;
    public TiledMap map;
    private String TMXName;
    public List<ObjectView> objectViews;
    public enum Direction{LEFT, RIGHT, UP, DOWN}
    private Direction playerDirection = Direction.RIGHT;
    private int id;
    private Socket socket;
    private List<String> serverMessages;
    private int Y = 0, X = 0;


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
        map.render(-GameState.firstX, -GameState.firstY);
        if (Y < 0) {
            playerDirection = Direction.UP;
            GameState.player.setImage(GameState.playerUp.get(GameState.upNumber / Animation_SPEED)
                    .getScaledCopy(GameState.player.getPosition().width, GameState.player.getPosition().height));
            GameState.upNumber = (GameState.upNumber + 1) % (3 * Animation_SPEED);
            if(!playerIntersect(((MapViewer) GameState.gameState.getCurrentState()).objectViews)) {
                if (GameState.app.getHeight() / 8 > GameState.player.getPosition().y) {
                    GameState.firstY -= 1;
                } else {
                    GameState.player.getPosition().y -= 1;
                }
            }
            Y++;
        }
        if (Y > 0) {
            playerDirection = Direction.DOWN;
            GameState.player.setImage(GameState.playerDown.get(GameState.downNumber / Animation_SPEED)
                    .getScaledCopy(GameState.player.getPosition().width, GameState.player.getPosition().height));
            GameState.downNumber = (GameState.downNumber + 1) % (3 * Animation_SPEED);
            if(!playerIntersect(((MapViewer) GameState.gameState.getCurrentState()).objectViews)) {
                if (!playerIntersect(((MapViewer) GameState.gameState.getCurrentState()).objectViews)) {
                    if (7 * GameState.app.getHeight() / 8 < GameState.player.getPosition().y) {
                        GameState.firstY += 1;
                    } else {
                        GameState.player.getPosition().y += 1;
                    }
                }
            }
            Y--;
        }
        if (X < 0) {
            playerDirection = Direction.LEFT;
            GameState.player.setImage(GameState.playerLeft.get(GameState.leftNumber / Animation_SPEED)
                    .getScaledCopy(GameState.player.getPosition().width, GameState.player.getPosition().height));
            GameState.leftNumber = (GameState.leftNumber + 1) % (3 * Animation_SPEED);
            if(!playerIntersect(((MapViewer) GameState.gameState.getCurrentState()).objectViews)) {
                if (GameState.app.getHeight() / 8 > GameState.player.getPosition().x) {
                    GameState.firstX -= 1;
                } else {
                    GameState.player.getPosition().x -= 1;
                }
            }
            X++;
        }
        if (X > 0) {
            playerDirection = Direction.RIGHT;
            GameState.player.setImage(GameState.playerRight.get(GameState.rightNumber / Animation_SPEED)
                    .getScaledCopy(GameState.player.getPosition().width, GameState.player.getPosition().height));
            GameState.rightNumber = (GameState.rightNumber + 1) % (3 * Animation_SPEED);
            if(!playerIntersect(((MapViewer) GameState.gameState.getCurrentState()).objectViews)) {
                if (!playerIntersect(((MapViewer) GameState.gameState.getCurrentState()).objectViews)) {
                    if (7 * GameState.app.getHeight() / 8 < GameState.player.getPosition().x) {
                        GameState.firstX += 1;
                    } else {
                        GameState.player.getPosition().x += 1;
                    }
                }
            }
            X--;
        }
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
                if (STATE_ID == 0 || intersectedView.getName().equalsIgnoreCase(Names.HOME.name()))
                    sendAndGetResponse("goto " + intersectedView.getName() + "\n");
                else {
                    checkForBarn(intersectedView);
                    checkForGarden(intersectedView);
                    checkForField(intersectedView);
                    checkForForest(intersectedView);
                    sendAndGetResponse("inspect " + intersectedView.getName() + "\n");
                }
            }
        }
        if(input.isKeyDown(Input.KEY_UP)){
            Y -= PLAYER_SPEED;
        }
        if(input.isKeyDown(Input.KEY_DOWN)){
            Y += PLAYER_SPEED;
        }
        if(input.isKeyDown(Input.KEY_LEFT)){
            X -= PLAYER_SPEED;
        }
        if(input.isKeyDown(Input.KEY_RIGHT)){
            X += PLAYER_SPEED;
        }
    }

    private void checkForForest(ObjectView intersectedView) {
        try{
            for (int i = 0; i < Names.WOOD_NAMES.length; i++) {
                if (intersectedView.getName().equalsIgnoreCase(Names.WOOD_NAMES[i])) {
                    send("inspect " + "woods" + "\n");
                    break;
                }
            }
            for (int i = 0; i < Names.ROCK_NAMES.length; i++) {
                if (intersectedView.getName().equalsIgnoreCase(Names.ROCK_NAMES[i])) {
                    send("inspect " + "rocks" + "\n");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkForField(ObjectView intersectedView) {
        try{
            if (intersectedView.getName().toLowerCase().startsWith("field")){
                send("inspect " + "Field" + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkForGarden(ObjectView intersectedView) {
        try {
            if (intersectedView.getName().toLowerCase().endsWith("_tree")){
                send("inspect " + "Garden" + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkForBarn(ObjectView intersectedView) {
        try {
            if (intersectedView.getName().startsWith("cow") && !intersectedView.getName().endsWith("empty"))
                send("inspect " + "cows" + "\n");
            if (intersectedView.getName().startsWith("chicken") && !intersectedView.getName().endsWith("empty"))
                send("inspect " + "chickens" + "\n");
            if (intersectedView.getName().startsWith("sheep") && !intersectedView.getName().endsWith("empty"))
                send("inspect " + "sheeps" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAndGetResponse(String message) {
        try {
            send(message);
            TimeUnit.MILLISECONDS.sleep(500);
            System.out.println(serverMessages);
            if (serverMessages.size() >= 1) {
                if (serverMessages.get(0).equalsIgnoreCase("Y/N"))
                    showYesNoMenu(serverMessages);
                else if (serverMessages.get(0).equalsIgnoreCase("Number/"))
                    showNumberMenu(serverMessages);
                else if (serverMessages.get(0).equalsIgnoreCase("greenhouse/broken"))
                    repairGreenHouse();
                else if (serverMessages.get(0).equalsIgnoreCase("greenhouse/repaired")) {
                    for (ObjectView objectView:objectViews) {
                        if(objectView.getType() == ObjectView.Type.BUILDING &&
                                objectView.getName().equalsIgnoreCase(Names.GREENHOUSE.name())) {
                            BuildingObjectView building = (BuildingObjectView) objectView;
                            goTo(building);
                        }
                    }
                }
                else if (serverMessages.get(0).equalsIgnoreCase("barnNames")){
                    setBarnNames(serverMessages);
                }
                else if (serverMessages.get(0).equalsIgnoreCase("Backpack/"))
                    showBackPackMenu(serverMessages);
                else if (serverMessages.get(0).toLowerCase().startsWith("farm:") ||
                        serverMessages.get(0).toLowerCase().startsWith("garden:"))
                    return;
                else if (serverMessages.get(0).toLowerCase().startsWith("field:") ||
                        serverMessages.get(0).toLowerCase().startsWith("woods") ||
                        serverMessages.get(0).toLowerCase().startsWith("rocks"))
                    send("back\n");
                else
                    showMenu(serverMessages);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setBarnNames(List<String> serverMessages) {
        List<ObjectView> cowObjectViews = new ArrayList<>();
        List<ObjectView> chickenObjectViews = new ArrayList<>();
        List<ObjectView> sheepObjectViews = new ArrayList<>();
        System.out.println(objectViews.size());
        for (ObjectView objectView: GameState.mapViews.get(2).objectViews) {
            if (objectView.getName().toLowerCase().startsWith("cow")){
                cowObjectViews.add(objectView);
            }
            if (objectView.getName().toLowerCase().startsWith("sheep")){
                sheepObjectViews.add(objectView);
            }
            if (objectView.getName().toLowerCase().startsWith("chicken")){
                chickenObjectViews.add(objectView);
            }
        }
        for (int i = 1; i < serverMessages.size(); i++) {
            if (i >= 1 && i <= 5)
                cowObjectViews.get(i - 1).setName(serverMessages.get(i));
            if (i >= 6 && i <= 15)
                chickenObjectViews.get(i - 6).setName(serverMessages.get(i));
            if (i >= 16)
                sheepObjectViews.get(i - 16).setName(serverMessages.get(i));
        }
    }

    private void repairGreenHouse() {
        try {
            send("goto " + Names.GREENHOUSE.name() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendAndGetResponse("inspect repair greenhouse\n");
    }

    private void showBackPackMenu(List<String> serverMessages) {
        String[] messages = new String[serverMessages.size() - 2];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = serverMessages.get(i + 2);
        }
        BackPackDialog dialog = new BackPackDialog(new JFrame(), serverMessages.get(1), this, messages);
        dialog.setSize(500, 300);
    }

    private void showNumberMenu(List<String> serverMessages) {
        NumberDialog dialog = new NumberDialog(new JFrame(), "number question", serverMessages.get(1), this);
        dialog.setSize(500, 300);
    }

    private void showYesNoMenu(List<String> serverMessages) {
        YesNoDialog dialog = new YesNoDialog(new JFrame(), "yes no question", serverMessages.get(1), this);
        dialog.setSize(500, 300);
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
                    if (serverMessages == null ||
                            (serverMessages.size() >=1 && serverMessages.get(serverMessages.size() - 1) == null ))
                        break;
                    if(serverMessages.size() > 0 &&
                            serverMessages.get(serverMessages.size() - 1).equals("___")){
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
        try {
            serverMessages.add(in.readLine());
        }catch (SocketException e){

        }
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
                    if (building.getStateId() == 3){
                        sendAndGetResponse("greenhouse/repair\n");
                    }
                    else {
                        goTo(building);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void goTo(BuildingObjectView building) {
        try {
            if (building.getStateId() != 2)
                send("goto " + building.getName() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameState.gameState.enterState(building.getStateId());
        STATE_ID = building.getStateId();
        GameState.player.getPosition().x = building.getFirstPlayerX();
        GameState.player.getPosition().y = building.getFirstPlayerY();
        GameState.firstX = building.getFirstX();
        GameState.firstY = building.getFirstY();
        if (building.getStateId() == 2) {
            sendAndGetResponse("goto " + building.getName() + "\n");
        }

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

