package com.keyroom.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class BookingModel {
    @Expose
    boolean status;
    @Expose
    String message;
    @Expose
    ArrayList<Row> row;
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

    public ArrayList<Row> getRow() {
        return row;
    }

    public void setRow(ArrayList<Row> row) {
        this.row = row;
    }

    public Integer getTotal_records() {
        return total_records;
    }

    public void setTotal_records(Integer total_records) {
        this.total_records = total_records;
    }

    public class Booking_Service {
        @Expose
        Integer id;
        @Expose
        String title;
        @Expose
        String slug;
        @Expose
        String address;
        @Expose
        String price;
        @Expose
        Integer star_rate;
        @Expose
        String banner_image_id_link;


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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Integer getStar_rate() {
            if(star_rate!=null)
                return star_rate;
            else
                return 0;
        }

        public void setStar_rate(Integer star_rate) {
            this.star_rate = star_rate;
        }

        public String getBanner_image_id_link() {
            return banner_image_id_link;
        }

        public void setBanner_image_id_link(String banner_image_id_link) {
            this.banner_image_id_link = banner_image_id_link;
        }

    }

    public class Row implements Comparable, Cloneable{
        @Expose
        Integer id;
        @Expose
        Integer customer_id;
        @Expose
        String email;
        @Expose
        String first_name;
        @Expose
        String address;
        @Expose
        String last_name;
        @Expose
        String country;
        @Expose
        String code;
        @Expose
        Booking_Service booking_service;
        @Expose
        String total;
        @Expose
        String status;
        @Expose
        String payment_status;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public Integer getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(Integer id) {
            this.customer_id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Booking_Service getBooking_service() {
            return booking_service;
        }

        public void setBooking_service(Booking_Service booking_service) {
            this.booking_service = booking_service;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        @Override
        public int compareTo(Object o) {
            BookingModel.Row compare = (BookingModel.Row) o;

            if (compare.getId() == this.getId() && compare.status.equals(this.status)) {
                return 0;
            }
            return 1;
        }
        @Override
        public Row clone() {

            Row clone;
            try {
                clone = (Row) super.clone();

            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e); //should not happen
            }

            return clone;
        }
    }

   /* public class Gallery{
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
    }*/

}
