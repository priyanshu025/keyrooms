package com.keyroom.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class PartyHallDetailsModel {
    @Expose
    boolean status;
    @Expose
    Row row;

    public boolean isStatus() {
        return status;
    }

    public Row getRow() {
        return row;
    }

    public class Row{
        @Expose
        Integer id;
        @Expose
        String title;
        @Expose
        String content;
        @Expose
        String contact_no;
        @Expose
        String email;
        @Expose
        double map_lat;
        @Expose
        double map_lng;
        @Expose
        ArrayList<Gallery> gallery;
        @Expose
        String policy;
        @Expose
        String price;
        @Expose
        String sale_price;
        @Expose
        String star_rate;
        @Expose
        String address;

        public String getAddress() {
            return address;
        }

        public Integer getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getContact_no() {
            return contact_no;
        }

        public String getEmail() {
            return email;
        }

        public double getMap_lat() {
            return map_lat;
        }

        public double getMap_lng() {
            return map_lng;
        }

        public ArrayList<Gallery> getGallery() {
            return gallery;
        }

        public String getPolicy() {
            return policy;
        }

        public String getPrice() {
            return price;
        }

        public String getSale_price() {
            return sale_price;
        }

        public String getStar_rate() {
            return star_rate;
        }


    }
    public class Gallery {
        @Expose
        String large;
        @Expose
        String thumb;

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }
    }
}
