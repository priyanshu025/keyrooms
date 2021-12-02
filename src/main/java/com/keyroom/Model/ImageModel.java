package com.keyroom.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class ImageModel {

    @Expose
    Boolean status;
    @Expose
    String messages;
    @Expose
    ArrayList<LocationCityModel> locations;


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public ArrayList<LocationCityModel> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<LocationCityModel> locations) {
        this.locations = locations;
    }
}
