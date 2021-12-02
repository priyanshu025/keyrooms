package com.keyroom.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class PopularHotelModel {
    @Expose
    boolean status;
    @Expose
    String messages;
    @Expose
    ArrayList<Rows> rows;

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

    public ArrayList<Rows> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Rows> rows) {
        this.rows = rows;
    }

    public class Rows {
        @Expose
        int id;
        @Expose
        String title;
        @Expose
        String slug;
        @Expose
        String address;
        @Expose
        String price;
        @Expose
        String sale_price;

        @Expose
        ArrayList<Gallery> gallery;
        @Expose
        ArrayList<BannerImage> bannerImage;
        @Expose
        boolean isWishList;
        @Expose
        boolean is_sold;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public ArrayList<Gallery> getGallery() {
            return gallery;
        }

        public void setGallery(ArrayList<Gallery> gallery) {
            this.gallery = gallery;
        }

        public ArrayList<BannerImage> getBannerImage() {
            return bannerImage;
        }

        public void setBannerImage(ArrayList<BannerImage> bannerImage) {
            this.bannerImage = bannerImage;
        }

        public String getSale_price() {
            return sale_price;
        }

        public void setSale_price(String sale_price) {
            this.sale_price = sale_price;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public boolean isWishList() {
            return isWishList;
        }

        public void setWishList(boolean wishList) {
            isWishList = wishList;
        }

        public void setIs_sold(boolean is_sold) {
            this.is_sold = is_sold;
        }

        public boolean isIs_sold() {
            return is_sold;
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

    public class BannerImage {
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
