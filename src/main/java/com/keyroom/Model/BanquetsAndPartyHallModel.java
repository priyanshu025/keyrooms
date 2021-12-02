package com.keyroom.Model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class BanquetsAndPartyHallModel {

    @Expose
    boolean status;
    @Expose
    ArrayList<Banquets> banquets;

    public boolean isStatus() {
        return status;
    }

    public ArrayList<Banquets> getBanquets() {
        return banquets;
    }

    public class Banquets{
        @Expose
        String title;
        @Expose
        String slug;
        @Expose
        String content;
        @Expose
        ArrayList<PartyBannerImageId> party_banner_image_id;
        @Expose
        ArrayList<Gallery> gallery;
        @Expose
        String price;
        @Expose
        String sale_price;
        @Expose
        String address;

        public String getAddress() {
            return address;
        }

        public String getPrice() {
            return price;
        }

        public String getSale_price() {
            return sale_price;
        }

        public String getTitle() {
            return title;
        }

        public String getSlug() {
            return slug;
        }

        public String getContent() {
            return content;
        }

        public ArrayList<PartyBannerImageId> getParty_banner_image_id() {
            return party_banner_image_id;
        }

        public ArrayList<Gallery> getGallery() {
            return gallery;
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


        }

        public class PartyBannerImageId{
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
