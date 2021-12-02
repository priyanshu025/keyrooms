package com.keyroom.Network;


import android.util.Log;

import com.keyroom.Utility.Content;
import com.keyroom.Utility.SharedPrefs;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigHader {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        Log.e("Authorization==>", "@@" + SharedPrefs.getValue(SharedPrefs.Access_Token));

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        //.addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer " + SharedPrefs.getValue(SharedPrefs.Access_Token))
                        .build();
                return chain.proceed(request);
            }
        });
        retrofit = new Retrofit.Builder()
                .baseUrl(Content.Base_URL_ORIGNAL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit;
    }
}
