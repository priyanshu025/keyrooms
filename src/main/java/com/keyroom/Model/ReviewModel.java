package com.keyroom.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class ReviewModel {

    @Expose
    boolean status;

    @Expose
    ArrayList<Review> reviews;

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public boolean isStatus() {
        return status;
    }


    public class Review {
        @Expose
        String content;

        @Expose
        String rate_number;

        @Expose
        Author author;

        public String getContent() {
            return content;
        }

        public String getRate_number() {
            return rate_number;
        }

        public Author getAuthor() {
            return author;
        }
    }
    public class Author{
        @Expose
        String name;

        public String getName() {
            return name;
        }
    }
}
