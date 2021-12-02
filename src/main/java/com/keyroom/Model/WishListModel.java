package com.keyroom.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class WishListModel {
    @Expose
    boolean status;
    @Expose
    String message;
    @Expose
    ArrayList<Wishlist> wishlist;
    @Expose
    Integer total_records;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Wishlist> getWishlist() {
        return wishlist;
    }

    public void setWishlist(ArrayList<Wishlist> wishlist) {
        this.wishlist = wishlist;
    }

    public Integer getTotal_records() {
        return total_records;
    }

    public void setTotal_records(Integer total_records) {
        this.total_records = total_records;
    }

    public class Wishlist{
        @Expose
        Integer id;
        @Expose
        String title;
        @Expose
        String slug;
        @Expose
        String address;
        @Expose
        ArrayList<Gallery> gallery;
        @Expose
        Integer star_rate;
        @Expose
        String price;
        @Expose
        ArrayList<BannerImage> bannerImage;

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

        public Integer getStar_rate() {
            return star_rate;
        }

        public void setStar_rate(Integer star_rate) {
            this.star_rate = star_rate;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public ArrayList<BannerImage> getBannerImage() {
            return bannerImage;
        }

        public void setBannerImage(ArrayList<BannerImage> bannerImage) {
            this.bannerImage = bannerImage;
        }
    }

    public class Gallery{
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

    public class BannerImage{
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
