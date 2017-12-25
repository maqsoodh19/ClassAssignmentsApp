package com.example.maqso.classassignmentsapp.Singleton;


import com.example.maqso.classassignmentsapp.Interfaces.ServiceClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by maqso on 10/30/2017.
 */

public class ServiceSingleton {
    private static ServiceClient service;

    public static ServiceClient getInstance() {

        if (service == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            OkHttpClient.Builder okclient = new OkHttpClient.Builder();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            okclient.addInterceptor(logging);

            String url = "http://10.0.2.2:8000/";
            Retrofit builder = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okclient.build())
                    .build();

            service = builder.create(ServiceClient.class);

        }
        return service;
    }

}
