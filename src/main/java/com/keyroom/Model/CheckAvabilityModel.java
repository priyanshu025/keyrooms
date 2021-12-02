package com.keyroom.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class CheckAvabilityModel {
    @Expose
    boolean status;
    @Expose
    String messages;
    @Expose
    ArrayList<Rooms> rooms;

    public  ArrayList<Rooms> getRooms(){
        return rooms;
    }
    public void  setRooms(ArrayList<Rooms> rooms){
        this.rooms=rooms;
    }
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public class Rooms {

        @Expose
        Integer id;
        @Expose
        String title;
        @Expose
        Integer price;
        @Expose
        String adults_html;
        @Expose
        String slug;
        @Expose
        String address;
        @Expose
        String image;
        @Expose
        Integer star_rate;
        @Expose
        Integer number;
        @Expose
        Integer number_selected;
        @Expose
        String extra_prices;


        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Integer getStar_rate() {
            return star_rate;
        }

        public void setStar_rate(Integer star_rate) {
            this.star_rate = star_rate;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public String getAdults_html() {
            return adults_html;
        }

        public void setAdults_html(String adults_html) {
            this.adults_html = adults_html;
        }

        public Integer getNumber_selected() {
            return number_selected;
        }

        public void setNumber_selected(Integer number_selected) {
            this.number_selected = number_selected;
        }

        public String getExtra_prices() {
            return extra_prices;
        }

        public void setExtra_prices(String extra_prices) {
            this.extra_prices = extra_prices;
        }
    }
}
