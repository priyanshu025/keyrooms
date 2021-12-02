package com.keyroom.Network;

import android.content.Intent;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

/*    @FormUrlEncoded
    @POST("user/sendotp")
    Call<JsonObject> sendotp(
            @Field("country_code") String country_code,
            @Field("number") String number);   */

//////////////////////////////////


    @FormUrlEncoded
    @POST("user/sine")
    Call<JsonObject> login(
            @Field("country_code") String country_code,
            @Field("number") String number);

    @FormUrlEncoded
    @POST("user/signUpWithTrueCaller")
    Call<JsonObject> truecallerLogin(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("phone") String phone,
            @Field("country_code") String country_code
    );

    @FormUrlEncoded
    @POST("user/checkuser")
    Call<JsonObject> checkUser(
            @Field("phone") String phone
    );

    //////////////////////////////////////////////////////
    @FormUrlEncoded
    @POST("user/tan")
    Call<JsonObject> verify(
            @Field("user_id") Integer user_id,
            @Field("otp") String otp);

    @FormUrlEncoded
    @POST("user/cosine")

    Call<JsonObject> register(
            @Field("user_id") Integer user_id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("term") String term,
            @Field("phone") String phone,
            @Field("otp")String otp
            );


/////////////////////////////////////
/*    @FormUrlEncoded
    @POST("user/verifyotp")
    Call<JsonObject> verifyotp(
            @Field("user_id") Integer user_id,
            @Field("otp") String otp);*/

    @GET("verifyCoupon/{coupon_code}")
    Call<JsonObject> couponcode(
            @Path("coupon_code") String input);

    @GET("user/noti")
    Call<JsonObject> getNotification();

    @FormUrlEncoded
    @POST("user/signinwithpassword")
    Call<JsonObject> signinwithpassword(
            @Field("country_code") String country_code,
            @Field("number") String number,
            @Field("password") String password);

  /*  @FormUrlEncoded
    @POST("user/signup")
    Call<JsonObject> signup(
            @Field("user_id") Integer user_id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("otp") String otp);*/

    @GET("locations")
    Call<JsonObject> locations();

    @GET("bq")
    Call<JsonObject> getBanquet();
    @GET("ph")
    Call<JsonObject> getPartyHall();

    @FormUrlEncoded
    @POST("clear")
    Call<JsonObject> clearNotification(@Field("id")String id);

    @GET("discount")
    Call<JsonObject> getFirstUser();

    @GET("dis/{location_id}")
    Call<JsonObject> getDiscountedHotel(@Path("location_id") String location_id);

    @GET("banquet/form/{slug}")
    Call<JsonObject> banquetsForm(@Path("slug") String slug,@Query("name") String name,@Query("phone") String phone,@Query("email")String email,@Query("guests")String guests,@Query("hours")String hours,@Query("purpose")String purpose);

    @GET("offer")
    Call<JsonObject> offers();

    @GET("stay")
    Call<JsonObject> staySafe();
    @FormUrlEncoded
    @POST("user/profile")
    Call<JsonObject> profile(
            @Field("user_id") Integer user_id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("avatar_id") Integer avatar_id,
            @Field("birthday") String birthday,
            @Field("address") String address,
            @Field("address2") String address2,
            @Field("city") String city,
            @Field("state") String state,
            @Field("country") String country,
            @Field("zip_code") String zip_code);

    @FormUrlEncoded
    @POST("tokens")
    Call<JsonObject> getToken(
            @Field("token") String token,
            @Field("mobile") String phone
            );

    @Multipart
    @POST("user/profile/change-avatar")
    Call<JsonObject> uploadImage(@Part MultipartBody.Part file, @Part("user_id") RequestBody requestBodyDe);

    @GET("hotel/searchbyname")
    Call<JsonObject> searchbyname(@Query("name") String name, @Query("start") String start, @Query("end") String end, @Query("adults") String adults, @Query("children") String children, @Query("room") String room, @Query("user_id") Integer user_id,@Query("page_no") int page_no, @Query("sort") String sort);

    @GET("Review")
    Call<JsonObject> review();

    @GET("hotel/{input}")
    Call<JsonObject> hotelDetails(@Path("input") String input, @Query("user_id") Integer user_id);

    @GET("ds/{input}")
    Call<JsonObject> hotelDiscountedDetails(@Path("input") String input,@Query("user_id")Integer user_id);

    @GET("banquets/{slug}")
    Call<JsonObject> banquetsDetails(@Path("slug") String slug,@Query("user_id") Integer user_id);
    @GET("party/{slug}")
    Call<JsonObject> partyDetails(@Path("slug") String slug,@Query("user_id") Integer user_id);

    @FormUrlEncoded
    @POST("user/wishlist")
    Call<JsonObject> addWhishlist(@Field("object_id") int object_id, @Field("object_model") String object_model);


    @GET("user/wishlist/remove")
    Call<JsonObject> removeWishList(@Query("user_id") Integer user_id, @Query("hotel_id") Integer hotel_id);

    @GET("user/wishlist")
    Call<JsonObject> wishList(@Query("user_id") Integer user_id,@Query("page_no") Integer page_no);

    @FormUrlEncoded
    @POST("forgot/password")
    Call<JsonObject> forgotPassword(@Field("number_or_email") String number_or_email);

    @GET("popular/hotel")
    Call<JsonObject> popularHotelList(@Query("user_id") Integer user_id,@Query("page_no") int page_no,@Query("sort") String sort);

    @GET("premium/hotel")
    Call<JsonObject> premiumHotelList(@Query("user_id") Integer user_id,@Query("page_no") int page_no,@Query("sort") String sort);

    @FormUrlEncoded
    @POST("nearby/hotel")
    Call<JsonObject> nearbyHotel(@Field("lat") Double lat, @Field("long") Double lon, @Field("user_id") Integer user_id);

    @FormUrlEncoded
    @POST("hotel/checkroomsavailability")
    Call<JsonObject> checkAvability(@Field("hotel_id") Integer hotel_id, @Field("start_date") String start_date, @Field("end_date") String end_date, @Field("adults") Integer adults, @Field("children") Integer children);

    @FormUrlEncoded
    @POST("hotel/check1")
    Call<JsonObject> checkdiscountedAvailibility(@Field("hotel_id") Integer hotel_id, @Field("start_date") String start_date, @Field("end_date") String end_date, @Field("adults") Integer adults, @Field("children") Integer children);

    @GET("booking/detail/{booking_code}")
    Call<JsonObject> bookingDetilesHistory(@Path("booking_code") String input);

    @FormUrlEncoded
    @POST("booking/doCheckout")
    Call<JsonObject> doCheckOut(@Field("code") String code, @Field("first_name") String first_name, @Field("last_name") String last_name, @Field("email") String email, @Field("phone") String phone, @Field("address_line_1") String address_line_1, @Field("address_line_2") String address_line_2, @Field("city") String city, @Field("state") String state, @Field("zip_code") String zip_code, @Field("country") String country, @Field("customer_notes") String customer_notes, @Field("payment_gateway") String payment_gateway, @Field("term_conditions") String term_conditions);

 /*   @FormUrlEncoded
    @POST("booking/doCheckout")
    Call<JsonObject> doCheckOut(@Field("code") String code, @Field("first_name") String first_name, @Field("last_name") String last_name, @Field("email") String email, @Field("phone") String phone, @Field("payment_gateway") String payment_gateway, @Field("term_conditions") String term_conditions);
*/
    @FormUrlEncoded
    @POST("booking/doCheckout")
    Call<JsonObject> doCheckOut(@Field("code") String code, @Field("first_name") String first_name, @Field("last_name") String last_name, @Field("email") String email, @Field("phone") String phone, @Field("payment_gateway") String payment_gateway, @Field("term_conditions") String term_conditions, @Field("final_price") String payAmount);


    //////////////


    @FormUrlEncoded
    @POST("booking/firstuser")
    Call<JsonObject> customBooking(@Field("first_name") String first_name, @Field("last_name") String last_name, @Field("phone") String phone, @Field("payment_gateway") String payment_gateway, @Field("term_conditions") String term_conditions, @Field("slug") String slug,@Field("hotel_id")String id,@Field("hotel_create_user")String create_user);




    //////////////////////////////



    @GET("get/top/destinations")
    Call<JsonObject> getDestinations();

    @GET("user/booking-history")
    Call<JsonObject> bookingHistory(@Query("status") String status,@Query("page_no") Integer page_no);

    @GET("get/happy/clients")
    Call<JsonObject> getHappyClients();

    @FormUrlEncoded
    @POST("booking/addToCart")
    Call<JsonObject> addToCart(@Field("service_id") String service_id, @Field("service_type") String service_type, @Field("start_date") String start_date, @Field("end_date") String end_date, @Field("adults") String adults, @Field("children") String children, @Field("rooms") String rooms);


    @FormUrlEncoded
    @POST("booking/cartApi")
    Call<JsonObject> cartApi(@Field("service_id") String service_id, @Field("service_type") String service_type, @Field("start_date") String start_date, @Field("end_date") String end_date, @Field("adults") String adults, @Field("children") String children, @Field("rooms") String rooms);


    @GET("hotel/get/filters")
    Call<JsonObject> getFilters();

    @FormUrlEncoded
    @POST("get/filter/hotels")
        Call<JsonObject> getFiltersApply(@Field("price_range")String price_range,@Field("star_rate") String star_rate,@Field("terms") String terms,@Field("page_no") int page_no,@Field("type") String type);

    @GET("user/logout")
    Call<JsonObject> userLogout();

    @GET("booking/{code}/cancel")
    Call<JsonObject> cancelBooking(@Path("code") String code);

    @GET("booking/bookagain/{id}")
    Call<JsonObject> bookingaAgain(@Path("id") Integer id);

    @GET("get/hotel/max_guests")
    Call<JsonObject> getMax_adult();

    @FormUrlEncoded
    @POST("review/addreview")
    Call<JsonObject> rateUsAndWriteReview(@Field("review_title")String review_title,@Field("review_content") String review_content,@Field("review_service_id") Integer review_service_id,@Field("review_service_type") String review_service_type,@Field("review_stats") JSONObject review_stats);

    @FormUrlEncoded
    @POST("global/search")
    Call<JsonObject> globalSearch(@Field("search")String search);

}
