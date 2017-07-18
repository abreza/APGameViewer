package com.company;

import com.company.Animal;

/**
 * Created by reza on 7/18/17.
 */
public class Player {
    public int ID, x, y, health, energy, chickens, cows, sheeps;
    public Animal animal;

    public Player(int ID, int x, int y, int health, int energy, int chickens, int cows, int sheeps) {
        this.ID = ID;
        this.x = x;
        this.y = y;
        this.health = health;
        this.energy = energy;
        this.chickens = chickens;
        this.cows = cows;
        this.sheeps = sheeps;
    }
}
