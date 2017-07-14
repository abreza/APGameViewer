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

    public static void main(String[] args) {
        Main.args = args;
//        try {
//            init();
//        } catch (IOException e) {
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

    public static void initFirst() throws IOException {
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

    public static Thread test = new Thread(() -> {
        try {
            while (true){
                Thread.sleep(3);
                Socket socket = new Socket("localhost", 4033);
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                if (GameState.player.direction == Animal.Direction.UP){
                    out.println(MapViewer.port + " " + GameState.player.getPosition().x + " " + GameState.player.getPosition().y + " up" + GameState.player.upNumber);
                }
                else if (GameState.player.direction == Animal.Direction.DOWN){
                    out.println(MapViewer.port + " " + GameState.player.getPosition().x + " " + GameState.player.getPosition().y + " down" + GameState.player.downNumber);
                }
                else if (GameState.player.direction == Animal.Direction.LEFT){
                    out.println(MapViewer.port + " " + GameState.player.getPosition().x + " " + GameState.player.getPosition().y + " left" + GameState.player.leftNumber);
                }
                else if (GameState.player.direction == Animal.Direction.RIGHT){
                    out.println(MapViewer.port + " " + GameState.player.getPosition().x + " " + GameState.player.getPosition().y + " right" + GameState.player.rightNumber);
                }
                out.flush();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg = in.readLine();
                String[] message = msg.split(" ");
                if(!message[0].equals("empty")){
                    System.out.println(message[1] + " " + message[2]);
                    boolean b = false;
                    for(Animal player : players.keySet()){
                        if(player.getName().equals("player" + message[0])){
                            players.replace(player, 1);
                            b = true;
                            player.getPosition().x = Integer.parseInt(message[1]);
                            player.getPosition().y = Integer.parseInt(message[2]);
                            Image image = null;
                            if(message[3].startsWith("up")){
                                image = player.up.get(0);
                            }
                            else if(message[3].startsWith("down")){
                                image = player.down.get(0);
                            }
                            else if(message[3].startsWith("left")){
                                image = player.left.get(0);
                            }
                            else if(message[3].startsWith("right")){
                                image = player.right.get(0);
                            }
                            player.setImage(image);
                        }
                    }
                    if(!b){
                        Image image = null;
                        if(message[3].startsWith("up")){
                            image = GameState.player.up.get(0);
                        }
                        else if(message[3].startsWith("down")){
                            image = GameState.player.down.get(0);
                        }
                        else if(message[3].startsWith("left")){
                            image = GameState.player.left.get(0);
                        }
                        else if(message[3].startsWith("right")){
                            image = GameState.player.right.get(0);
                        }
                        Animal player = new Animal(new Position(Integer.parseInt(message[1]), Integer.parseInt(message[2]), GameState.player.getPosition().height,
                                GameState.player.getPosition().width), image, "player" + message[0], GameState.player.getType(),
                                GameState.player.up, GameState.player.down, GameState.player.right, GameState.player.left);
                        players.put(player, 1);
                        ((MapViewer)GameState.gameState.getCurrentState()).getObjectViews().add(player);
                    }
                    for (Animal player:players.keySet()) {
                        if(players.get(player) == 0){
                            players.remove(player);
                        }
                        else{
                            players.replace(player, 0);
                        }
                    }
                }
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    });
}
