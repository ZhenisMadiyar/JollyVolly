package com.example.jv.jollyvolly.tabs.menu_list;

/**
 * Created by madiyarzhenis on 09.09.15.
 */
public class Parent {
    String name;
    String url;

    public Parent(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
