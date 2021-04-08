package com.maihealthtracker.Models;

import java.util.ArrayList;

public class Breakfast {

    public Breakfast() {
    }

    ArrayList<Icon> foods, drinks, symptoms;

    int health;

    public Breakfast(ArrayList<Icon> foods, ArrayList<Icon> drinks, ArrayList<Icon> symptoms, int health) {
        this.foods = foods;
        this.drinks = drinks;
        this.symptoms = symptoms;
        this.health = health;
    }

    public ArrayList<Icon> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Icon> foods) {
        this.foods = foods;
    }

    public ArrayList<Icon> getDrinks() {
        return drinks;
    }

    public void setDrinks(ArrayList<Icon> drinks) {
        this.drinks = drinks;
    }

    public ArrayList<Icon> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<Icon> symptoms) {
        this.symptoms = symptoms;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
