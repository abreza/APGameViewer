package com.company;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class Main {

    public static Map<Animal, Integer> players = new HashMap<>();
    public static int playerId;
    public static int gameID;
    public static String[] args;
    public static int playerViewId = 1;
    public static Image playerAttr;
    public static void main(String[] args) {
        Main.args = args;
//        try {
//            initFirst();
//            Scanner scanner = new Scanner(System.in);
//            if(scanner.nextInt() == 1){
//                initCreateMultiPlayer();
//            }
//            else {
//                initAdd();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SlickException e) {
//            e.printStackTrace();
//        }
        new Thread(() -> {
            try {
                GameState.run();
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void initFirst() throws IOException, SlickException {
        Socket socket = new Socket("localhost", 1378);
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println("signUp ali");
        out.flush();
        playerId = Integer.parseInt(in.readLine());
        MapViewer.port = playerId;
        socket.close();
    }


    public static void initAdd() throws IOException {
        Socket socket = new Socket("localhost", MapViewer.port);
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println("add 0");
        out.flush();
        gameID = Integer.parseInt(in.readLine());
        socket.close();
    }

    public static void initCreateMultiPlayer() throws IOException {
        Socket socket = new Socket("localhost", MapViewer.port);
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println("createNewGame multiPlayer");
        out.flush();
        gameID = Integer.parseInt(in.readLine());
        socket.close();
    }


    public static void initCreateSingle() throws IOException{
        Socket socket = new Socket("localhost", MapViewer.port);
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println("createNewGame singlePlayer");
        out.flush();
        gameID = Integer.parseInt(in.readLine());
        socket.close();
    }

    public static void positions() {
        try {
            Thread.sleep(1);
            Socket socket = new Socket("localhost", 4033 + gameID);
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            if (GameState.player.direction == Animal.Direction.UP){
                out.println(MapViewer.port + " " + (GameState.player.getPosition().x + GameState.firstX - 35) +
                        " " + (GameState.player.getPosition().y + GameState.firstY - 20) + " up" +
                        GameState.player.upNumber / GameState.player.Animation_SPEED);
            }
            else if (GameState.player.direction == Animal.Direction.DOWN){
                out.println(MapViewer.port + " " + (GameState.player.getPosition().x + GameState.firstX - 35) +
                        " " + (GameState.player.getPosition().y + GameState.firstY - 20) + " down" +
                        GameState.player.downNumber / GameState.player.Animation_SPEED);
            }
            else if (GameState.player.direction == Animal.Direction.LEFT){
                out.println(MapViewer.port + " " + (GameState.player.getPosition().x + GameState.firstX - 35) +
                        " " + (GameState.player.getPosition().y + GameState.firstY - 20) + " left" +
                        GameState.player.leftNumber / GameState.player.Animation_SPEED);
            }
            else if (GameState.player.direction == Animal.Direction.RIGHT){
                out.println(MapViewer.port + " " + (GameState.player.getPosition().x + GameState.firstX - 35) +
                        " " + (GameState.player.getPosition().y + GameState.firstY - 20) + " right" +
                        GameState.player.rightNumber / GameState.player.Animation_SPEED);
            }
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = in.readLine();
            String[] message = msg.split(" ");
            for (int i = 0; i < message.length; i += 8) {
                if(message[i].equals("empty")){
                    i -= 7;
                    continue;
                }
//                System.out.println(i + " " + message[i] + " " + message[i + 1] + " " + message[i + 2] + " " + message[i + 3] + " " + message[i + 4]);
                if(message[i].equals(Integer.toString(playerId))){
//                    System.out.println(message[i + 1] + " " + message[i + 2] + " " + message[i + 3]);
                    boolean b = false;
                    if(message[i + 4].equals("1")){
                        for(Animal player : players.keySet()){
                            if(player.getName().equals("player" + message[i])){
                                players.replace(player, 1);
                                b = true;
                                player.getPosition().x = Integer.parseInt(message[i + 1]);
                                player.getPosition().y = Integer.parseInt(message[i + 2]);
                                Image image = null;
                                if(message[i + 3].startsWith("up")){
                                    image = player.up.get(Integer.parseInt(message[i + 3].substring(2, 3)));
                                }
                                else if(message[i + 3].startsWith("down")){
                                    image = player.down.get(Integer.parseInt(message[i + 3].substring(4, 5)));
                                }
                                else if(message[i + 3].startsWith("left")){
                                    image = player.left.get(Integer.parseInt(message[i + 3].substring(4, 5)));
                                }
                                else if(message[i + 3].startsWith("right")){
                                    image = player.right.get(Integer.parseInt(message[i + 3].substring(5, 6)));
                                }
                                player.setImage(image);
                            }
                        }
                        if(!b){
                            Image image = null;
                            if(message[i + 3].startsWith("up")){
                                image = GameState.player.up.get(Integer.parseInt(message[i + 3].substring(2, 3)));
                            }
                            else if(message[i + 3].startsWith("down")){
                                image = GameState.player.down.get(Integer.parseInt(message[i + 3].substring(4, 5)));
                            }
                            else if(message[i + 3].startsWith("left")){
                                image = GameState.player.left.get(Integer.parseInt(message[i + 3].substring(4, 5)));
                            }
                            else if(message[i + 3].startsWith("right")){
                                image = GameState.player.right.get(Integer.parseInt(message[i + 3].substring(5, 6)));
                            }
                            Animal player = new Animal(new Position(Integer.parseInt(message[i + 1]), Integer.parseInt(message[2]), GameState.player.getPosition().height,
                                    GameState.player.getPosition().width), image, "player" + message[i], GameState.player.getType(),
                                    GameState.player.up, GameState.player.down, GameState.player.left, GameState.player.right, "player");
                            players.put(player, 1);
                            GameState.player.currentObjectViews.add(player);
                            ((MapViewer)GameState.gameState.getCurrentState()).getObjectViews().add(player);
                        }
                    }
                    else{
                        for(Animal player : players.keySet()){
                            if(player.getName().equals("player" + message[i])){
                                if(GameState.player.currentObjectViews.contains(player)){
                                    GameState.player.currentObjectViews.remove(player);
                                }
                                if(((MapViewer)GameState.gameState.getCurrentState()).getObjectViews().contains(player)){
                                    ((MapViewer)GameState.gameState.getCurrentState()).getObjectViews().remove(player);
                                }
                                players.remove(player);
                                break;
                            }
                        }
                    }
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
