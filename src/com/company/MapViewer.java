package com.company;


import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MapViewer extends BasicGameState {
    public static int PLAYER_SPEED = 1;
    public static int STATE_ID = 1;
    public TiledMap map;
    private String TMXName;
    private List<ObjectView> objectViews;
    private List<Animal> animals;
    private int id;
    private Socket socket;
    private List<String> serverMessages;
    private static DatagramSocket UDPServerSocket;
    private static ServerSocket TCPServerSocket;
    private static Socket androidSocket;
    private static PrintWriter out;


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
        for (ObjectView objectView : objectViews) {
            if(objectView.getImage() != null){
                graphics.drawImage(objectView.getImage().
                        getScaledCopy(objectView.getPosition().width, objectView.getPosition().height),
                        objectView.getPosition().x + 20, objectView.getPosition().y + 35);
            }
        }
        GameState.player.move();
        graphics.drawImage(GameState.player.getImage(), GameState.player.getPosition().x, GameState.player.getPosition().y);
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
                    sendAndGetResponse("inspect " + intersectedView.getName() + "\n");
                }
            }
        }
        if(input.isKeyDown(Input.KEY_UP)){
            GameState.player.moveY -= PLAYER_SPEED;
        }
        if(input.isKeyDown(Input.KEY_DOWN)){
            GameState.player.moveY += PLAYER_SPEED;
        }
        if(input.isKeyDown(Input.KEY_LEFT)){
            GameState.player.moveX -= PLAYER_SPEED;
        }
        if(input.isKeyDown(Input.KEY_RIGHT)){
            GameState.player.moveX += PLAYER_SPEED;
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
                else if (serverMessages.get(0).toLowerCase().startsWith("field:"))
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



    private ObjectView getIntersectedView(List<ObjectView> objectViews){
        for (ObjectView objectView: objectViews){
            if (GameState.player.intersect(objectView.getPosition(), false)){
                return objectView;
            }
        }
        return null;
    }




    void goTo(BuildingObjectView building) {
        try {
            if (building.getStateId() != 2)
                send("goto " + building.getName() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameState.gameState.enterState(building.getStateId());
        GameState.player.currentObjectViews = ((MapViewer)GameState.gameState.getState(building.getStateId())).objectViews;
        STATE_ID = building.getStateId();
        GameState.player.getPosition().x = building.getFirstPlayerX();
        GameState.player.getPosition().y = building.getFirstPlayerY();
        GameState.firstX = building.getFirstX();
        GameState.firstY = building.getFirstY();
        if (building.getStateId() == 2) {
            sendAndGetResponse("goto " + building.getName() + "\n");
        }

    }

    public static void UDPSend(String message) throws IOException {
        UDPServerSocket.send(new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getByName("255.255.255.255"), 1377));
    }

    public static void TCPSend(String message) throws IOException {
        out.println(message);
        out.flush();
    }

    public static String UDPRead(DatagramPacket incoming) throws IOException {
        UDPServerSocket.receive(incoming);
        return new String(incoming.getData(), 0, incoming.getLength());
    }

    public static String TCPRead() throws IOException, ClassNotFoundException {
        return new BufferedReader(new InputStreamReader(androidSocket.getInputStream())).readLine();
    }

    public void androidUpdate(){
        new Thread(() -> {
            try {
            TCPServerSocket = new ServerSocket(1377);
            UDPServerSocket = new DatagramSocket(1377);
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            while (true) {
                String Message = UDPRead(incoming);
                if(Message == "up"){
                    GameState.player.moveY -= PLAYER_SPEED;
                }
                if(Message == "down"){
                    GameState.player.moveY += PLAYER_SPEED;
                }
                if(Message == "left"){
                    GameState.player.moveX -= PLAYER_SPEED;
                }
                if(Message == "right"){
                    GameState.player.moveX += PLAYER_SPEED;
                }
                if(Message == "ok"){
                    ObjectView intersectedView =
                            getIntersectedView(((MapViewer) GameState.gameState.getCurrentState()).objectViews);
                    if (intersectedView != null){
                        if (STATE_ID == 0 || intersectedView.getName().equalsIgnoreCase(Names.HOME.name()))
                            sendAndGetResponse("goto " + intersectedView.getName() + "\n");
                        else {
                            checkForBarn(intersectedView);
                            checkForGarden(intersectedView);
                            sendAndGetResponse("inspect " + intersectedView.getName() + "\n");
                        }
                    }
                }
                socket.close();
            }
            }catch (IOException ioe){

            }
        }).start();
    }


    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public List<ObjectView> getObjectViews() {
        return objectViews;
    }

    public void setObjectViews(List<ObjectView> objectViews) {
        this.objectViews = objectViews;
    }

    public String getTMXName() {
        return TMXName;
    }

    public void setTMXName(String TMXName) {
        this.TMXName = TMXName;
    }
}

