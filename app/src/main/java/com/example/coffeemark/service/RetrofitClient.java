package com.example.coffeemark.service;

import android.util.Log;

import com.example.coffeemark.util.UrlBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static Retrofit getClient() {
        String serverUrl = new UrlBuilder.Builder()
                .setProtocol("http")
                .setIp("192.168.1.237")
                .setPort("8080")
                .build()
                .buildUrl();
        return new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getClientReg() {
        String serverUrl = new UrlBuilder.Builder()
                .setProtocol("http")
                .setIp("192.168.1.186")
                .setPort("8080")
                .build()
                .buildUrl();
        return new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getClientFS() {
        String serverUrl = new UrlBuilder.Builder()
                .setProtocol("http")
                .setIp("192.168.88.253")
                .setPort("8020")
                .build()
                .buildUrl();
        return new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

