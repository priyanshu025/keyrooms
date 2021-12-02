package com.keyroom.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class TopDestinationsHotelModel {
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
        int id;
        @Expose
        String name;
        @Expose
        String slug;
        @Expose
        String image_link;
        @Expose
        Service service;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getImage_link() {
            return image_link;
        }

        public void setImage_link(String image_link) {
            this.image_link = image_link;
        }

        public Service getService() {
            return service;
        }

        public void setService(Service service) {
            this.service = service;
        }
    }

    public class Service{
        @Expose
        String type;
        @Expose
        String count;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
