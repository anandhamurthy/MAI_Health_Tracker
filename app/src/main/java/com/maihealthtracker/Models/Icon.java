package com.maihealthtracker.Models;

public class Icon {

    public Icon() {
    }

    int icon_id;
    String name, tag;
    boolean selected;
    int count;

    public Icon(int icon_id, String name, String tag, boolean selected, int count) {
        this.icon_id = icon_id;
        this.name = name;
        this.tag = tag;
        this.selected = selected;
        this.count = count;
    }

    public int getIcon_id() {
        return icon_id;
    }

    public void setIcon_id(int icon_id) {
        this.icon_id = icon_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
