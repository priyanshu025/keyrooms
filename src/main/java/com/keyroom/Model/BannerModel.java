package com.keyroom.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class BannerModel {
    @Expose
    boolean status;
    @Expose
    ArrayList<Offers> offers;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<Offers> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Offers> offers) {
        this.offers = offers;
    }

    public class Offers{

        @Expose
        String url;

        @Expose
        ArrayList<OfferImageId> offer_image_id;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ArrayList<OfferImageId> getOffer_image_id() {
            return offer_image_id;
        }

        public void setOffer_image_id(ArrayList<OfferImageId> offer_image_id) {
            this.offer_image_id = offer_image_id;
        }

        public class OfferImageId{

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

}

