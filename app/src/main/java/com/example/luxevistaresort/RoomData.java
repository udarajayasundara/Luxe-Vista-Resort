package com.example.luxevistaresort;

import java.io.Serializable;

public class RoomData implements Serializable {

    String name, description, imageUrl, type, id;
    long price;
    public RoomData() {
    }

    public RoomData(String name, String description, String imageUrl, String type, String id, long price) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.type = type;
        this.id = id;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
