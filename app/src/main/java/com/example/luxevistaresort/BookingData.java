package com.example.luxevistaresort;

import java.io.Serializable;
import com.google.firebase.Timestamp;

public class BookingData implements Serializable {
    String roomName, serviceName, userId, date, type;
    long price;
    Timestamp timestamp;

    public BookingData() {
    }

    public BookingData(String roomName, String serviceName, String userId, long price, String date, String type, Timestamp timestamp) {
        this.roomName = roomName;
        this.serviceName = serviceName;
        this.userId = userId;
        this.price = price;
        this.date = date;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}