package com.company;

import org.newdawn.slick.SlickException;

public class Main {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                GameState.run();
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
