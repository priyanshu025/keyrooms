package com.keyroom.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class SearchModel {
    @Expose
    boolean status;
    @Expose
    String messages;
    @Expose
    ArrayList<Rows> rows;
    @Expose
    int total_records;

    @Expose
    int row_count;
    @Expose
    String sold_out_pic;

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

    public int getTotal_records() {
        return total_records;
    }

    public void setTotal_records(int total_records) {
        this.total_records = total_records;
    }

    public int getRow_count() {
        return row_count;
    }

    public void setRow_count(int row_count) {
        this.row_count = row_count;
    }

    public String getSold_out_pic() {
        return sold_out_pic;
    }

    public void setSold_out_pic(String sold_out_pic) {
        this.sold_out_pic = sold_out_pic;
    }

    public ArrayList<Rows> getRows() {
        return rows;
    }


    public void setRows(ArrayList<Rows> rows) {
        this.rows = rows;
    }


    public class Rows{
        @Expose
        Integer id;
        @Expose
        String title;
        @Expose
        String slug;
        @Expose
        String address;
        @Expose
        Integer star_rate;
        @Expose
        String price;
        @Expose
        String sale_price;
        @Expose
        ArrayList<Facilities> terms_by_attribute_in_listing_page;
        @Expose
        ArrayList<Gallery> gallery;
        @Expose
        ArrayList<BannerImage> bannerImage;
        @Expose
        boolean isWishList;
        @Expose
        boolean is_sold;
        @Expose
        ReviewDetail review_detail;
        @Expose
        ArrayList<HotelPolicy> hotel_policy;
        @Expose
        Integer is_featured;

        public String getSale_price() {
            return sale_price;
        }

        public void setSale_price(String sale_price) {
            this.sale_price = sale_price;
        }

        public Integer getIs_featured() {
            return is_featured;
        }

        public void setIs_featured(Integer is_featured) {
            this.is_featured = is_featured;
        }

        public ArrayList<HotelPolicy> getHotel_policy() {
            return hotel_policy;
        }

        public void setHotel_policy(ArrayList<HotelPolicy> hotel_policy) {
            this.hotel_policy = hotel_policy;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public ArrayList<Facilities> getTerms_by_attribute_in_listing_page() {
            return terms_by_attribute_in_listing_page;
        }

        public void setTerms_by_attribute_in_listing_page(ArrayList<Facilities> terms_by_attribute_in_listing_page) {
            this.terms_by_attribute_in_listing_page = terms_by_attribute_in_listing_page;
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

        public boolean isWishList() {
            return isWishList;
        }

        public void setWishList(boolean wishList) {
            isWishList = wishList;
        }

        public ReviewDetail getReview_detail() {
            return review_detail;
        }

        public void setReview_detail(ReviewDetail review_detail) {
            this.review_detail = review_detail;
        }

        public boolean isIs_sold() {
            return is_sold;
        }

        public void setIs_sold(boolean is_sold) {
            this.is_sold = is_sold;
        }
    }

    public class HotelPolicy{
        @Expose
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class Facilities{
        @Expose
        Integer id;
        @Expose
        String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
}
