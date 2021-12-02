package com.keyroom.Model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class HotelDetailsModel {
    @Expose
    boolean status;
    @Expose
    String messages;
    @Expose
    Row row;
    @Expose
    ReviewDetail review_detail;

    @Expose
    ArrayList<ReviewList> review_list;

    public ArrayList<ReviewList> getReview_list() {
        return review_list;
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

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public ReviewDetail getReview_detail() {
        return review_detail;
    }

    public void setReview_detail(ReviewDetail review_detail) {
        this.review_detail = review_detail;
    }

    @Expose
    ArrayList<Rooms> rooms;

    public ArrayList<Rooms> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Rooms> rooms) {
        this.rooms = rooms;
    }

    public class Row {
        @Expose
        Integer id;
        @Expose
        String title;
        @Expose
        String slug;
        @Expose
        String content;
        @Expose
        String address;
        @Expose
        double map_lat;
        @Expose
        double map_lng;

        public double getMap_lat() {
            return map_lat;
        }

        public double getMap_lng() {
            return map_lng;
        }

        @Expose
        ArrayList<Gallery> gallery;
        @NonNull
        @Expose
        ArrayList<Policy> policy;
        @Expose
        Integer star_rate;
        @Expose
        String price;
        @Expose
        String sale_price;


        @Expose
        ArrayList<BannerImage> bannerImage;
        @Expose
        ArrayList<Attributes> attributes;
        @Expose
        boolean isWishList;


        public String getSale_price() {
            return sale_price;
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

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public ArrayList<Policy> getPolicy() {
            return policy;
        }

        public void setPolicy(ArrayList<Policy> policy) {
            this.policy = policy;
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

        public ArrayList<Attributes> getAttributes() {
            return attributes;
        }

        public void setAttributes(ArrayList<Attributes> attributes) {
            this.attributes = attributes;
        }

        public boolean isWishList() {
            return isWishList;
        }

        public void setWishList(boolean wishList) {
            isWishList = wishList;
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

    public class Policy {
        @Expose
        String title;
        @Expose
        String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

    public class Facilities {
        @Expose
        String name;
        @Expose
        Integer id;
        @Expose
        String image;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public class Attributes {
        @Expose
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Expose
        ArrayList<Facilities> list;

        public ArrayList<Facilities> getFacilities() {
            return list;
        }

        public void setFacilities(ArrayList<Facilities> list) {
            this.list = list;
        }
    }

    public class Rooms {

        @Expose
        Integer id;
        @Expose
        String title;
        @Expose
        String price;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAdults_html() {
            return adults_html;
        }

        public void setAdults_html(String adults_html) {
            this.adults_html = adults_html;
        }
    }

    public class ReviewDetail{
        @Expose
        String score_total;
        @Expose
        String total_review;

        public String getScore_total() {
            return score_total;
        }

        public void setScore_total(String score_total) {
            this.score_total = score_total;
        }

        public String getTotal_review() {
            return total_review;
        }

        public void setTotal_review(String total_review) {
            this.total_review = total_review;
        }
    }

    public class ReviewList{

        @Expose
        String title;
        @Expose
        String content;
        @Expose
        Author author;

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public Author getAuthor() {
            return author;
        }
    }
    public class Author
    {
        @Expose
        String name;

        public String getName() {
            return name;
        }
    }
}
