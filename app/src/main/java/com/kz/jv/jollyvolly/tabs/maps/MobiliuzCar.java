package com.kz.jv.jollyvolly.tabs.maps;

/**
 * Created by madiyarzhenis on 28.09.15.
 */
public class MobiliuzCar {
    int id;
    String address;
    String time;
    String status;

    public MobiliuzCar(int id, String address, String time, String status, String imageUrl, double lat, double lng) {
        this.id = id;
        this.address = address;
        this.time = time;
        this.status = status;
        this.imageUrl = imageUrl;
        this.lat = lat;
        this.lng = lng;
    }

    String imageUrl;
    double lat;
    double lng;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
