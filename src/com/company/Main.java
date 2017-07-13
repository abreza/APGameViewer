package com.company;

import org.newdawn.slick.SlickException;

import java.io.*;
import java.net.Socket;


public class Main {
    public static int playerId;
    public static int gameID;
    public static String[] args;

    public static void main(String[] args) {
        Main.args = args;
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            try {
                GameState.run();
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void init() throws IOException {
        Socket socket = new Socket("localhost", 1378);
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println("signUp reza");
        out.flush();
        playerId = Integer.parseInt(in.readLine());
        MapViewer.port = playerId;
        socket.close();
        socket = new Socket("localhost", MapViewer.port);
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        out.println("add 0");
        out.println("createNewGame multiPlayer");
        out.flush();
        gameID = Integer.parseInt(in.readLine());
        socket.close();
    }

}
