package com.keyroom.Model;

public class CardSwipeModel {
    private int image;
    private String text;

    public CardSwipeModel(int image, String text) {
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public String getText() {
        return text;
    }
}
