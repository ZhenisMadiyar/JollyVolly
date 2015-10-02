package com.example.jv.jollyvolly.tabs.menu_list;

/**
 * Created by madiyarzhenis on 07.09.15.
 */
public class Child {
    String name;
    String imageUrl;
    String price;
    String size;

    public Child(String name, String imageUrl, String price, String size) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
