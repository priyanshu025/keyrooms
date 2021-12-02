package com.keyroom.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class FilterModel {
    @Expose
    Integer status;
    @Expose
    Data data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        @Expose
        PriceRange price_range;
        @Expose
        Stars stars;
        @Expose
        ArrayList<Attributes> attributes;

        public PriceRange getPrice_range() {
            return price_range;
        }

        public void setPrice_range(PriceRange price_range) {
            this.price_range = price_range;
        }

        public Stars getStars() {
            return stars;
        }

        public void setStars(Stars stars) {
            this.stars = stars;
        }

        public ArrayList<Attributes> getAttributes() {
            return attributes;
        }

        public void setAttributes(ArrayList<Attributes> attributes) {
            this.attributes = attributes;
        }
    }

    public class PriceRange {
        @Expose
        String min_price;
        @Expose
        String max_price;

        public String getMin_price() {
            return min_price;
        }

        public void setMin_price(String min_price) {
            this.min_price = min_price;
        }

        public String getMax_price() {
            return max_price;
        }

        public void setMax_price(String max_price) {
            this.max_price = max_price;
        }
    }

    public class Stars {
        @Expose
        Integer form;
        @Expose
        Integer to;

        public Integer getForm() {
            return form;
        }

        public void setForm(Integer form) {
            this.form = form;
        }

        public Integer getTo() {
            return to;
        }

        public void setTo(Integer to) {
            this.to = to;
        }
    }

    public class Attributes {
        @Expose
        String name;
        @Expose
        ArrayList<Terms> terms;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<Terms> getTerms() {
            return terms;
        }

        public void setTerms(ArrayList<Terms> terms) {
            this.terms = terms;
        }
    }


}
