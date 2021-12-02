package com.keyroom.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class StaySafeModel {
    @Expose
    boolean status;
    @Expose
    ArrayList<StaySafe> stay_safe;

    public boolean isStatus() {
        return status;
    }

    public ArrayList<StaySafe> getStay_safe() {
        return stay_safe;
    }

    public class StaySafe{

        @Expose
        String url;

        @Expose
        ArrayList<offer_image_id> offer_image_id;

        public String getUrl() {
            return url;
        }

        public ArrayList<offer_image_id> getOffer_image_id() {
            return offer_image_id;
        }

        public class offer_image_id{
            @Expose
            String large;
            @Expose
            String thumb;

            public String getLarge() {
                return large;
            }

            public String getThumb() {
                return thumb;
            }
        }
    }
}
