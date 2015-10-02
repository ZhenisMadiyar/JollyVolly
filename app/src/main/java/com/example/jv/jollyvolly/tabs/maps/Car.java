package com.example.jv.jollyvolly.tabs.maps;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by madiyarzhenis on 02.09.15.
 */
public class Car implements Parcelable {
    String carId;
    String status;
    String  latLang;
    String time;
    String address;
    String imageUrl;
    int id_car;

    public Car(String carId, String status, String latLang, String time, String address, String imageUrl, int id_car) {
        this.carId = carId;
        this.status = status;
        this.latLang = latLang;
        this.time = time;
        this.address = address;
        this.imageUrl = imageUrl;
        this.id_car = id_car;
    }

    public Car(Parcel in) {
        carId = in.readString();
        status = in.readString();
        latLang = in.readString();
        time = in.readString();
        address = in.readString();
        imageUrl = in.readString();
        id_car = in.readInt();
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLatLang() {
        return latLang;
    }

    public void setLatLang(String latLang) {
        this.latLang = latLang;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(carId);
        dest.writeString(status);
        dest.writeString(latLang);
        dest.writeString(time);
        dest.writeString(address);
        dest.writeString(imageUrl);
        dest.writeInt(id_car);
    }
    // Just cut and paste this for now
    public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        public Car[] newArray(int size) {
            return new Car[size];
        }
    };
}
