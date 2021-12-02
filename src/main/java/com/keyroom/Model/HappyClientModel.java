package com.keyroom.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class HappyClientModel {
    @Expose
    boolean status;
    @Expose
    ArrayList<Data> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public class Data{
        @Expose
        String name;
        @Expose
        String desc;
        @Expose
        Integer number_star;
        @Expose
        String image_url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Integer getNumber_star() {
            return number_star;
        }

        public void setNumber_star(Integer number_star) {
            this.number_star = number_star;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
    }
}
