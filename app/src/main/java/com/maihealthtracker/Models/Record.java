package com.maihealthtracker.Models;

import java.util.ArrayList;

public class Record {

    public Record() {
    }

    int day, month, year;
    String month_name;

    String record_id, user_id;

    int water_count, weight;

    String notes;

    Breakfast breakfast;
    Lunch lunch;
    Snack snack;
    Dinner dinner;

    public Record(int day, int month, int year, String month_name, String record_id, String user_id, int water_count, int weight, String notes, Breakfast breakfast, Lunch lunch, Snack snack, Dinner dinner) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.month_name = month_name;
        this.record_id = record_id;
        this.user_id = user_id;
        this.water_count = water_count;
        this.weight = weight;
        this.notes = notes;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.snack = snack;
        this.dinner = dinner;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMonth_name() {
        return month_name;
    }

    public void setMonth_name(String month_name) {
        this.month_name = month_name;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getWater_count() {
        return water_count;
    }

    public void setWater_count(int water_count) {
        this.water_count = water_count;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Breakfast getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(Breakfast breakfast) {
        this.breakfast = breakfast;
    }

    public Lunch getLunch() {
        return lunch;
    }

    public void setLunch(Lunch lunch) {
        this.lunch = lunch;
    }

    public Snack getSnack() {
        return snack;
    }

    public void setSnack(Snack snack) {
        this.snack = snack;
    }

    public Dinner getDinner() {
        return dinner;
    }

    public void setDinner(Dinner dinner) {
        this.dinner = dinner;
    }
}
